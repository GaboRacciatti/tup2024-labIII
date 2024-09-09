package ar.edu.utn.frbb.tup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosRetiroDepositoDto;
import ar.edu.utn.frbb.tup.controller.validator.MovimientosValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CuentaSinFondosException;
import ar.edu.utn.frbb.tup.model.exception.DiferenteMonedaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;


@Service
public class MovimientoService {

    @Autowired
    CuentaDao cuentaDao;

    @Autowired
    BanelcoService banelcoService;

    @Autowired
    MovimientosValidator movimientosValidator;

    public void realizarDeposito(MovimientosRetiroDepositoDto depositoDto) throws CantidadNegativaException {
        Cuenta cuenta = cuentaDao.find(depositoDto.getNumeroCuenta());
        if (cuenta != null) {
            Movimiento movimiento = new Movimiento(depositoDto);
            if (cuenta.getMoneda() == movimiento.getMoneda()) {  // Comparación directa de enums
                cuenta.setBalance(cuenta.getBalance() + depositoDto.getMonto());
                movimiento.setTipo(TipoMovimiento.DEPOSITO);
                cuenta.agregarMovimiento(movimiento);
                cuentaDao.update(cuenta);
            } else {
                throw new IllegalArgumentException("La moneda no coincide");
            }
        } else {
            throw new IllegalArgumentException("La cuenta no existe");
        }
    }

    public void realizarRetiro(MovimientosRetiroDepositoDto retiroDto) throws CantidadNegativaException, NoAlcanzaException {
        Cuenta cuenta = cuentaDao.find(retiroDto.getNumeroCuenta());
        if (cuenta != null) {
            if (cuenta.getBalance() >= retiroDto.getMonto()) {
                cuenta.setBalance(cuenta.getBalance() - retiroDto.getMonto());
                Movimiento movimiento = new Movimiento(retiroDto);
                movimiento.setTipo(TipoMovimiento.RETIRO);
                cuenta.agregarMovimiento(movimiento);
                cuentaDao.update(cuenta);
            } else {
                throw new CantidadNegativaException("El monto de la cuenta no alcanza para realizar el retiro");
            }
        } else {
            throw new IllegalArgumentException("La cuenta no existe");
        }
    }

    public void transferir(MovimientoDto movimientosDto) throws CuentaNotFoundException, DiferenteMonedaException, CantidadNegativaException, CuentaSinFondosException {
        Cuenta cuentaOrigen = cuentaDao.find(movimientosDto.getCuentaOrigen());
        Cuenta cuentaDestino = cuentaDao.find(movimientosDto.getCuentaDestino());

        if (cuentaOrigen != null) {
            Movimiento movimiento = new Movimiento(movimientosDto);

            TipoMoneda monedaOrigen = movimientosDto.getTipoMoneda();  
            if (cuentaOrigen.getMoneda() == monedaOrigen) { 

                if (cuentaDestino != null) {  // Si la cuenta destino existe

                    if (cuentaOrigen.getBalance() >= movimientosDto.getMonto()) {  // El balance debe ser suficiente

                        if (cuentaOrigen.getMoneda() == cuentaDestino.getMoneda()) {  // Comparación de monedas entre cuentas

                            cuentaOrigen.setBalance(cuentaOrigen.getBalance() - aplicarRecargo(movimientosDto.getMonto(), monedaOrigen));
                            cuentaDestino.setBalance(cuentaDestino.getBalance() + movimientosDto.getMonto());
                            registrarTransferencias(movimientosDto, cuentaOrigen, cuentaDestino);
                        } else {
                            throw new DiferenteMonedaException("Las monedas entre cuentas deben ser las mismas");
                        }
                    } else {
                        throw new CantidadNegativaException("El monto supera al dinero disponible en la cuenta");
                    }
                } else {
                    invocacionServicioBanelco(movimiento, movimientosDto, cuentaOrigen);
                }
            } else {
                throw new DiferenteMonedaException("Son diferentes monedas");
            }
        } else {
            throw new CuentaNotFoundException("La cuenta de origen no existe");
        }
    }

    public void registrarTransferencias(MovimientoDto movimientosDto, Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        Movimiento movimientoOrigen = new Movimiento(movimientosDto);
        Movimiento movimientoDestino = new Movimiento(movimientosDto);

        movimientoOrigen.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA_SALIENTE);
        movimientoDestino.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA_ENTRANTE);

        cuentaOrigen.agregarMovimiento(movimientoOrigen);
        cuentaDestino.agregarMovimiento(movimientoDestino);

        cuentaDao.save(cuentaOrigen);
        cuentaDao.save(cuentaDestino);
    }

    public void invocacionServicioBanelco(Movimiento movimiento, MovimientoDto movimientosDto, Cuenta cuentaOrigen) throws CuentaNotFoundException, CuentaSinFondosException {
        if (cuentaOrigen.getBalance() >= movimientosDto.getMonto()) {
            boolean transferenciaExterna = banelcoService.transferenciaBanelco();  // Lógica del servicio Banelco

            if (transferenciaExterna) {
                cuentaOrigen.setBalance(cuentaOrigen.getBalance() - movimientosDto.getMonto());
                movimiento.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA);
                cuentaOrigen.agregarMovimiento(movimiento);
                cuentaDao.save(cuentaOrigen);

                System.out.println("Transferencia externa exitosa.");
            } else {
                throw new CuentaNotFoundException("La cuenta externa no existe");
            }
        } else {
            throw new CuentaSinFondosException("No posee los fondos suficientes");
        }
    }

    private double aplicarRecargo(double monto, TipoMoneda moneda) {
        double recargo = 0;
        if (moneda == TipoMoneda.PESOS && monto > 1000000) {
            recargo = monto * 0.02;
        }
        if (moneda == TipoMoneda.DOLARES && monto > 5000) {
            recargo = monto * 0.005;
        }
        return monto + recargo;
    }
}
    