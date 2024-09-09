package ar.edu.utn.frbb.tup.controller.dto;

import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;

public class MovimientoDto {
    private Double monto;
    private long cuentaOrigen;
    private long cuentaDestino;
    private TipoMoneda tipoMoneda;

    public MovimientoDto(long cuentaDestino, long cuentaOrigen, Double monto, TipoMoneda tipoMoneda) {
        this.cuentaDestino = cuentaDestino;
        this.cuentaOrigen = cuentaOrigen;
        this.monto = monto;
        this.tipoMoneda = tipoMoneda;
    }


    public Double getMonto() {
        return monto;
    }

    public long getCuentaDestino() {
        return cuentaDestino;
    }

    public long getCuentaOrigen() {
        return cuentaOrigen;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setCuentaOrigen(long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public void setCuentaDestino(long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

}