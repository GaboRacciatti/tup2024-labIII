package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoBanco;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;

import java.time.LocalDate;
import java.util.LinkedList;

public class CuentaEntity extends BaseEntity {
    long id;
    String nombre;    
    LocalDate fechaCreacion;
    double balance;
    String tipoCuenta;
    Long titular;
    long numeroCuenta;
    private TipoMoneda moneda;
    private TipoBanco tipoBanco;
    LinkedList<Movimiento> movimientos;

    public CuentaEntity(Cuenta cuenta) {
        super(cuenta.getNumeroCuenta());
        this.numeroCuenta = cuenta.getNumeroCuenta(); 
        this.balance = cuenta.getBalance();
        this.tipoCuenta = cuenta.getTipoCuenta().toString();
        this.tipoBanco = cuenta.getTipoBanco();
        this.titular = cuenta.getDniTitular();
        this.fechaCreacion = cuenta.getFechaCreacion();
        this.movimientos = cuenta.getMovimientos();
        this.moneda = cuenta.getMoneda(); 
    }


    public void updateFromCuenta(Cuenta cuenta) {
        this.balance = cuenta.getBalance();
        this.moneda = cuenta.getMoneda();
        this.titular = cuenta.getDniTitular();
        this.movimientos = cuenta.getMovimientos();
    }




    public Cuenta toCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(this.numeroCuenta); 
        cuenta.setBalance(this.balance);
        cuenta.setTipoCuenta(TipoCuenta.valueOf(this.tipoCuenta));
        cuenta.setFechaCreacion(this.fechaCreacion);
        cuenta.setDniTitular(this.titular); 
        cuenta.setTipoBanco(this.tipoBanco);
        cuenta.setMoneda(this.moneda);
        cuenta.setMovimientos(this.movimientos);
        return cuenta;
    }

    @Override
    public String toString() {
        return "CuentaEntity{" +
                "balance=" + balance +
                ", dniTitular=" + titular +
                ", fechaCreacion=" + fechaCreacion +
                ", movimientos=" + movimientos +
                ", nombre='" + nombre + '\'' +
                ", numeroCuenta=" + numeroCuenta +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", tipoMoneda='" + moneda + '\'' +
                '}';
    }

    public Long getTitular() {
        return titular;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    public long getNumeroCuenta() {
        return numeroCuenta;
    }
}
