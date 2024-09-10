package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosRetiroDepositoDto;
import ar.edu.utn.frbb.tup.controller.validator.MovimientosValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CuentaSinFondosException;
import ar.edu.utn.frbb.tup.model.exception.DatosMalIngresadosException;
import ar.edu.utn.frbb.tup.model.exception.DiferenteMonedaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.MovimientosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

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
    public ResponseEntity<Movimiento> transferir(@RequestBody MovimientoDto transferenciaDto) {
        try {
            movimientosValidator.validateMovimientosTransferencias(transferenciaDto);
            movimientosService.transferir(transferenciaDto);

            Cuenta cuentaOrigen = cuentaService.find(transferenciaDto.getCuentaOrigen());
            if (cuentaOrigen == null || cuentaOrigen.getMovimientos() == null || cuentaOrigen.getMovimientos().isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            Movimiento mov = cuentaOrigen.getMovimientos().getLast();
            return ResponseEntity.ok(mov);
        } catch (CuentaSinFondosException | TipoCuentaNoSoportadaException | DatosMalIngresadosException | DiferenteMonedaException | CuentaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PostMapping("/deposito")
    public ResponseEntity<MovimientosRetiroDepositoDto> depositos(@RequestBody MovimientosRetiroDepositoDto movimientoDto) 
            throws TipoCuentaNoSoportadaException, CuentaNotFoundException, DatosMalIngresadosException, DiferenteMonedaException {
    
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
    public LinkedList<Movimiento> getMovimientos(@PathVariable Long numeroCuenta) {
        Cuenta cuenta = cuentaService.find(numeroCuenta);
        return cuenta.getMovimientos();
    }
}