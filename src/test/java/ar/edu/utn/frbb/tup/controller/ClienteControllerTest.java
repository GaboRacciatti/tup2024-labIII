package ar.edu.utn.frbb.tup.controller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.DatosMalIngresadosException;
import ar.edu.utn.frbb.tup.model.exception.MenorEdadException;
import ar.edu.utn.frbb.tup.service.ClienteService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void testCrearCliente_Success() throws ClienteAlreadyExistsException, MenorEdadException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(123456789);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("1990-01-01");
        clienteDto.setTipoPersona("F");

        Cliente cliente = new Cliente(clienteDto);

        assertEquals(clienteDto, cliente);
        }    

    @Test
    void testCrearCliente_InvalidName() {
        // 
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(123456789);
        clienteDto.setNombre(" "); // invalid name
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("1990-01-01");

        // Act and Assert
        assertThrows(DatosMalIngresadosException.class, () -> clienteController.crearCliente(clienteDto));
    }

    @Test
    void testCrearCliente_InvalidDateOfBirth() {
        // Arrange
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(123456789);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("2022-01-01"); // invalid date of birth

        // Act and Assert
        assertThrows(InvalidDateOfBirthException.class, () -> clienteController.crearCliente(clienteDto));
    }
}