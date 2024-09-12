package ar.edu.utn.frbb.tup.controller.validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.exception.DatosMalIngresadosException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.presentation.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.presentation.controller.dto.MovimientosRetiroDepositoDto;
import ar.edu.utn.frbb.tup.presentation.controller.validator.MovimientosValidator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovimientosValidatorTest {

    private MovimientosValidator movimientosValidator;
    private MovimientosRetiroDepositoDto retiroDepositoDto;
    private MovimientoDto movimientosTransferenciasDto;
    private Movimiento movimiento;

    @BeforeEach
    void setUp() {
        movimientosValidator = new MovimientosValidator();
        retiroDepositoDto = new MovimientosRetiroDepositoDto();
        retiroDepositoDto.setMonto(100.0);
        retiroDepositoDto.setTipoMoneda("P");

        movimientosTransferenciasDto = new MovimientoDto();
        movimientosTransferenciasDto.setMonto(100.0);
        movimientosTransferenciasDto.setTipoMoneda("P");
        movimientosTransferenciasDto.setCuentaOrigen(123456L);
        movimientosTransferenciasDto.setCuentaDestino(654321L);

        movimiento = new Movimiento();
        movimiento.setCuentaOrigen(123456L);
        movimiento.setCuentaDestino(654321L);
    }

    @Test
    void validarMovimientosConDatosValidos() {
        assertDoesNotThrow(() -> movimientosValidator.validateMovimientos(retiroDepositoDto));
    }

    @Test
    void validarMovimientosConMontoNulo() {
        retiroDepositoDto.setMonto(null);
        DatosMalIngresadosException thrown = assertThrows(DatosMalIngresadosException.class, () -> movimientosValidator.validateMovimientos(retiroDepositoDto));
        assertEquals("El monto debe ser mayor que cero", thrown.getMessage());
    }

    @Test
    void validarMovimientosConMontoCero() {
        retiroDepositoDto.setMonto(0.0);
        DatosMalIngresadosException thrown = assertThrows(DatosMalIngresadosException.class, () -> movimientosValidator.validateMovimientos(retiroDepositoDto));
        assertEquals("El monto debe ser mayor que cero", thrown.getMessage());
    }

    @Test
    void validarMovimientosConTipoMonedaIncorrecto() {
        retiroDepositoDto.setTipoMoneda("X");
        DatosMalIngresadosException thrown = assertThrows(DatosMalIngresadosException.class, () -> movimientosValidator.validateMovimientos(retiroDepositoDto));
        assertEquals("Tipo de moneda no válido: X", thrown.getMessage());
    }

    @Test
    void validarMovimientosTransferenciasConDatosValidos() {
        assertDoesNotThrow(() -> movimientosValidator.validateMovimientosTransferencias(movimientosTransferenciasDto));
    }

    @Test
    void validarMovimientosTransferenciasConCuentasIguales() {
        movimientosTransferenciasDto.setCuentaDestino(123456L);
        TipoCuentaNoSoportadaException thrown = assertThrows(TipoCuentaNoSoportadaException.class, () -> movimientosValidator.validateMovimientosTransferencias(movimientosTransferenciasDto));
        assertEquals("La cuenta origen y destino no pueden ser la misma", thrown.getMessage());
    }

    @Test
    void validarMovimientosTransferenciasConMontoNulo() {
        movimientosTransferenciasDto.setMonto(null);
        DatosMalIngresadosException thrown = assertThrows(DatosMalIngresadosException.class, () -> movimientosValidator.validateMovimientosTransferencias(movimientosTransferenciasDto));
        assertEquals("El monto debe ser mayor que cero", thrown.getMessage());
    }

    @Test
    void validarMovimientosTransferenciasConTipoMonedaIncorrecto() {
        movimientosTransferenciasDto.setTipoMoneda("X");
        DatosMalIngresadosException thrown = assertThrows(DatosMalIngresadosException.class, () -> movimientosValidator.validateMovimientosTransferencias(movimientosTransferenciasDto));
        assertEquals("Tipo de moneda no válido: X", thrown.getMessage());
    }

    @Test
    void validarCuentaOrigenDestinoConCuentasIguales() {
        movimiento.setCuentaDestino(123456L);
        DatosMalIngresadosException thrown = assertThrows(DatosMalIngresadosException.class, () -> movimientosValidator.validarCuentaOrigenDestino(movimiento));
        assertEquals("La cuenta de origen y destino no pueden ser la misma", thrown.getMessage());
    }

    @Test
    void validarCuentaOrigenDestinoConCuentasDiferentes() {
        assertDoesNotThrow(() -> movimientosValidator.validarCuentaOrigenDestino(movimiento));
    }
}
