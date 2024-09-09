package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosRetiroDepositoDto;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import ar.edu.utn.frbb.tup.controller.validator.MovimientosValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Views;
import ar.edu.utn.frbb.tup.model.exception.*;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

@RestController
@RequestMapping("/api")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    CuentaService cuentaService;

    @Autowired
    private MovimientosValidator movimientoValidator;

    @JsonView(Views.Public.class)    
    @PostMapping("/deposito")
    public Movimiento deposito(@RequestBody MovimientosRetiroDepositoDto movimientosSimplesDto) 
            throws CuentaNotFoundException, DiferenteMonedaException, CantidadNegativaException {
        
        movimientoValidator.validateSimples(movimientosSimplesDto);
        movimientoService.realizarDeposito(movimientosSimplesDto);
        Cuenta cuenta = cuentaService.find(movimientosSimplesDto.getNumeroCuenta());
        return cuenta.getMovimientos().getLast(); 
    }

    @JsonView(Views.Public.class)
    @PostMapping("/retiro")
    public Movimiento retiro(@RequestBody MovimientosRetiroDepositoDto movimientosSimplesDto)
            throws CuentaNotFoundException, CuentaSinFondosException, CantidadNegativaException, NoAlcanzaException, DiferenteMonedaException {
        
        movimientoValidator.validateSimples(movimientosSimplesDto);
        movimientoService.realizarRetiro(movimientosSimplesDto);
        Cuenta cuenta = cuentaService.find(movimientosSimplesDto.getNumeroCuenta());
        return cuenta.getMovimientos().getLast();
    }

    @JsonView(Views.Public.class)
    @PostMapping("/transferencia")
    public Movimiento transferencia(@RequestBody MovimientoDto movimientosDto) 
            throws CuentaNotFoundException, CuentaSinFondosException, DiferenteMonedaException, CantidadNegativaException {
        
        movimientoValidator.validate(movimientosDto);
        movimientoService.transferir(movimientosDto);
        Cuenta cuenta = cuentaService.find(movimientosDto.getCuentaOrigen());
        return cuenta.getMovimientos().getLast();
    }

    @JsonView(Views.Public.class)
    @GetMapping("/{numeroCuenta}")
    public LinkedList<Movimiento> obtenerMovimientos(@PathVariable long numeroCuenta) 
            throws CuentaNotFoundException {
        
        Cuenta cuenta = cuentaService.find(numeroCuenta);
        return cuenta.getMovimientos();
    }
}
