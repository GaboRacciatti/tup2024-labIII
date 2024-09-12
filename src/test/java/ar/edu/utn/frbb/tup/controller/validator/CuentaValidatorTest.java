package ar.edu.utn.frbb.tup.controller.validator; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.utn.frbb.tup.presentation.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.presentation.controller.validator.CuentaValidator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CuentaValidatorTest {

    private CuentaValidator cuentaValidator;
    private CuentaDto cuentaDto;

    @BeforeEach
    void setUp() {
        cuentaValidator = new CuentaValidator();
        cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("CC");
        cuentaDto.setMoneda("P");
    }

    @Test
    void validarConDatosVálidos() {
        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto));
    }

    @Test
    void validarConTipoCuentaIncorrecto() {
        cuentaDto.setTipoCuenta("ZZ");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validate(cuentaDto));
        assertEquals("El tipo de cuenta no es correcto", thrown.getMessage());
    }

    @Test
    void validarConMonedaIncorrecta() {
        cuentaDto.setMoneda("X");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validate(cuentaDto));
        assertEquals("El tipo de moneda no es correcto", thrown.getMessage());
    }

    @Test
    void validarConTipoCuentaNulo() {
        cuentaDto.setTipoCuenta(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validate(cuentaDto));
        assertEquals("El tipo de cuenta no es correcto", thrown.getMessage());
    }

    @Test
    void validarConMonedaNula() {
        cuentaDto.setMoneda(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validate(cuentaDto));
        assertEquals("El tipo de moneda no es correcto", thrown.getMessage());
    }

    @Test
    void validarConCuentaNula() {
        cuentaDto = null;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> cuentaValidator.nohaycuentas(cuentaDto));
        assertEquals("No hay cuentas", thrown.getMessage());
    }

    @Test
    void validarConCuentaNoNula() {
        assertDoesNotThrow(() -> cuentaValidator.nohaycuentas(cuentaDto));
    }
}
