package ar.edu.utn.frbb.tup.controller.dto;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;


public class MovimientoDto {
    private Double monto;
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private TipoMoneda tipoMoneda;
    private TipoMovimiento tipoMovimiento;

    public MovimientoDto(Cuenta cuentaDestino, Cuenta cuentaOrigen, Double monto, TipoMoneda tipoMoneda, TipoMovimiento tipoMovimiento) {
        this.cuentaDestino = cuentaDestino;
        this.cuentaOrigen = cuentaOrigen;
        this.monto = monto;
        this.tipoMoneda = tipoMoneda;
        this.tipoMovimiento = tipoMovimiento;
    }

    public MovimientoDto(Transferencia transferencia, CuentaDao cuentaDao) {
        this.monto = transferencia.getMonto();
        this.cuentaOrigen = cuentaDao.find(transferencia.getCuentaOrigen()); 
        this.cuentaDestino = cuentaDao.find(transferencia.getCuentaDestino()); 
        this.tipoMoneda = TipoMoneda.fromString(transferencia.getMoneda()); 
    }

    public Double getMonto() {
        return monto;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
}