package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import ar.edu.utn.frbb.tup.controller.dto.MovimientoDto;
import ar.edu.utn.frbb.tup.controller.dto.MovimientosRetiroDepositoDto;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movimiento {

    LocalTime hora;
    String descripcion;
    TipoMovimiento tipo;
    TipoMoneda moneda;
    long cuentaDestino;
    long cuentaOrigen;    
    LocalDate fecha;
    Double monto;

    public Movimiento() {}

    public Movimiento(MovimientosRetiroDepositoDto movimientosSimplesDto){
        this.fecha = LocalDate.now();
        this.monto = movimientosSimplesDto.getMonto();
        this.moneda = TipoMoneda.fromString(movimientosSimplesDto.getTipoMoneda());
        this.tipo = movimientosSimplesDto.getTipoMovimiento();
    }

    public Movimiento(MovimientoDto movimientosDto) {
        this.cuentaDestino = movimientosDto.getCuentaDestino();
        this.cuentaOrigen = movimientosDto.getCuentaOrigen();
        this.fecha = LocalDate.now();
        this.monto = movimientosDto.getMonto();
        this.moneda = TipoMoneda.fromString(movimientosDto.getTipoMoneda());
        this.tipo = movimientosDto.getTipoMovimiento();
    }

    @Override
    public String toString() {
            return "\n Tipo de Movimiento: " + getTipoMovimiento() + "\n Monto: " + getMonto() + "\n Fecha: " + getFecha();
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(moneda);
    }
    public TipoMoneda getTipoMoneda() {
        return moneda;
    }

    public void setTipoMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
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

    public TipoMovimiento getTipoMovimiento() {
        return tipo;
    }

    public void setTipoMovimiento(TipoMovimiento tipo) {
        this.tipo = tipo;
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