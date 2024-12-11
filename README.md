# tup2024
# PARA EJECUTAR EL PROYECTO: RUN EN src\main\java\ar\edu\utn\frbb\tup\Application.java
## SE UTILIZO TODO LO VISTO EN EL CURSADO, SOLO INCLUÍ JSONInclude PARA NO MOSTRAR NULLS.
# EJEMPLOS DE ENDPOINTS.
# //CLIENTES

{
  "nombre": "Gabriel",
  "apellido": "Racciatti",
  "dni": 46352010,
  "fechaNacimiento": "2005-04-19",
  "telefono": "2915082493",
  "direccion": "Zelarrayan 655",
  "tipoPersona": "F"
}
{
  "nombre": "Pepe",
  "apellido": "Rino",
  "dni": 22539121,
  "fechaNacimiento": "1990-05-21",
  "telefono": "291442660",
  "direccion": "Av. Alem 655",
  "tipoPersona": "F"
}
//CUENTAS
{
  "tipoCuenta": "CA",
  "dniTitular": 46352010,
  "moneda": "P",
  "balance": 100000,
  "tipoBanco": "BBVA"
}

{
  "tipoCuenta": "CA",
  "dniTitular": 22539121,
  "moneda": "P",
  "balance": 100000,
  "tipoBanco": "BBVA"
}
// TRANSFERENCIA
{
    "cuentaOrigen": 1,
    "cuentaDestino": 2,
    "monto": 1000.0,
    "tipoMoneda": "P"
}
//Deposito / Retiro
{
  "numeroCuenta": 1,
  "monto": 500.0,
  "moneda": "PESOS"
}
{
  "numeroCuenta": 2,
  "monto": 1000,
  "moneda" : "PESOS"
}
