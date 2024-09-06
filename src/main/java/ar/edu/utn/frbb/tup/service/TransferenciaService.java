package ar.edu.utn.frbb.tup.service;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;

@Service
public class TransferenciaService {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private MovimientoService movimientoService;

    public TransferenciaResponseDto realizarTransferencia(TransferenciaDto transferenciaDto) {
        Cuenta cuentaOrigen = cuentaService.find(transferenciaDto.getCuentaOrigen());
        Cuenta cuentaDestino = cuentaService.find(transferenciaDto.getCuentaDestino());

        if (cuentaOrigen == null || cuentaDestino == null) {
            return new TransferenciaResponseDto("FALLIDA", "Una o ambas cuentas no existen.");
        }

        if (!cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda())) {
            return new TransferenciaResponseDto("FALLIDA", "Las monedas de las cuentas deben ser las mismas.");
        }

        if (cuentaOrigen.getBalance() < transferenciaDto.getMonto()) {
            return new TransferenciaResponseDto("FALLIDA", "El saldo de la Cuenta Origen es insuficiente.");
        }

        double monto = transferenciaDto.getMonto();

        if (cuentaOrigen.getTipoBanco() != cuentaDestino.getTipoBanco()) {
            monto = aplicarRecargo(monto, cuentaOrigen.getMoneda());
        }

        try {
            cuentaOrigen.debitarDeCuenta(monto);
            cuentaDestino.acreditarEnCuenta(monto); 
        } catch (NoAlcanzaException | CantidadNegativaException e) {
            return new TransferenciaResponseDto("FALLIDA", e.getMessage());
        }

        // Registro del movimiento para la cuenta origen (TRANSFERENCIA_SALIENTE)
        Movimiento movimientoSalida = new Movimiento(
            0, 
            LocalDate.now(),
            LocalTime.now(),
            "Transferencia a cuenta " + cuentaDestino.getNumeroCuenta(),
            TipoMovimiento.TRANSFERENCIA_SALIENTE,
            monto,
            cuentaOrigen,
            cuentaDestino,
            cuentaOrigen.getMoneda()
        );
        movimientoService.registrarMovimiento(movimientoSalida);

        // Registro del movimiento para la cuenta destino (TRANSFERENCIA_ENTRANTE)
        Movimiento movimientoEntrada = new Movimiento(
            0, 
            LocalDate.now(),
            LocalTime.now(),
            "Transferencia desde cuenta " + cuentaOrigen.getNumeroCuenta(),
            TipoMovimiento.TRANSFERENCIA_ENTRANTE,
            monto,
            cuentaDestino,
            cuentaOrigen,
            cuentaOrigen.getMoneda()
        );
        movimientoService.registrarMovimiento(movimientoEntrada);

        cuentaService.update(cuentaOrigen);
        cuentaService.update(cuentaDestino);

        return new TransferenciaResponseDto("EXITOSA", "Transferencia realizada con éxito.");
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
