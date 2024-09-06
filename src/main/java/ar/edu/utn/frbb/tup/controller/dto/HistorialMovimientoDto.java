package ar.edu.utn.frbb.tup.controller.dto;

import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;

import java.time.LocalDate;

public class HistorialMovimientoDto {
    private LocalDate fechaOperacion;
    private TipoMovimiento tipoMovimiento;
    private Double monto;

    public HistorialMovimientoDto(LocalDate fechaOperacion, TipoMovimiento tipoMovimiento, Double monto) {
        this.fechaOperacion = fechaOperacion;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
    }

    // Getters y Setters
    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(LocalDate fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
