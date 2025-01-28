# Sistema Bancario con Java y Spring Boot  
Este proyecto es una aplicación de backend desarrollada en **Java** utilizando **Spring Boot**, diseñada para simular un sistema bancario con una base de datos en memoria. El sistema incluye funcionalidades clave como la creación de clientes, la apertura de cuentas, transferencias entre cuentas, y más.  

## Funcionalidades  
- **Gestión de Clientes:**  
  - Crear un cliente con atributos como nombre, apellido, DNI, fecha de nacimiento, etc.  
  - Listar, actualizar y eliminar clientes.  
- **Gestión de Cuentas:**  
  - Crear cuentas bancarias para clientes existentes:  
    - Tipos de cuenta: *Caja de Ahorro* y *Cuenta Corriente*.  
    - Monedas disponibles: *Pesos* y *Dólares*.  
  - Consultar y eliminar cuentas.  
- **Operaciones Bancarias:**  
  - Transferencias entre cuentas bancarias.  
  - Depósitos y retiros de cuentas.  

## Tecnologías Utilizadas  
- **Lenguaje:** Java  
- **Frameworks:** Spring Boot, Spring Data, Spring Web
- - **Testing:** Mockito  
- **Control de versiones:** Git  
- **Serialización JSON:** Se utilizó `@JsonInclude` para excluir valores nulos en las respuestas JSON.  

## Cómo Ejecutar el Proyecto  
1. Clona el repositorio desde GitHub:  
   ```bash
   git clone https://github.com/tu-usuario/tu-repo.git
2. Ejecuta el archivo principal:
   ```bash
   src\main\java\ar\edu\utn\frbb\tup\Application.java
3. Accede a los endpoints desde herramientas como Postman o cualquier cliente HTTP.

## Endpoints y Ejemplos de Uso
# Clientes
**Crear Cliente**
**Endpoint**:
   ```bash
POST /api/clientes
 ```
**Ejemplo de Request**:
   ```bash

{
  "nombre": "Gabriel",
  "apellido": "Racciatti",
  "dni": 46352010,
  "fechaNacimiento": "2005-04-19",
  "telefono": "2915082493",
  "direccion": "Zelarrayan 655",
  "tipoPersona": "F"
}
```
# Cuentas
**Crear Cuentas**
**Endpoint**:
   ```bash
POST /api/cuentas
 ```
**Ejemplo de Request**:
   ```bash
{
  "tipoCuenta": "CA",
  "dniTitular": 46352010,
  "moneda": "P",
  "balance": 100000,
  "tipoBanco": "BBVA"
}
```
# Transferencias
**Realizar Transferencia**
**Endpoint**:
   ```bash
POST /api/transferencias
 ```
**Ejemplo de Request**:
   ```bash
{
  "cuentaOrigen": 1,
  "cuentaDestino": 2,
  "monto": 1000.0,
  "tipoMoneda": "P"
}
```

# Depósitos y Retiros

**Depósito o Retiro**
**Endpoint**:
   ```bash
POST /api/movimientos
 ```
**Ejemplo de Request**:
**Deposito**:
   ```bash
{
  "numeroCuenta": 1,
  "monto": 500.0,
  "moneda": "PESOS"
}
 ```
**Retiro**:
   ```bash
{
  "numeroCuenta": 2,
  "monto": 1000.0,
  "moneda": "PESOS"
}
 ```
# Estructura del Proyecto
## El proyecto sigue una arquitectura RESTful bien definida con las siguientes capas principales:
 - Controller: Manejo de las solicitudes HTTP y mapeo de endpoints.
 - Service: Lógica de negocio.
 - Model: Representación de las entidades del sistema (Cliente, Cuenta, Movimiento).
