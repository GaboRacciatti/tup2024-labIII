package ar.edu.utn.frbb.tup.controller.dto;

import java.time.LocalDate;

import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;

public class DepositoDto {
    private long numeroCuenta;
    private TipoMoneda tipoMoneda;
    private double monto;
    private LocalDate fecha;

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
}
