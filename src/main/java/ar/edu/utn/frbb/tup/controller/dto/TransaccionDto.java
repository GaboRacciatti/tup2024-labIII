package ar.edu.utn.frbb.tup.controller.dto;

import java.time.LocalDate;

public class TransaccionDto {
    private LocalDate fecha;
    private String tipo;
    private String descripcionBreve;
    private double monto;

    // Constructor
    public TransaccionDto(LocalDate fecha, String tipo, String descripcionBreve, double monto) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcionBreve = descripcionBreve;
        this.monto = monto;
    }

    // Getters y setters
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcionBreve() {
        return descripcionBreve;
    }

    public void setDescripcionBreve(String descripcionBreve) {
        this.descripcionBreve = descripcionBreve;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
