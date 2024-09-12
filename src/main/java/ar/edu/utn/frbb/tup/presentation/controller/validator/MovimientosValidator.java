package ar.edu.utn.frbb.tup.presentation.controller.validator;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.DatosMalIngresadosException;
import ar.edu.utn.frbb.tup.model.exception.DiferenteMonedaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.presentation.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.presentation.controller.dto.MovimientosRetiroDepositoDto;

import org.springframework.stereotype.Component;

@Component
public class MovimientosValidator {

  public void validateMovimientos(MovimientosRetiroDepositoDto RetiroDepositoDto) throws DatosMalIngresadosException, DiferenteMonedaException {
    if (RetiroDepositoDto == null) {
      throw new DatosMalIngresadosException("El movimiento no puede ser nulo");
    }
    
    validateMonto(RetiroDepositoDto.getMonto());
    validateTipoMoneda(RetiroDepositoDto.getTipoMoneda());
  }

  public void validateMovimientosTransferencias(MovimientoDto movimientosTransferenciasDto)
          throws TipoCuentaNoSoportadaException, DatosMalIngresadosException, DiferenteMonedaException {
    if (movimientosTransferenciasDto == null) {
      throw new DatosMalIngresadosException("La transferencia no puede ser nula");
    }

    validateMonto(movimientosTransferenciasDto.getMonto());
    validateTipoMoneda(movimientosTransferenciasDto.getTipoMoneda());

    if (movimientosTransferenciasDto.getCuentaOrigen() == movimientosTransferenciasDto.getCuentaDestino()) {
      throw new TipoCuentaNoSoportadaException("La cuenta origen y destino no pueden ser la misma");
    }
  }

  private void validateMonto(Double monto) throws DatosMalIngresadosException {
    if (monto == null || monto <= 0.0) {
      throw new DatosMalIngresadosException("El monto debe ser mayor que cero");
    }
  }

  private void validateTipoMoneda(String tipoMoneda) throws DatosMalIngresadosException {
    try {
        TipoMoneda.fromString(tipoMoneda);
    } catch (IllegalArgumentException e) {
        throw new DatosMalIngresadosException("Tipo de moneda no válido: " + tipoMoneda);
    }
 }

 public void validarCuentaOrigenDestino(Movimiento movimiento) throws DatosMalIngresadosException {
    if (movimiento.getCuentaOrigen() == movimiento.getCuentaDestino()) {
        throw new DatosMalIngresadosException("La cuenta de origen y destino no pueden ser la misma");
    }
}
}