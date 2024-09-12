package ar.edu.utn.frbb.tup.controller.validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.utn.frbb.tup.presentation.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.presentation.controller.validator.ClienteValidator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClienteValidatorTest {

    private ClienteValidator clienteValidator;
    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        clienteValidator = new ClienteValidator();
        clienteDto = new ClienteDto();
        clienteDto.setNombre("John");
        clienteDto.setApellido("Doe");
        clienteDto.setTipoPersona("Física");
        clienteDto.setDireccion("123 Main St");
        clienteDto.setTelefono("1234567890");
    }

    @Test
    void validarConDatosVálidos() {
        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    @Test
    void validarConNombreNulo() {
        clienteDto.setNombre(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("El nombre no puede estar vacío", thrown.getMessage());
    }

    @Test
    void validarConNombreVacio() {
        clienteDto.setNombre("");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("El nombre no puede estar vacío", thrown.getMessage());
    }

    @Test
    void validarConApellidoNulo() {
        clienteDto.setApellido(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("El apellido no puede estar vacío", thrown.getMessage());
    }

    @Test
    void validarConApellidoVacio() {
        clienteDto.setApellido("");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("El apellido no puede estar vacío", thrown.getMessage());
    }

    @Test
    void validarConTipoPersonaNulo() {
        clienteDto.setTipoPersona(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("El tipo de persona no puede estar vacío", thrown.getMessage());
    }

    @Test
    void validarConTipoPersonaVacio() {
        clienteDto.setTipoPersona("");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("El tipo de persona no puede estar vacío", thrown.getMessage());
    }

    @Test
    void validarConDireccionNula() {
        clienteDto.setDireccion(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("La direccion no puede estar vacía", thrown.getMessage());
    }

    @Test
    void validarConDireccionVacia() {
        clienteDto.setDireccion("");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("La direccion no puede estar vacía", thrown.getMessage());
    }

    @Test
    void validarConTelefonoNulo() {
        clienteDto.setTelefono(null);
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("El Telefono no puede estar vacío", thrown.getMessage());
    }

    @Test
    void validarConTelefonoVacio() {
        clienteDto.setTelefono("");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteDto));
        assertEquals("El Telefono no puede estar vacío", thrown.getMessage());
    }
}
