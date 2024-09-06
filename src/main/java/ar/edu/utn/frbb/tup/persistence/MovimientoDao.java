package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MovimientoDao {
    private List<Movimiento> movimientos = new ArrayList<>();

    public void addMovimiento(Movimiento movimiento) {
        movimientos.add(movimiento);
    }

    public List<Movimiento> findByNumeroCuenta(long numeroCuenta) {
        return movimientos.stream()
            .filter(movimiento -> {
                if (movimiento.getTipoMovimiento() == TipoMovimiento.TRANSFERENCIA_ENTRANTE || 
                    movimiento.getTipoMovimiento() == TipoMovimiento.TRANSFERENCIA_SALIENTE) {
                    return movimiento.getCuentaOrigen().getNumeroCuenta() == numeroCuenta || 
                           movimiento.getCuentaDestino().getNumeroCuenta() == numeroCuenta;
                } else {
                    return movimiento.getCuentaOrigen().getNumeroCuenta() == numeroCuenta;
                }
            })
            .collect(Collectors.toList());
    }
}
