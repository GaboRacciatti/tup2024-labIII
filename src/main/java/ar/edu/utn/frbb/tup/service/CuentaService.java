package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.ClienteNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.presentation.controller.dto.CuentaDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CuentaService {

    private final CuentaDao cuentaDao;
    private final ClienteService clienteService;


    @Autowired
    public CuentaService(CuentaDao cuentaDao, ClienteService clienteService) {
        this.cuentaDao = cuentaDao;
        this.clienteService = clienteService;
    }

    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException, ClienteNotFoundException {
        Cuenta cuenta = new Cuenta(cuentaDto);

        if(cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new TipoCuentaNoSoportadaException("El tipo de cuenta " + cuenta.getTipoCuenta() + " no está soportado.");
        }

        
        clienteService.agregarCuenta(cuenta, cuentaDto.getDniTitular());
        cuentaDao.save(cuenta);
        return cuenta;
    }

    public boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        return (cuenta.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && cuenta.getMoneda() == TipoMoneda.PESOS) ||
               (cuenta.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && 
               (cuenta.getMoneda() == TipoMoneda.PESOS || cuenta.getMoneda() == TipoMoneda.DOLARES));
    }

    public Cuenta find(long numeroCuenta) throws CuentaNotFoundException {
        if (cuentaDao.find(numeroCuenta) == null) {
            throw new CuentaNotFoundException("La cuenta con ID " + numeroCuenta + " no existe");
        }
        return cuentaDao.find(numeroCuenta);
    }


    public boolean tieneFondosSuficientes(Long numeroCuenta, Double monto) throws CuentaNotFoundException {
        Cuenta cuenta = cuentaDao.find(numeroCuenta);
        if (cuenta == null) {
            throw new CuentaNotFoundException("La cuenta con ID " + numeroCuenta + " no existe");
        }

        return cuenta.getBalance() >= monto;
    }
    
    public Cuenta update(Cuenta cuenta) throws CuentaNotFoundException {
        cuentaDao.update(cuenta);
        return cuenta;
    }

    public List<Cuenta> obtenerTodasLasCuentas() throws CuentaNotFoundException {
        if (cuentaDao.findAll() == null) {
            throw new CuentaNotFoundException("No existen cuentas");
        }
        return cuentaDao.findAll();
    }

}
