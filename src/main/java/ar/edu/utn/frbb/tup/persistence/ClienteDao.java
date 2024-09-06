package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDao extends AbstractBaseDao{

    @Autowired
    CuentaDao cuentaDao;

    public Cliente find(long dni, boolean loadComplete) {
        if (getInMemoryDatabase().get(dni) == null)
            return null;
        Cliente cliente =   ((ClienteEntity) getInMemoryDatabase().get(dni)).toCliente();
        if (loadComplete) {
            for (Cuenta cuenta :
                    cuentaDao.getCuentasByCliente(dni)) {
                cliente.addCuenta(cuenta);
            }
        }
        return cliente;

    }

    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public void delete(Cliente cliente) {
        find(cliente.getDni(), true);
        getInMemoryDatabase().remove(cliente.getDni());
    }

        public List<Cliente> getAll() {
        Map<Long, Object> database = getInMemoryDatabase();
        List<Cliente> clientes = new ArrayList<>();
        for (Object entity : database.values()) {
            clientes.add(((ClienteEntity) entity).toCliente());
        }
        return clientes;
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }

        public List<Cliente> findAll() {
            List<Cliente> todosLosClientes = new ArrayList<>();
            for (Object object : getInMemoryDatabase().values()) {
                ClienteEntity clienteEntity = (ClienteEntity) object;
                todosLosClientes.add(clienteEntity.toCliente());
            }
            return todosLosClientes;
        }
}
