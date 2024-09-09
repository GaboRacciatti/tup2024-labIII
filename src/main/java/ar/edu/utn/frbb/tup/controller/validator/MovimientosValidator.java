package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosRetiroDepositoDto;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.DiferenteMonedaException;
import org.springframework.stereotype.Component;

@Component
public class MovimientosValidator {

    public void validateSimples(MovimientosRetiroDepositoDto movimientosRetiroDepositoDto)
            throws CantidadNegativaException, DiferenteMonedaException {

        if (movimientosRetiroDepositoDto.getMonto() == null || movimientosRetiroDepositoDto.getMonto() <= 0) {
            throw new CantidadNegativaException("El monto no puede ser negativo o nulo.");
        }

        if (movimientosRetiroDepositoDto.getTipoMoneda() == null) {
            throw new DiferenteMonedaException("El tipo de moneda no puede ser nulo.");
        }
    }

    public void validate(MovimientoDto movimientoDto)
            throws CantidadNegativaException, DiferenteMonedaException {

        if (movimientoDto.getMonto() == null || movimientoDto.getMonto() <= 0) {
            throw new CantidadNegativaException("El monto no puede ser negativo o nulo.");
        }

        if (movimientoDto.getTipoMoneda() == null) {
            throw new DiferenteMonedaException("El tipo de moneda no puede ser nulo.");
        }
    }
}
