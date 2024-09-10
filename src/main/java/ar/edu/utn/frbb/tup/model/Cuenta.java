package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Objects;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.enums.TipoBanco;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;

public class Cuenta {
    private static long contadorCuentas = 1;
    private long numeroCuenta;
    LocalDate fechaCreacion;
    public TipoBanco tipoBanco;
    double balance;
    TipoCuenta tipoCuenta;
    private TipoMoneda moneda;
    private long dniTitular;
    LinkedList<Movimiento> movimientos;
    public Cuenta() {}

    public Cuenta(CuentaDto cuentaDto) {
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
        this.fechaCreacion = LocalDate.now();
        this.balance = cuentaDto.getBalance();
        this.numeroCuenta = contadorCuentas++;
        this.tipoBanco = cuentaDto.getTipoBanco();
        this.movimientos = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "cliente=" + dniTitular +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", balance=" + balance +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(moneda);
    }

    public static long getContadorCuentas() {
        return contadorCuentas;
    }

    public static void setContadorCuentas(long contadorCuentas) {
        Cuenta.contadorCuentas = contadorCuentas;
    }

    public TipoBanco getTipoBanco() {
        return tipoBanco;
    }

    public void setTipoBanco(TipoBanco tipoBanco) {
        this.tipoBanco = tipoBanco;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Cuenta setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public LinkedList<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(LinkedList<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public void agregarMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento);
    }


    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void acreditarEnCuenta(double monto) throws CantidadNegativaException {
        if (monto < 0) {
            throw new CantidadNegativaException("No se puede acreditar una cantidad negativa");
        }
        this.balance += monto;
    }
    
    public void forzarDebitoDeCuenta(double monto) throws CantidadNegativaException {
        if (monto < 0) {
            throw new CantidadNegativaException("No se puede debitar una cantidad negativa");
        }
        if (this.balance < monto) {
            throw new CantidadNegativaException("Saldo insuficiente para el débito");
        }
        this.balance -= monto;
    }
    
    

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public long getDniTitular() {
        return dniTitular;
    }

    public void setDniTitular(long dniTitular) {
        this.dniTitular = dniTitular;
    }
}
