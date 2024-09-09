package ar.edu.utn.frbb.tup.controller.dto;

import java.util.LinkedHashSet;

import ar.edu.utn.frbb.tup.model.enums.TipoBanco;

public class CuentaDto {

    private String tipoCuenta;
    private long dniTitular;
    private String moneda;
    private long NumeroCuenta;
    private double balance;
    private String cbu;
    private TipoBanco tipoBanco;
    private LinkedHashSet<MovimientoDto> movimientos;
    
    public long getDniTitular() {
        return dniTitular;
    }

    public String getMoneda() {
        return moneda;
    }

    public long getNumeroCuenta() {
        return NumeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        numeroCuenta = NumeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }
    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }
    public void setDniTitular(long dniTitular) {
        this.dniTitular = dniTitular;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public TipoBanco getTipoBanco() {
        return tipoBanco;
    }

    public void setTipoBanco(TipoBanco tipoBanco) {
        this.tipoBanco = tipoBanco;
    }

    public LinkedHashSet<MovimientoDto> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(LinkedHashSet<MovimientoDto> movimientos) {
        this.movimientos = movimientos;
    }

}
