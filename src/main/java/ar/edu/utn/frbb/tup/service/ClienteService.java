package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    ClienteDao clienteDao;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente(clienteDto);

        if (clienteDao.find(cliente.getDni(), false) != null) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }

        if (cliente.getEdad() < 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor a 18 años");
        }

        clienteDao.save(cliente);
        return cliente;
    }

    public void agregarCuenta(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistsException {
        Cliente titular = buscarClientePorDni(dniTitular);
        cuenta.setDniTitular(titular.getDni());
        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda");
        }
        titular.addCuenta(cuenta);
        clienteDao.save(titular);
    }
    public Cliente buscarClientePorDni(long dni) {
        Cliente cliente = clienteDao.find(dni, true);
        if(cliente == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        return cliente;
    }

    public Cliente eliminarClientePorDni(long dni) throws ClienteNotFoundException {
        Cliente cliente = clienteDao.find(dni, true);
        if (cliente == null) {
            throw new ClienteNotFoundException("El cliente buscado no existe");
        }
        clienteDao.delete(cliente);
        return cliente;
    }

    public Cliente editarClientPorDni(long dni, ClienteDto clienteDto) throws ClienteNotFoundException {
        Cliente cliente = clienteDao.find(dni, true);
        if (cliente == null) {
            throw new ClienteNotFoundException("El cliente buscado no existe");
        }

        if (clienteDto.getDireccion() != null && !clienteDto.getDireccion().isEmpty()) {
            cliente.setDireccion(clienteDto.getDireccion());
        }

        if (clienteDto.getTelefono() != null && !clienteDto.getTelefono().isEmpty()) {
            cliente.setTelefono(clienteDto.getTelefono());
        }

        return cliente;
    }

    public List<Cliente> obtenerTodosLosClientes() {
        if (clienteDao == null) {
            throw new IllegalStateException("No hay clientes en la base de datos");
        }
        return clienteDao.findAll();
    }
}

