    package ar.edu.utn.frbb.tup.controller.dto;

    import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;

    public class MovimientosRetiroDepositoDto { 
        private Double monto;
        private String tipoMoneda;
        private long numeroCuenta;
        private TipoMovimiento tipoMovimiento;

        public MovimientosRetiroDepositoDto(Double monto, long numeroCuenta, String tipoMoneda, TipoMovimiento tipoMovimiento) {
            this.monto = monto;
            this.numeroCuenta = numeroCuenta;
            this.tipoMoneda = tipoMoneda;
            this.tipoMovimiento = tipoMovimiento;
        }

        public Double getMonto() {
            return monto;
        }

        public TipoMovimiento getTipoMovimiento() {
            return tipoMovimiento;
        }

        public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
            this.tipoMovimiento = tipoMovimiento;
        }

        public String getTipoMoneda() {
            return tipoMoneda;
        }

        public long getNumeroCuenta() {
            return numeroCuenta;
        }

        public void setMonto(Double monto) {
            this.monto = monto;
        }

        public void setTipoMoneda(String tipoMoneda) {
            this.tipoMoneda = tipoMoneda;
        }

        public void setNumeroCuenta(long numeroCuenta) {
            this.numeroCuenta = numeroCuenta;
        }
    }
