package ar.edu.utn.frbb.tup.controller.dto;

import java.util.List;

public class TransaccionesResponseDto {
    private long numeroCuenta;
    private List<TransaccionDto> transacciones;

    public TransaccionesResponseDto(long numeroCuenta, List<TransaccionDto> transacciones) {
        this.numeroCuenta = numeroCuenta;
        this.transacciones = transacciones;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public List<TransaccionDto> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<TransaccionDto> transacciones) {
        this.transacciones = transacciones;
    }
}
