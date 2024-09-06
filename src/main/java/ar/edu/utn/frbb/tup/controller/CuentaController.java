package ar.edu.utn.frbb.tup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.NoCuentasFoundException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.service.CuentaService;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService; 

    @Autowired
    private CuentaValidator cuentaValidator;

    @PostMapping
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody CuentaDto cuentaDto) throws TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        cuentaValidator.validate(cuentaDto);
        Cuenta cuenta = cuentaService.darDeAltaCuenta(cuentaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cuenta);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cuenta>> obtenerTodasLasCuentas() throws NoCuentasFoundException {
        List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
        if (cuentas.isEmpty()) {
            throw new NoCuentasFoundException("no hay cuentas disponibles");
        }
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<Cuenta> obtenerCuenta(@PathVariable long numeroCuenta) throws NoCuentasFoundException {
        Cuenta cuenta = cuentaService.find(numeroCuenta);
        if (cuenta == null) {
            throw new NoCuentasFoundException("no hay cuentas disponibles con ese numero de cuenta");
        }
        return ResponseEntity.ok(cuenta);
    }
}
