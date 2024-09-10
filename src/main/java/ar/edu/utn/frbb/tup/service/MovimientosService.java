package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosRetiroDepositoDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovimientosService {

    @Autowired
    BanelcoService banelcoService;

    @Autowired
    CuentaDao cuentaDao;

    public Movimiento depositar(MovimientosRetiroDepositoDto deposito)
            throws CuentaNotFoundException, DiferenteMonedaException {

        Cuenta cuenta = cuentaDao.find(deposito.getNumeroCuenta());
        if (cuenta == null) {
            throw new CuentaNotFoundException("Cuenta no encontrada");
        }


        if (deposito.getMonto() < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }

        TipoMoneda tipoMonedaDeposito = TipoMoneda.fromString(deposito.getTipoMoneda());
        if (!cuenta.getMoneda().equals(tipoMonedaDeposito)) {
            throw new DiferenteMonedaException("Moneda incompatible");
        }

        cuenta.setBalance(cuenta.getBalance() + deposito.getMonto());
        Movimiento movimiento = new Movimiento(deposito);
        movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        cuenta.agregarMovimiento(movimiento);
        cuentaDao.update(cuenta);

        return new Movimiento(deposito);
    }


    public Movimiento retirar(MovimientosRetiroDepositoDto retiro)
            throws CuentaSinFondosException, CuentaNotFoundException, DiferenteMonedaException {

        Cuenta cuenta = cuentaDao.find(retiro.getNumeroCuenta());
        if (cuenta == null) {
            throw new CuentaNotFoundException("Cuenta no encontrada");
        }

        TipoMoneda tipoMonedaRetiro = TipoMoneda.fromString(retiro.getTipoMoneda());
        if (!cuenta.getMoneda().equals(tipoMonedaRetiro)) {
            throw new DiferenteMonedaException("Moneda incompatible");
        }

        if (cuenta.getBalance() < retiro.getMonto()) {
            throw new CuentaSinFondosException("Fondos insuficientes");
        }

        cuenta.setBalance(cuenta.getBalance() - retiro.getMonto());
        Movimiento movimiento = new Movimiento(retiro);
        movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);
        cuenta.agregarMovimiento(movimiento);
        cuentaDao.update(cuenta);

        return new Movimiento(retiro);
    }

    public Movimiento transferir(MovimientoDto transferenciaDto)
            throws CuentaNotFoundException, CuentaSinFondosException, DiferenteMonedaException {
        Cuenta cuentaOrigen = cuentaDao.find(transferenciaDto.getCuentaOrigen());
        Cuenta cuentaDestino = cuentaDao.find(transferenciaDto.getCuentaDestino());


        if (cuentaOrigen == null) {
            throw new CuentaNotFoundException("La cuenta de origen no existe");
        }

        Movimiento transferencia = new Movimiento(transferenciaDto);

        if (cuentaDestino != null) {
            if (cuentaOrigen.getBalance() >= transferencia.getMonto()) {
                if (cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda())) {
                    double comision = calcularComision(transferenciaDto, cuentaOrigen.getMoneda());

                    cuentaOrigen.setBalance(cuentaOrigen.getBalance() - transferencia.getMonto() - comision);
                    cuentaDestino.setBalance(cuentaDestino.getBalance() + transferencia.getMonto());

                    Movimiento movimientoOrigen = new Movimiento(transferenciaDto);
                    Movimiento movimientoDestino = new Movimiento(transferenciaDto);

                    movimientoOrigen.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA);
                    movimientoDestino.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA);

                    cuentaOrigen.agregarMovimiento(movimientoOrigen);
                    cuentaDestino.agregarMovimiento(movimientoDestino);

                    cuentaDao.save(cuentaOrigen);
                    cuentaDao.save(cuentaDestino);

                } else {
                    throw new DiferenteMonedaException("Las monedas entre cuentas debe ser la misma");
                }
            } else {
                throw new CuentaSinFondosException("El monto supera al dinero disponible en la cuenta");
            }

        } else {
            banelcoExternal(transferenciaDto, cuentaOrigen);
        }

        return new Movimiento(transferenciaDto);
    }


    private double calcularComision(MovimientoDto transferencia, TipoMoneda tipoMoneda) {
        double monto = transferencia.getMonto();
        double comision = 0;

        // Aplicar comisión según la moneda y monto de transferencia
        switch (tipoMoneda) {
            case PESOS -> {
                if (monto > 1000000) {
                    comision = monto * 0.02; // 2% de comisión si supera $1,000,000
                }
            }
            case DOLARES -> {
                if (monto > 5000) {
                    comision = monto * 0.005; // 0.5% de comisión si supera U$S 5,000
                }
            }
        }

        return comision;
    }

    public void banelcoExternal(MovimientoDto transferencia, Cuenta cuentaOrigen)
            throws CuentaNotFoundException, CuentaSinFondosException {

        // Verificar que la cuenta origen tenga fondos suficientes
        double comision = calcularComision(transferencia, cuentaOrigen.getMoneda());
        double montoTotal = transferencia.getMonto() + comision;

        if (cuentaOrigen.getBalance() < montoTotal) {
            throw new CuentaSinFondosException("Fondos insuficientes para realizar la transferencia");
        }

        // Llamar al servicio externo para realizar la transferencia
        boolean transferenciaExitosa = banelcoService.realizarTransferenciaBanelco(
                transferencia.getCuentaOrigen(),
                transferencia.getCuentaDestino(),
                transferencia.getMonto()
        );

        if (transferenciaExitosa) {
            // Actualizar el saldo de la cuenta origen y registrar el movimiento
            cuentaOrigen.setBalance(cuentaOrigen.getBalance() - montoTotal);

            Movimiento movimiento = new Movimiento(transferencia);
            movimiento.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA);
            cuentaOrigen.agregarMovimiento(movimiento);

            // Guardar las actualizaciones en la base de datos
            cuentaDao.update(cuentaOrigen);
        } else {
            throw new CuentaNotFoundException("Transferencia fallida: cuenta destino no encontrada");
        }
    }

}