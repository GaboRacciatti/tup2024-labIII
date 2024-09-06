package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.persistence.entity.MovimientoEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MovimientoDao extends AbstractBaseDao{
    @Override
    protected String getEntityName() {
        return "MOVIMIENTO";
    }
    
    private List<Movimiento> movimientos = new ArrayList<>();


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

    public void save(Movimiento movimiento) {
        MovimientoEntity entity = new MovimientoEntity(movimiento);
        getInMemoryDatabase().put(entity.getId(), entity);
    }
    public List<Movimiento> getMovimientosByCuenta(long numeroCuenta) {
        List<Movimiento> movimientosDeCuenta = new ArrayList<>();
        
        for (Object object : getInMemoryDatabase().values()) {
            MovimientoEntity movimiento = ((MovimientoEntity) object);
            Movimiento mov = movimiento.toMovimiento();
            if ((mov.getCuentaOrigen() != null && mov.getCuentaOrigen().getNumeroCuenta() == numeroCuenta) ||
                (mov.getCuentaDestino() != null && mov.getCuentaDestino().getNumeroCuenta() == numeroCuenta)) {
                movimientosDeCuenta.add(mov);
            }
        }
        
        return movimientosDeCuenta;
    }
}
