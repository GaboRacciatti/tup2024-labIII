package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;

import java.time.LocalDate;
import java.time.LocalTime;

public class MovimientoEntity extends BaseEntity {
    private LocalDate fecha;
    private LocalTime hora;
    private String descripcion;
    private TipoMovimiento tipo;
    private double monto;
    private long cuentaOrigenId;
    private long cuentaDestinoId;
    private TipoMoneda moneda;

    public MovimientoEntity(Movimiento movimiento) {
        super(movimiento.getId());
        this.fecha = movimiento.getFecha();
        this.hora = movimiento.getHora();
        this.descripcion = movimiento.getDescripcion();
        this.tipo = movimiento.getTipo();
        this.monto = movimiento.getMonto();
        this.cuentaOrigenId = movimiento.getCuentaOrigen().getNumeroCuenta();
        this.cuentaDestinoId = movimiento.getCuentaDestino() != null ? movimiento.getCuentaDestino().getNumeroCuenta() : 0;
        this.moneda = movimiento.getMoneda();
    }

    public Movimiento toMovimiento() {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(this.getId());
        movimiento.setFecha(this.fecha);
        movimiento.setHora(this.hora);
        movimiento.setDescripcion(this.descripcion);
        movimiento.setTipo(this.tipo);
        movimiento.setMonto(this.monto);
        movimiento.setMoneda(this.moneda);
        // Note: You'll need to set cuentaOrigen and cuentaDestino separately using CuentaDao
        return movimiento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public long getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(long cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public long getCuentaDestinoId() {
        return cuentaDestinoId;
    }

    public void setCuentaDestinoId(long cuentaDestinoId) {
        this.cuentaDestinoId = cuentaDestinoId;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

}