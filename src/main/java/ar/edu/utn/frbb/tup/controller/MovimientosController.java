package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosRetiroDepositoDto;
import ar.edu.utn.frbb.tup.controller.validator.MovimientosValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CuentaSinFondosException;
import ar.edu.utn.frbb.tup.model.exception.DatosMalIngresadosException;
import ar.edu.utn.frbb.tup.model.exception.DiferenteMonedaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.MovimientosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientosController {

    @Autowired
    private MovimientosService movimientosService;

    @Autowired
    private MovimientosValidator movimientosValidator;

    @Autowired
    private CuentaService cuentaService;

    @PostMapping("/transferencias")
    public ResponseEntity<MovimientoDto> transferir(@RequestBody MovimientoDto transferenciaDto) throws TipoCuentaNoSoportadaException, DatosMalIngresadosException, DiferenteMonedaException, CuentaNotFoundException, CuentaSinFondosException {
        movimientosValidator.validateMovimientosTransferencias(transferenciaDto);
        movimientosService.transferir(transferenciaDto);
        transferenciaDto.setTipoMovimiento(TipoMovimiento.TRANSFERENCIA);
        
        return ResponseEntity.ok(transferenciaDto);
    }


    @PostMapping("/deposito")
    public ResponseEntity<MovimientosRetiroDepositoDto> depositos(@RequestBody MovimientosRetiroDepositoDto movimientoDto) 
            throws TipoCuentaNoSoportadaException, CuentaNotFoundException, DatosMalIngresadosException, DiferenteMonedaException, CuentaSinFondosException {
    
        movimientosValidator.validateMovimientos(movimientoDto);
        movimientosService.depositar(movimientoDto);
        movimientoDto.setTipoMovimiento(TipoMovimiento.DEPOSITO);
        
        return ResponseEntity.ok(movimientoDto);
    }

    @PostMapping("/retiros")
    public ResponseEntity<MovimientosRetiroDepositoDto> retiros(@RequestBody MovimientosRetiroDepositoDto movimientoDto) 
            throws CuentaSinFondosException, CuentaNotFoundException, DiferenteMonedaException, DatosMalIngresadosException {
    
        movimientosValidator.validateMovimientos(movimientoDto);
        movimientosService.retirar(movimientoDto);
        movimientoDto.setTipoMovimiento(TipoMovimiento.RETIRO);
                    
        return ResponseEntity.ok(movimientoDto);
    }
    

    @GetMapping("/{numeroCuenta}")
    public List<Movimiento> getMovimientos(@PathVariable Long numeroCuenta) {
        Cuenta cuenta = cuentaService.find(numeroCuenta);
        return cuenta.getMovimientos();
    }

}