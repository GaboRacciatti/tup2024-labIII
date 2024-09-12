package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.controller.dto.CuentaDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private Cliente cliente;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private CuentaDto cuentaDto;


    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCuentaSuccess() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("CC");
        cuentaDto.setMoneda("P");
        cuentaDto.setNumeroCuenta(123L);
        cuentaDto.setBalance(1000.0);
    }    
    
    

    @Test
    public void testTipoCuentaNoSoportada() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("CC");
        cuentaDto.setMoneda("D");

        assertThrows(TipoCuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    public void testObtenerTodasLasCuentas() {
        Cliente cliente = new Cliente();
        cliente.setDni(123L);
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setNumeroCuenta(123L);
        cuenta1.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta1.setMoneda(TipoMoneda.PESOS);
        cliente.addCuenta(cuenta1);
        Cuenta cuenta2 = new Cuenta();
        cuenta2.setNumeroCuenta(124L);
        cuenta2.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta2.setMoneda(TipoMoneda.PESOS);
        cliente.addCuenta(cuenta2);

        List<Cuenta> cuentas = cliente.getCuentas();

        assertEquals(2, cuentas.size());
        assertEquals(123L, cuentas.get(0).getNumeroCuenta());
        assertEquals(124L, cuentas.get(1).getNumeroCuenta());
    }

    @Test
    public void testObtenerCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        assertEquals(123L, cuenta.getNumeroCuenta());
        assertEquals(TipoCuenta.CAJA_AHORRO, cuenta.getTipoCuenta());
        assertEquals(TipoMoneda.PESOS, cuenta.getMoneda());
    }
    
    @Test
    public void testObtenerTodasLasCuentas_NoCuentas() throws CuentaNotFoundException {
        List<Cuenta> cuentasObtenidas = cuentaService.obtenerTodasLasCuentas();

        assertTrue(cuentasObtenidas.isEmpty());
    }
    @Test
    public void testTieneFondosSuficientes_NotFound() {
        assertThrows(CuentaNotFoundException.class, () -> cuentaService.tieneFondosSuficientes(123L, 500.0));
    }
    @Test
    public void testTipoCuentaNoSoportada_Success() {
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.DOLARES);

        boolean tipoSoportado = cuentaService.tipoCuentaEstaSoportada(cuenta);

        assertFalse(tipoSoportado);
    }

    @Test
    public void testTipoCuentaSoportada_Success() {
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        boolean tipoSoportado = cuentaService.tipoCuentaEstaSoportada(cuenta);

        assertTrue(tipoSoportado);
    }

    @Test
    public void testDarDeAltaDosCuentasACliente() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        Cliente peperino = new Cliente();
        peperino.setDni(26456439);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);
        
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta1.setMoneda(TipoMoneda.PESOS);
        cuenta1.setNumeroCuenta(123L);
        cuenta1.setBalance(1000.0);

        cliente.addCuenta(cuenta1);
        cuentaDao.save(cuenta1);

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta2.setMoneda(TipoMoneda.DOLARES);
        cuenta2.setNumeroCuenta(124L);
        cuenta2.setBalance(2000.0);

        cliente.addCuenta(cuenta2);
        cuentaDao.save(cuenta1);

        verify(cuentaDao, times(2)).save(any());
    }

    @Test
    public void testCuentaDatosInvalidos() {
        CuentaDto dtoNulo = new CuentaDto();
        dtoNulo.setTipoCuenta(null);
        dtoNulo.setMoneda(null);
        dtoNulo.setNumeroCuenta(0L);
        dtoNulo.setBalance(0L);

        assertThrows(IllegalArgumentException.class, () -> cuentaService.darDeAltaCuenta(dtoNulo));
    }

    @Test
    void find_Success() throws CuentaNotFoundException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException, CuentaAlreadyExistsException {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(1000.0);
        cuentaDao.save(cuenta);
        assertEquals(123L, cuenta.getNumeroCuenta());
    }

    @Test
    void find_Fail() throws CuentaNotFoundException {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(1000.0);
        cuentaDao.save(cuenta);
        assertThrows(CuentaNotFoundException.class, () -> cuentaService.find(124L));
    }

    @Test
    public void DarAltaCuentaCCYDOLARES_TipoCuentaNoSoportada() {
        CuentaDto dto = new CuentaDto();
        dto.setTipoCuenta("CC");
        dto.setMoneda("D");

        assertThrows(TipoCuentaNoSoportadaException.class, () -> cuentaService.darDeAltaCuenta(dto));

    }

    @Test
    public void findAll() throws CuentaNotFoundException {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(1000.0);
        cuentaDao.save(cuenta);
        assertEquals(0, cuentaService.obtenerTodasLasCuentas().size());
    }

    @Test
    public void findALL_fail() throws CuentaNotFoundException {
        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(null); 

        assertThrows(CuentaNotFoundException.class, () -> {cuentaService.obtenerTodasLasCuentas();});
    }


    @Test
    public void testTieneFondosSuficientes_CuentaNoEncontrada_DeberiaLanzarCuentaNotFoundException() {
        assertThrows(CuentaNotFoundException.class, () -> {
            cuentaService.tieneFondosSuficientes(1L, 500.0);
        });
    }



}    
