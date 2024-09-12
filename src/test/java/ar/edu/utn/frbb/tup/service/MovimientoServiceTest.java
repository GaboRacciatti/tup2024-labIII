package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CuentaSinFondosException;
import ar.edu.utn.frbb.tup.model.exception.DiferenteMonedaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.presentation.controller.dto.MovimientosRetiroDepositoDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MovimientoServiceTest {
    @InjectMocks
    private MovimientosService movimientoService;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private BanelcoService banelcoService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }


    @Test
    public void testMovimientoDeposito() {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        movimiento.setMonto(1000.0);
        movimiento.setMoneda(TipoMoneda.PESOS);
        movimiento.setFecha(LocalDateTime.now().toLocalDate());

        assertEquals(TipoMovimiento.DEPOSITO, movimiento.getTipoMovimiento());
        assertEquals(1000.0, movimiento.getMonto());
        assertEquals(TipoMoneda.PESOS, movimiento.getMoneda());
        assertEquals(LocalDateTime.now().toLocalDate(), movimiento.getFecha());
    }

    @Test
    public void testMovimientoRetiro() {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);
        movimiento.setMonto(500.0);
        movimiento.setMoneda(TipoMoneda.DOLARES);

        assertEquals(TipoMovimiento.RETIRO, movimiento.getTipoMovimiento());
        assertEquals(500.0, movimiento.getMonto());
        assertEquals(TipoMoneda.DOLARES, movimiento.getMoneda());
    }

    @Test
    public void testMovimientoTransferencia() {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA);
        movimiento.setMonto(750.0);
        movimiento.setMoneda(TipoMoneda.PESOS);
        movimiento.setCuentaOrigen(123456);
        movimiento.setCuentaDestino(987654);
        assertEquals(TipoMovimiento.TRANSFERENCIA, movimiento.getTipoMovimiento());
        assertEquals(750.0, movimiento.getMonto());
        assertEquals(TipoMoneda.PESOS, movimiento.getMoneda());
        assertEquals(123456, movimiento.getCuentaOrigen());
        assertEquals(987654, movimiento.getCuentaDestino());
    }

    @Test
    public void testRetirarCuentaSinFondos() {
        MovimientosRetiroDepositoDto movimiento = new MovimientosRetiroDepositoDto();
        movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);
        movimiento.setMonto(1000.0);
        movimiento.setTipoMoneda("P");
        movimiento.setNumeroCuenta(123L);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setBalance(500);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setDniTitular(1234567);


        when(cuentaDao.find(123L)).thenReturn(cuenta);

        assertThrows(CuentaSinFondosException.class, () -> {
            movimientoService.retirar(movimiento);
        });

    }

    @Test
    public void testRetirarCuentaDistintaMoneda() {
        MovimientosRetiroDepositoDto movimiento = new MovimientosRetiroDepositoDto();
        movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);
        movimiento.setMonto(1000.0);
        movimiento.setTipoMoneda("D");
        movimiento.setNumeroCuenta(123L);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        cuenta.setBalance(500);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setDniTitular(1234567);


        when(cuentaDao.find(123L)).thenReturn(cuenta);

        assertThrows(DiferenteMonedaException.class, () -> {
            movimientoService.retirar(movimiento);
        });

    }
    @Test
    public void testRetirarCuentaNotFound() {
        MovimientosRetiroDepositoDto movimiento = new MovimientosRetiroDepositoDto();
        movimiento.setTipoMovimiento(TipoMovimiento.RETIRO);
        movimiento.setMonto(1000.0);
        movimiento.setTipoMoneda("D");
        movimiento.setNumeroCuenta(123);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setBalance(500);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setDniTitular(1234567);


        when(cuentaDao.find(12345L)).thenReturn(cuenta);

        assertThrows(CuentaNotFoundException.class, () -> {
            movimientoService.retirar(movimiento);
        });

    }
    @Test
    public void testDepositarCuentaNoEncontrada() {
        MovimientosRetiroDepositoDto movimiento = new MovimientosRetiroDepositoDto();
        movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        movimiento.setMonto(1000.0);
        movimiento.setTipoMoneda("D");
        movimiento.setNumeroCuenta(123);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setBalance(500);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setDniTitular(1234567);


        when(cuentaDao.find(12345L)).thenReturn(cuenta);

        assertThrows(CuentaNotFoundException.class, () -> {
            movimientoService.retirar(movimiento);
        });
    }

    @Test
    public void testDepositarMonedaDiferente() throws CuentaNotFoundException, DiferenteMonedaException {
        MovimientosRetiroDepositoDto movimiento = new MovimientosRetiroDepositoDto();
        movimiento.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        movimiento.setMonto(1000.0);
        movimiento.setTipoMoneda("D");
        movimiento.setNumeroCuenta(12345L);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setBalance(500);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setDniTitular(1234567);


        when(cuentaDao.find(12345L)).thenReturn(cuenta);

        assertThrows(DiferenteMonedaException.class, () -> {
            movimientoService.retirar(movimiento);
        });
    }

    @Test
    public void testDepositoMontoNegativo(){
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setBalance(500);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setDniTitular(1234567);

        when(cuentaDao.find(12345L)).thenReturn(cuenta);

        MovimientosRetiroDepositoDto deposito = new MovimientosRetiroDepositoDto();
        deposito.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        deposito.setMonto(-1000.0);
        deposito.setTipoMoneda("D");
        deposito.setNumeroCuenta(12345L);
        assertThrows(IllegalArgumentException.class, () -> {
            movimientoService.depositar(deposito);
        });
    }
    @Test
    public void testTransferirCuentaNoEncontrada() {
        MovimientoDto transferencia = new MovimientoDto();
        transferencia.setMonto(1000.0);
        transferencia.setTipoMoneda("D");
        transferencia.setCuentaOrigen(12345L);
        transferencia.setCuentaDestino(123L);
        assertThrows(CuentaNotFoundException.class, () -> {
            movimientoService.transferir(transferencia);
        });
    }
    @Test
    public void testTransferirCuentaDistintaMoneda() {
        MovimientoDto transferencia = new MovimientoDto();
        transferencia.setMonto(1000.0);
        transferencia.setTipoMoneda("D");
        transferencia.setCuentaOrigen(12345L);
        transferencia.setCuentaDestino(123L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(12345L);
        cuentaOrigen.setBalance(1500);
        cuentaOrigen.setMoneda(TipoMoneda.DOLARES);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(123L);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);
        when(cuentaDao.find(12345L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(123L)).thenReturn(cuentaDestino);

        assertThrows(DiferenteMonedaException.class, () -> {
            movimientoService.transferir(transferencia);
        });
    }
    @Test
    public void testTransferirCuentaSinFondos() {
        MovimientoDto transferencia = new MovimientoDto();
        transferencia.setMonto(1000.0);
        transferencia.setTipoMoneda("D");
        transferencia.setCuentaOrigen(12345L);
        transferencia.setCuentaDestino(123L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(12345L);
        cuentaOrigen.setBalance(500);
        cuentaOrigen.setMoneda(TipoMoneda.DOLARES);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(123L);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.DOLARES);
        when(cuentaDao.find(12345L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(123L)).thenReturn(cuentaDestino);

        assertThrows(CuentaSinFondosException.class, () -> {
            movimientoService.transferir(transferencia);
        });
    }


    @Test
    public void testTransferirCuentaOrigenNoEncontrada() {
        MovimientoDto transferencia = new MovimientoDto();
        transferencia.setMonto(1000.0);
        transferencia.setTipoMoneda("D");
        transferencia.setCuentaOrigen(12345L);
        transferencia.setCuentaDestino(123L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(12345L);
        cuentaOrigen.setBalance(500);
        cuentaOrigen.setMoneda(TipoMoneda.DOLARES);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(123L);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.DOLARES);

        when(cuentaDao.find(1234567L)).thenReturn(cuentaOrigen);

        assertThrows(CuentaNotFoundException.class, () -> {
            movimientoService.transferir(transferencia);
        });
    }

    @Test
    public void testTransferenciaEnPesosConComision() throws DiferenteMonedaException, CuentaNotFoundException, CuentaSinFondosException {
        MovimientoDto transferencia = new MovimientoDto();
        transferencia.setMonto(1250000.0);
        transferencia.setTipoMoneda("P");
        transferencia.setCuentaOrigen(12345L);
        transferencia.setCuentaDestino(123L);
    
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(12345L);
        cuentaOrigen.setBalance(1800000.0);
        cuentaOrigen.setMoneda(TipoMoneda.PESOS);
        cuentaOrigen.setMovimientos(new LinkedList<>()); // Inicializar el campo movimientos

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(123L);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.PESOS);
        cuentaDestino.setMovimientos(new LinkedList<>()); // Inicializar el campo movimientos

        when(cuentaDao.find(12345L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(123L)).thenReturn(cuentaDestino);
    
        Movimiento resultado = movimientoService.transferir(transferencia);
    
        assertEquals(1250000.0, resultado.getMonto());
        assertEquals(1800000.0 - (1250000.0 + 1250000.0*0.02), cuentaOrigen.getBalance()); // Verificar saldo de origen después de la transferencia y comisión
        assertEquals(500 + 1250000.0, cuentaDestino.getBalance()); // Verificar saldo de destino después de la transferencia
    }
    
    @Test
    public void testTransferenciaEnDolaresConComision() throws DiferenteMonedaException, CuentaNotFoundException, CuentaSinFondosException {
        MovimientoDto transferencia = new MovimientoDto();
        transferencia.setMonto(5100.0);
        transferencia.setTipoMoneda("D");
        transferencia.setCuentaOrigen(12345L);
        transferencia.setCuentaDestino(123L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(12345L);
        cuentaOrigen.setBalance(6000.0);
        cuentaOrigen.setMoneda(TipoMoneda.DOLARES);
        cuentaOrigen.setMovimientos(new LinkedList<>()); 

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(123L);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.DOLARES);
        cuentaDestino.setMovimientos(new LinkedList<>()); 

        when(cuentaDao.find(12345L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(123L)).thenReturn(cuentaDestino);

        Movimiento resultado = movimientoService.transferir(transferencia);

        assertEquals(5100.0, resultado.getMonto());
        assertEquals(6000.0 - (5100.0 + 5100.0*0.005), cuentaOrigen.getBalance()); 
        assertEquals(500 + 5100.0, cuentaDestino.getBalance()); 
    }
    @Test
    public void testRetirarSaldoSuficienteMonedaCompatible() throws Exception, CuentaSinFondosException {
        MovimientosRetiroDepositoDto retiroDto = new MovimientosRetiroDepositoDto();
        retiroDto.setNumeroCuenta(12345L);
        retiroDto.setMonto(1000.0);
        retiroDto.setTipoMoneda("D");
    
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setBalance(5000.0);
        cuenta.setMoneda(TipoMoneda.DOLARES);
        cuenta.setMovimientos(new LinkedList<>());
    
        when(cuentaDao.find(12345L)).thenReturn(cuenta);
    
        Movimiento resultado = movimientoService.retirar(retiroDto);
        resultado.setTipoMovimiento(TipoMovimiento.RETIRO);
    
        assertNotNull(resultado);
        assertEquals(1000.0, resultado.getMonto());
        assertEquals(TipoMovimiento.RETIRO, resultado.getTipoMovimiento());
        verify(cuentaDao).update(cuenta);
    }
    

    @Test
    public void testTransferirCuentaDestinoNoExistente_TransferenciaExternaExitosa() throws Exception, CuentaSinFondosException {
        MovimientoDto transferenciaDto = new MovimientoDto();
        transferenciaDto.setMonto(1000.0);
        transferenciaDto.setTipoMoneda("D");
        transferenciaDto.setCuentaOrigen(12345L);
        transferenciaDto.setCuentaDestino(67890L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(12345L);
        cuentaOrigen.setBalance(5000.0);
        cuentaOrigen.setMoneda(TipoMoneda.DOLARES);
        cuentaOrigen.setMovimientos(new LinkedList<>());


        when(cuentaDao.find(12345L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(67890L)).thenReturn(null); 

        when(banelcoService.realizarTransferenciaBanelco(
                transferenciaDto.getCuentaOrigen(),
                transferenciaDto.getCuentaDestino(),
                transferenciaDto.getMonto()
        )).thenReturn(true);

        Movimiento resultado = movimientoService.transferir(transferenciaDto);
        resultado.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA);

        assertNotNull(resultado);
        assertEquals(1000.0, resultado.getMonto());
        assertEquals(TipoMovimiento.TRANSFERENCIA, resultado.getTipoMovimiento());
        verify(banelcoService).realizarTransferenciaBanelco(
                transferenciaDto.getCuentaOrigen(),
                transferenciaDto.getCuentaDestino(),
                transferenciaDto.getMonto()
        );
        verify(cuentaDao).update(cuentaOrigen);
    }

    @Test
    public void testDepositarCuentaExistenteConMontoPositivo() throws CuentaNotFoundException, DiferenteMonedaException {
        MovimientosRetiroDepositoDto deposito = new MovimientosRetiroDepositoDto();
        deposito.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        deposito.setMonto(500.0);
        deposito.setTipoMoneda("P");
        deposito.setNumeroCuenta(12345L);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setBalance(1000.0);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.find(12345L)).thenReturn(cuenta);

        Movimiento resultado = movimientoService.depositar(deposito);

        assertNotNull(resultado);
        assertEquals(1500.0, cuenta.getBalance());
        assertEquals(TipoMovimiento.DEPOSITO, resultado.getTipoMovimiento());
    }

    @Test
    public void testRetirarCuentaExistenteSaldoSuficienteMonedaCorrecta() throws Exception, CuentaSinFondosException, DiferenteMonedaException {
        MovimientosRetiroDepositoDto retiroDto = new MovimientosRetiroDepositoDto();
        retiroDto.setNumeroCuenta(12345L);
        retiroDto.setMonto(1000.0);
        retiroDto.setTipoMoneda("D");

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setBalance(5000.0);
        cuenta.setMoneda(TipoMoneda.DOLARES);

        when(cuentaDao.find(12345L)).thenReturn(cuenta);

        Movimiento resultado = movimientoService.retirar(retiroDto);
        resultado.setTipoMovimiento(TipoMovimiento.RETIRO);

        assertNotNull(resultado);
        assertEquals(1000.0, resultado.getMonto());
        assertEquals(TipoMovimiento.RETIRO, resultado.getTipoMovimiento());
        assertEquals(4000.0, cuenta.getBalance());
    }

    @Test
    public void testTransferirConSaldoSuficienteMonedaCompatible() throws Exception, CuentaSinFondosException, DiferenteMonedaException {
        MovimientoDto transferencia = new MovimientoDto();
        transferencia.setMonto(1000.0);
        transferencia.setTipoMoneda("D");
        transferencia.setCuentaOrigen(12345L);
        transferencia.setCuentaDestino(67890L);

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(12345L);
        cuentaOrigen.setBalance(5000.0);
        cuentaOrigen.setMoneda(TipoMoneda.DOLARES);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(67890L);
        cuentaDestino.setBalance(3000.0);
        cuentaDestino.setMoneda(TipoMoneda.DOLARES);

        when(cuentaDao.find(12345L)).thenReturn(cuentaOrigen);
        when(cuentaDao.find(67890L)).thenReturn(cuentaDestino);

        Movimiento resultado = movimientoService.transferir(transferencia);

        assertNotNull(resultado);
        assertEquals(1000.0, resultado.getMonto());
        assertEquals(4000.0, cuentaOrigen.getBalance());
        assertEquals(4000.0, cuentaDestino.getBalance());
    }




}