package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        ClienteDto clienteMenorDeEdad = new ClienteDto();
        clienteMenorDeEdad.setFechaNacimiento("2020-02-07");
        clienteMenorDeEdad.setApellido("Rino");
        clienteMenorDeEdad.setNombre("Pepe");
        clienteMenorDeEdad.setDni(123456789);
        clienteMenorDeEdad.setTipoPersona("F");
        clienteMenorDeEdad.setBanco("Galicia");
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setFechaNacimiento("1978-03-25");
        clienteDto.setDni(29857643);
        clienteDto.setTipoPersona("F");
        Cliente cliente = clienteService.darDeAltaCliente(clienteDto);

        verify(clienteDao, times(1)).save(cliente);
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        ClienteDto pepeRino = new ClienteDto();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento("1978-03-25");
        pepeRino.setTipoPersona("F");

        when(clienteDao.find(26456437, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }

    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta,26456439);

        verify(clienteDao, times(1)).save(pepeRino);

        // Verificar que la cuenta se haya agregado
        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(cuenta, pepeRino.getCuentas().get(0));
        assertEquals(pepeRino.getDni(), cuenta.getDniTitular());
    }

    @Test
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException {
        Cliente luciano = new Cliente();
        luciano.setDni(26456439);
        luciano.setNombre("Pepe");
        luciano.setApellido("Rino");
        luciano.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        luciano.setTipoPersona(TipoPersona.fromString("F"));

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(luciano);

        clienteService.agregarCuenta(cuenta, luciano.getDni());

        // Crear la segunda cuenta duplicada
        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        // Verificar que se lance una excepción al intentar agregar una cuenta duplicada
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, luciano.getDni()));

        // Verificar que el cliente solo tenga una cuenta
        verify(clienteDao, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano.getDni(), cuenta.getDniTitular());
    }

    @Test
    public void testAgregarDosCuentasCajaAhorroYCuentaCorrienteEnPesos() throws TipoCuentaAlreadyExistsException {
        Cliente peperino = new Cliente();
        peperino.setDni(26456439);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        peperino.setTipoPersona(TipoPersona.fromString("F"));

        Cuenta cuenta1 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(100)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(peperino);

        clienteService.agregarCuenta(cuenta1, peperino.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(100)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        assertDoesNotThrow(() -> clienteService.agregarCuenta(cuenta2, peperino.getDni()), "Las cuentas son iguales.");

        // Verifica que se haya llamado a save() dos veces
        verify(clienteDao, times(2)).save(peperino);

        // Verifica que el cliente tenga 2 cuentas
        assertEquals(2, peperino.getCuentas().size());

        // Verifica el tipo de cuentas
        assertTrue(peperino.getCuentas().stream()
                .anyMatch(c -> c.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && c.getMoneda() == TipoMoneda.PESOS));
        assertTrue(peperino.getCuentas().stream()
                .anyMatch(c -> c.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && c.getMoneda() == TipoMoneda.PESOS));
    }

    @Test
    public void testAgregarDosCuentasCajaAhorroEnPesosYDolares() throws TipoCuentaAlreadyExistsException {
        Cliente peperino = new Cliente();
        peperino.setDni(26456439);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        peperino.setTipoPersona(TipoPersona.fromString("F"));

        Cuenta cuenta1 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(100)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(peperino);

        clienteService.agregarCuenta(cuenta1, peperino.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.DOLARES)
                .setBalance(100)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        assertDoesNotThrow(() -> clienteService.agregarCuenta(cuenta2,peperino.getDni()), "Las cuentas son iguales");

        // Verifica que se haya llamado a save() dos veces
        verify(clienteDao, times(2)).save(peperino);

        // Verifica que el cliente tenga 2 cuentas
        assertEquals(2, peperino.getCuentas().size());

        // Verifica las cuentas
        assertTrue(peperino.getCuentas().stream()
                .anyMatch(c -> c.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && c.getMoneda() == TipoMoneda.PESOS));
        assertTrue(peperino.getCuentas().stream()
                .anyMatch(c -> c.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && c.getMoneda() == TipoMoneda.DOLARES));
    }
}
