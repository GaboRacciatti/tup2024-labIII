package ar.edu.utn.frbb.tup.controller.dto;

public class RetiroResponseDto {
    private String mensaje;
    private long numeroCuenta;
    private double monto;

    public RetiroResponseDto(String mensaje, long numeroCuenta, double monto) {
        this.mensaje = mensaje;
        this.numeroCuenta = numeroCuenta;
        this.monto = monto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public double getMonto() {
        return monto;
    }
}
