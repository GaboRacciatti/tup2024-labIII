package ar.edu.utn.frbb.tup.model.enums;

public enum TipoBanco {
    PROVINCIA,
    BBVA,
    NACION,
    SANTANDER,
    PAMPA;

    public static TipoBanco fromString(String banco) {
        try {
            return TipoBanco.valueOf(banco.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de banco desconocido: " + banco);
        }
    }
}
