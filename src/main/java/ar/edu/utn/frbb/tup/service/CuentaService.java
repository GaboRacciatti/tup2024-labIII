package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

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

    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
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

    public Cuenta find(long numeroCuenta) {
        return cuentaDao.find(numeroCuenta);
    }

    public Cuenta actualizarCuenta(long numeroCuenta, CuentaDto cuentaDto) throws CuentaNotFoundException, TipoCuentaNoSoportadaException {
        Cuenta cuenta = cuentaDao.find(numeroCuenta);
        if (cuenta == null) {
            throw new CuentaNotFoundException("La cuenta no existe.");
        }
    
        if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new TipoCuentaNoSoportadaException("El tipo de cuenta no está soportado.");
        }
        cuenta.setTipoCuenta(TipoCuenta.fromString(cuentaDto.getTipoCuenta()));
        cuenta.setMoneda(TipoMoneda.fromString(cuentaDto.getMoneda()));
        cuentaDao.update(cuenta);
    
        return cuenta;
    }

    
    public Cuenta update(Cuenta cuenta) {
        cuentaDao.update(cuenta);
        return cuenta;
    }

    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaDao.findAll();
    }
}
