package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.controller.dto.DepositoResponseDto;
import ar.edu.utn.frbb.tup.controller.dto.RetiroDto;
import ar.edu.utn.frbb.tup.controller.dto.RetiroResponseDto;
import ar.edu.utn.frbb.tup.controller.dto.TransaccionDto;
import ar.edu.utn.frbb.tup.controller.dto.TransaccionesResponseDto;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Autowired
    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }
    @PostMapping("/transfer")
    public ResponseEntity<TransferenciaResponseDto> realizarTransferencia(@RequestBody TransferenciaDto transferenciaDto) throws CantidadNegativaException, NoAlcanzaException {
        TransferenciaResponseDto response = movimientoService.realizarTransferencia(transferenciaDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposito")
    public ResponseEntity<DepositoResponseDto> realizarDeposito(@RequestBody DepositoDto depositoDto) throws CantidadNegativaException {
        DepositoResponseDto response = movimientoService.realizarDeposito(depositoDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/retiro")
    public ResponseEntity<RetiroResponseDto> realizarRetiro(@RequestBody RetiroDto retiroDto) throws CantidadNegativaException, NoAlcanzaException {
        RetiroResponseDto response = movimientoService.realizarRetiro(retiroDto);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{numeroCuenta}/movimientos")
    public ResponseEntity<TransaccionesResponseDto> obtenerMovimientos(@PathVariable long numeroCuenta) {
    List<TransaccionDto> transacciones = movimientoService.obtenerHistorialTransacciones(numeroCuenta);
    TransaccionesResponseDto response = new TransaccionesResponseDto(numeroCuenta, transacciones);
    return ResponseEntity.ok(response);
    }
}
