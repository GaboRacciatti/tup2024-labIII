package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.MenorEdadException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.presentation.controller.dto.ClienteDto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        assertThrows(MenorEdadException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException, MenorEdadException {
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
    void buscarClientePorDni_Success() throws ClienteNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        Cliente result = clienteService.buscarClientePorDni(12345678L);

        assertNotNull(result);
        assertEquals(12345678L, result.getDni());
    }

    @Test
    void BuscarClientePorDni_Fail() throws ClienteNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);
        cliente.setNombre("Pepe");
        cliente.setApellido("Rino");
        cliente.setFechaNacimiento(LocalDate.of(1978, 3, 25));
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        cliente.setDireccion("Zelarrayan 655");
        cliente.setTelefono("1123456789");


        when(clienteDao.find(123456, true)).thenReturn(null);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.buscarClientePorDni(123456));

    }
    @Test
    void testClienteTieneCuenta_Success() throws ClienteNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);

        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.PESOS);

        cliente.addCuenta(cuenta);


        boolean resultado = cliente.tieneCuenta(TipoCuenta.CUENTA_CORRIENTE, TipoMoneda.PESOS);
        assertTrue(resultado);
    }
    
    @Test
    public void obtenerTodosLosClientes() throws ClienteAlreadyExistsException, MenorEdadException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Pepe");
        clienteDto.setApellido("Rino");
        clienteDto.setFechaNacimiento("1978-03-25");
        clienteDto.setTipoPersona("F");
        clienteDto.setDireccion("Zelarrayan 655");
        clienteDto.setTelefono("1123456789");
        clienteService.darDeAltaCliente(clienteDto);
        
        assertEquals(0, clienteService.obtenerTodosLosClientes().size());
    }

    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException, ClienteNotFoundException {
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

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(cuenta, pepeRino.getCuentas().get(0));
        assertEquals(pepeRino.getDni(), cuenta.getDniTitular());
    }

    @Test
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException, ClienteNotFoundException {
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

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, luciano.getDni()));

        verify(clienteDao, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano.getDni(), cuenta.getDniTitular());
    }

    @Test
    public void testEditarClientePorDni_Success() throws ClienteNotFoundException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDireccion("Nueva Dirección");
        clienteDto.setTelefono("1122334455");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setDni(12345678L);

        when(clienteDao.find(12345678L, true)).thenReturn(clienteExistente);

        Cliente clienteActualizado = clienteService.editarClientPorDni(12345678L, clienteDto);

        assertNotNull(clienteActualizado);
        assertEquals("Nueva Dirección", clienteActualizado.getDireccion());
        assertEquals("1122334455", clienteActualizado.getTelefono());
    }

    @Test
    public void testEditarClientePorDni_Fail() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDireccion("Nueva Dirección");

        when(clienteDao.find(12345678L, true)).thenReturn(null);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.editarClientPorDni(12345678L, clienteDto));
    }

    @Test
    public void testEliminarClientePorDni_Success() throws ClienteNotFoundException {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678L);

        when(clienteDao.find(12345678L, true)).thenReturn(cliente);

        Cliente clienteEliminado = clienteService.eliminarClientePorDni(12345678L);

        assertNotNull(clienteEliminado);
        assertEquals(12345678L, clienteEliminado.getDni());
        verify(clienteDao, times(1)).delete(cliente);
    }

    @Test
    public void testEliminarClientePorDni_Fail() {
        when(clienteDao.find(12345678L, true)).thenReturn(null);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.eliminarClientePorDni(12345678L));
    }

}
