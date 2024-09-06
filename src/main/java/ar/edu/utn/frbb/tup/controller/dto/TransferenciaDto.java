package ar.edu.utn.frbb.tup.controller.dto;

public class TransferenciaDto {
    private long cuentaOrigen;
    private long cuentaDestino;
    private double monto;
    private String moneda;

    public TransferenciaDto() {
        // Constructor vacío para deserialización
    }

    public TransferenciaDto(long cuentaOrigen, long cuentaDestino, double monto, String moneda) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.moneda = moneda;
    }

    public long getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public long getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
