package ar.edu.utn.frbb.tup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.controller.dto.DepositoResponseDto;
import ar.edu.utn.frbb.tup.controller.dto.RetiroDto;
import ar.edu.utn.frbb.tup.controller.dto.RetiroResponseDto;
import ar.edu.utn.frbb.tup.controller.dto.TransaccionDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.persistence.MovimientoDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoDao movimientoDao;

    @Autowired
    private CuentaService cuentaService;

    public DepositoResponseDto realizarDeposito(DepositoDto depositoDto) throws CantidadNegativaException {
        Cuenta cuenta = cuentaService.find(depositoDto.getNumeroCuenta());
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }

        if (depositoDto.getMonto() <= 0) {
            throw new CantidadNegativaException("El monto a depositar debe ser mayor que cero.");
        }

        cuenta.acreditarEnCuenta(depositoDto.getMonto());

        Movimiento movimiento = new Movimiento(
                generarIdMovimiento(),
                LocalDate.now(),
                LocalTime.now(),
                "Depósito realizado",
                TipoMovimiento.DEPOSITO,
                depositoDto.getMonto(),
                cuenta,
                null,
                depositoDto.getTipoMoneda()
        );

        cuenta.agregarMovimiento(movimiento);
        cuentaService.update(cuenta);
        movimientoDao.addMovimiento(movimiento);

        return new DepositoResponseDto("Depósito exitoso", cuenta.getNumeroCuenta(), depositoDto.getMonto());
    }

    public RetiroResponseDto realizarRetiro(RetiroDto retiroDto) throws CantidadNegativaException, NoAlcanzaException {
        Cuenta cuenta = cuentaService.find(retiroDto.getNumeroCuenta());
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }

        if (retiroDto.getMonto() <= 0) {
            throw new CantidadNegativaException("El monto a retirar debe ser mayor que cero.");
        }

        cuenta.debitarDeCuenta(retiroDto.getMonto());

        Movimiento movimiento = new Movimiento(
                generarIdMovimiento(),
                LocalDate.now(),
                LocalTime.now(),
                "Retiro realizado",
                TipoMovimiento.RETIRO,
                retiroDto.getMonto(),
                cuenta,
                null,
                retiroDto.getMoneda()
        );

        cuenta.agregarMovimiento(movimiento);
        cuentaService.update(cuenta);
        movimientoDao.addMovimiento(movimiento);

        return new RetiroResponseDto("Retiro exitoso", cuenta.getNumeroCuenta(), retiroDto.getMonto());
    }

    public TransferenciaResponseDto realizarTransferencia(TransferenciaDto transferenciaDto) throws CantidadNegativaException, NoAlcanzaException {
        Cuenta cuentaOrigen = cuentaService.find(transferenciaDto.getCuentaOrigen());
        Cuenta cuentaDestino = cuentaService.find(transferenciaDto.getCuentaDestino());

        if (cuentaOrigen == null || cuentaDestino == null) {
            throw new IllegalArgumentException("Una o ambas cuentas no existen.");
        }

        if (!cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda())) {
            throw new IllegalArgumentException("Las cuentas deben tener la misma moneda.");
        }

        if (transferenciaDto.getMonto() <= 0) {
            throw new CantidadNegativaException("El monto debe ser mayor a cero.");
        }

        double monto = transferenciaDto.getMonto();
        if (cuentaOrigen.getTipoBanco() != cuentaDestino.getTipoBanco()) {
            monto = aplicarRecargo(monto, cuentaOrigen.getMoneda());
        }

        cuentaOrigen.debitarDeCuenta(monto);
        cuentaDestino.acreditarEnCuenta(monto);

        Movimiento movimientoSalida = new Movimiento(
                generarIdMovimiento(),
                LocalDate.now(),
                LocalTime.now(),
                "Transferencia a cuenta " + cuentaDestino.getNumeroCuenta(),
                TipoMovimiento.TRANSFERENCIA_SALIENTE,
                monto,
                cuentaOrigen,
                cuentaDestino,
                cuentaOrigen.getMoneda()
        );

        registrarMovimiento(movimientoSalida); 

        Movimiento movimientoEntrada = new Movimiento(
                generarIdMovimiento(),
                LocalDate.now(),
                LocalTime.now(),
                "Transferencia desde cuenta " + cuentaOrigen.getNumeroCuenta(),
                TipoMovimiento.TRANSFERENCIA_ENTRANTE,
                monto,
                cuentaDestino,
                cuentaOrigen,
                cuentaDestino.getMoneda()
        );
        registrarMovimiento(movimientoEntrada);

        cuentaService.update(cuentaOrigen);
        cuentaService.update(cuentaDestino);
        return new TransferenciaResponseDto("200", "Transferencia realizada correctamente");
    }
    
    private long generarIdMovimiento() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }


    @SuppressWarnings("unchecked")
    public List<Movimiento> obtenerMovimientosPorCuenta(long numeroCuenta) {
        Cuenta cuenta = cuentaService.find(numeroCuenta);
        Movimiento movimientos = new Movimiento();
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }
        movimientos = (Movimiento) movimientoDao.findByNumeroCuenta(cuenta.getNumeroCuenta());
        return (List<Movimiento>) movimientos;
    }

    public List<TransaccionDto> obtenerHistorialTransacciones(long numeroCuenta) {
        Cuenta cuenta = cuentaService.find(numeroCuenta);
        if (cuenta == null) {
            System.out.println("Cuenta no encontrada para el número: " + numeroCuenta);
            throw new RuntimeException("Cuenta no encontrada");
        }

        List<Movimiento> movimientos = cuenta.getMovimientos();
        System.out.println("Número de movimientos encontrados para la cuenta " + numeroCuenta + ": " + movimientos.size());

        List<TransaccionDto> transacciones = movimientos.stream()
                .map(movimiento -> {
                    TransaccionDto dto = new TransaccionDto(
                            movimiento.getFecha(),
                            obtenerTipoMovimientoString(movimiento.getTipoMovimiento()),
                            movimiento.getDescripcion(),
                            movimiento.getMonto());
                    System.out.println("Movimiento mapeado: " + dto);
                    return dto;
                })
                .collect(Collectors.toList());

        System.out.println("Número de transacciones mapeadas: " + transacciones.size());
        return transacciones;
    }   

    public void registrarMovimiento(Movimiento movimiento) {
        movimientoDao.addMovimiento(movimiento);
    }


    private String obtenerTipoMovimientoString(TipoMovimiento tipoMovimiento) {
        switch (tipoMovimiento) {
            case TRANSFERENCIA_ENTRANTE:
                return "Transferencia Entrante";
            case TRANSFERENCIA_SALIENTE:
                return "Transferencia Saliente";
            case DEPOSITO:
                return "Depósito";
            case RETIRO:
                return "Retiro";
            default:
                return "Desconocido";
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
