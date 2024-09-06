package ar.edu.utn.frbb.tup.controller.dto;
//DTO para realizar movimientos de deposito y retiro sin la necesidad de incluir en el JSON cuentaOrigen y cuentaDestino
public class MovimientosRetiroDepositoDto { 
    private Double monto;
    private String tipoMoneda;
    private long numeroCuenta;

    public MovimientosRetiroDepositoDto(Double monto, long numeroCuenta, String tipoMoneda) {
        this.monto = monto;
        this.numeroCuenta = numeroCuenta;
        this.tipoMoneda = tipoMoneda;
    }

    public Double getMonto() {
        return monto;
    }

    public String getTipoMoneda() {
        return tipoMoneda;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }
}