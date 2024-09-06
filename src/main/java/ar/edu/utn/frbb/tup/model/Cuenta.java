package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.enums.TipoBanco;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;
import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;

public class Cuenta {
    private static long contadorCuentas = 1;
    private long numeroCuenta;
    LocalDate fechaCreacion;
    public TipoBanco tipoBanco;
    double balance;
    TipoCuenta tipoCuenta;
    TipoMoneda moneda;
    private long dniTitular;
    private String cbu;
    private List<Movimiento> movimientos; 


    public Cuenta() {
        this.numeroCuenta = contadorCuentas++;
        this.balance = 0;
        this.fechaCreacion = LocalDate.now();
        this.movimientos = new ArrayList<>(); 

    }

    public Cuenta(CuentaDto cuentaDto) {
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
        this.fechaCreacion = LocalDate.now();
        this.balance = cuentaDto.getBalance();
        this.numeroCuenta = contadorCuentas++;
        this.tipoBanco = (cuentaDto.getTipoBanco()); 
        this.cbu = cuentaDto.getCbu() != null ? cuentaDto.getCbu() : generarCbu();
        this.movimientos = new ArrayList<>(); 

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

    public Cuenta setBalance(double balance2) {
        this.balance = balance2;
        return this;
    }
    public List<Movimiento> getMovimientos() {
        return movimientos; 
    }

    public void agregarMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento); 
    }

    public void debitarDeCuenta(double cantidadADebitar) throws NoAlcanzaException, CantidadNegativaException {
        if (cantidadADebitar <= 0) {
            throw new CantidadNegativaException("La cantidad a debitar no puede ser negativa o 0");
        }
    
        if (balance < cantidadADebitar) {
            throw new NoAlcanzaException("El saldo de la cuenta no alcanza para realizar la operación");
        }
    
        this.balance -= cantidadADebitar;
    
        Movimiento movimiento = new Movimiento(
                1, 
                LocalDate.now(), 
                LocalTime.now(), 
                "Retiro realizado", 
                TipoMovimiento.RETIRO, 
                cantidadADebitar,
                this.moneda
        );
    
        agregarMovimiento(movimiento);
    }

    public void acreditarEnCuenta(double monto) throws CantidadNegativaException {
        if (monto < 0) {
            throw new CantidadNegativaException("No se puede acreditar una cantidad negativa");
        }
        this.balance += monto;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public void forzaDebitoDeCuenta(int i) {
        this.balance = this.balance - i;
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
    
    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }
    public void creditarEnCuenta(double cantidadACreditar, String descripcion) throws CantidadNegativaException {
        if (cantidadACreditar <= 0) {
            throw new CantidadNegativaException("La cantidad a acreditar no puede ser negativa o 0");
        }
    
        this.balance += cantidadACreditar;
    
        Movimiento movimiento = new Movimiento(
                1, 
                LocalDate.now(), 
                LocalTime.now(), 
                descripcion != null ? descripcion : "Depósito realizado", 
                TipoMovimiento.DEPOSITO, 
                cantidadACreditar,
                this.moneda
        );
    
        agregarMovimiento(movimiento);
    }
    public String generarCbu() { //genero cbu aleatorio
        StringBuilder cbu = new StringBuilder();
        for (int i = 0; i < 22; i++) {
            cbu.append((int) (Math.random() * 10)); // Genera un número aleatorio entre 0 y 9
        }
        return cbu.toString();
    }


}
