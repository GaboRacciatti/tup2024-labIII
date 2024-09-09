package ar.edu.utn.frbb.tup.controller.dto;

import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;

public class MovimientosRetiroDepositoDto { 
    private Double monto;
    private TipoMoneda tipoMoneda;
    private long numeroCuenta;

    public MovimientosRetiroDepositoDto(Double monto, long numeroCuenta, TipoMoneda tipoMoneda) {
        this.monto = monto;
        this.numeroCuenta = numeroCuenta;
        this.tipoMoneda = tipoMoneda;
    }

    public Double getMonto() {
        return monto;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }
}