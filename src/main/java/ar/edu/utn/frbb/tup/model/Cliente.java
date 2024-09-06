package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;

public class Cliente extends Persona {

    private TipoPersona tipoPersona;
    private LocalDate fechaAlta;
    private List<Cuenta> cuentas = new ArrayList<>();

    public Cliente(){};

    public Cliente(ClienteDto clienteDto) {
        super(clienteDto.getDni(), 
              clienteDto.getApellido(), 
              clienteDto.getNombre(), 
              LocalDate.parse(clienteDto.getFechaNacimiento()), 
              clienteDto.getTelefono(), 
              clienteDto.getDireccion());
              
        this.tipoPersona = TipoPersona.fromString(clienteDto.getTipoPersona());
        this.fechaAlta = LocalDate.now();
    }
    
    

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }



    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    public int getEdad() {
        return Period.between(this.getFechaNacimiento(), LocalDate.now()).getYears();
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    public boolean tieneCuenta(TipoCuenta tipoCuenta, TipoMoneda moneda) {
        return cuentas.stream()
            .anyMatch(cuenta -> cuenta.getTipoCuenta().equals(tipoCuenta) && cuenta.getMoneda().equals(moneda));
    }



    @Override
    public String toString() {
        return "\n ///// Cliente ///// \n" +
                " dni: " + getDni() +
                "\n nombre: " + getNombre() + 
                "\n apellido: " + getApellido() + 
                "\n tipoPersona: " + getTipoPersona() +
                "\n fechaAlta: " + getFechaAlta() +
                "\n edad: " + getEdad() +
                "\n cuentas: " + getCuentas();
    }
}
