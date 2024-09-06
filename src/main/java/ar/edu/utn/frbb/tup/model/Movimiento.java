package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.time.LocalTime;

import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;

public class Movimiento {
    private static long contadorid = 1;
    private long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String descripcion;
    private TipoMovimiento tipo;
    private double monto;
    private Cuenta cuentaOrigen; 
    private Cuenta cuentaDestino; 
    private TipoMoneda moneda;
    public Movimiento() {}

    public Movimiento(long id, LocalDate fecha, LocalTime hora, String descripcion, TipoMovimiento tipo, double monto, TipoMoneda moneda) {
        this.id = contadorid++;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.monto = monto;
        this.moneda = moneda;
    }

    public Movimiento(long id, LocalDate fecha, LocalTime hora, String descripcion, TipoMovimiento tipo, double monto, Cuenta cuentaOrigen, Cuenta cuentaDestino, TipoMoneda moneda) {
        this.id = contadorid++;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.moneda = moneda;
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

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipo;
    }

    public void setTipoMovimiento(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaHora() {
        return fecha;
    }

    public void setFechaHora(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
