package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CuentaDao  extends AbstractBaseDao{
    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Cuenta find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(id)).toCuenta();
    }
    
    public void update(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public void delete(long id) {
        getInMemoryDatabase().remove(id);
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Object object:
                getInMemoryDatabase().values()) {
            CuentaEntity cuenta = ((CuentaEntity) object);
            if (cuenta.getTitular().equals(dni)) {
                cuentasDelCliente.add(cuenta.toCuenta());
            }
        }
        return cuentasDelCliente;
    }

        public List<Cuenta> findAll() {
            List<Cuenta> todasLasCuentas = new ArrayList<>();
            for (Object object : getInMemoryDatabase().values()) {
                CuentaEntity cuentaEntity = (CuentaEntity) object;
                todasLasCuentas.add(cuentaEntity.toCuenta());
            }
            return todasLasCuentas;
        }

        public Cuenta findByNumeroCuenta(long numeroCuenta) {
            for (Object object : getInMemoryDatabase().values()) {
                CuentaEntity cuentaEntity = (CuentaEntity) object;
                if (cuentaEntity.getNumeroCuenta() == numeroCuenta) {
                    return cuentaEntity.toCuenta();
                }
            }
            return null;
        }

}
