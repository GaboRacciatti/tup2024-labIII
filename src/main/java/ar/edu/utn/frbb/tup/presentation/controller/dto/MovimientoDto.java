package ar.edu.utn.frbb.tup.presentation.controller.dto;

import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;

public class MovimientoDto {
    private Double monto;
    private long cuentaOrigen;
    private long cuentaDestino;
    private String tipoMoneda;
    private TipoMovimiento tipoMovimiento;

    public MovimientoDto() {}

    public MovimientoDto(long cuentaDestino, long cuentaOrigen, Double monto, String tipoMoneda, TipoMovimiento tipoMovimiento) {
        this.cuentaDestino = cuentaDestino;
        this.cuentaOrigen = cuentaOrigen;
        this.monto = monto;
        this.tipoMoneda = tipoMoneda;
        this.tipoMovimiento = tipoMovimiento;
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

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public void setCuentaOrigen(long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public void setCuentaDestino(long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }

}
