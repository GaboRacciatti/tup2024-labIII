package ar.edu.utn.frbb.tup.model.enums;

public enum TipoMovimiento {
    DEPOSITO("D"),
    RETIRO("R"),
    TRANSFERENCIA_ENTRANTE("TE"),
    TRANSFERENCIA_SALIENTE("TS"),
    CREDITO("C"),
    DEBITO("D");

    private final String descripcion;

    TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoMovimiento fromString(String text) {
        for (TipoMovimiento tipo : TipoMovimiento.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar un TipoOperacion con la descripcion: " + text);
    }
}
