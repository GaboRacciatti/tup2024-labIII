package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import org.springframework.stereotype.Component;

@Component
public class ClienteValidator {

    public void validate(ClienteDto clienteDto) {
        if (clienteDto.getNombre() == null || clienteDto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (clienteDto.getApellido() == null || clienteDto.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if (clienteDto.getTipoPersona() == null || clienteDto.getTipoPersona().isEmpty()) {
            throw new IllegalArgumentException("El tipo de persona no puede estar vacío");
        }
        
        if (clienteDto.getDireccion() == null || clienteDto.getDireccion().isEmpty()) {
            throw new IllegalArgumentException("La direccion no puede estar vacía");
        }
        if (clienteDto.getTelefono() == null || clienteDto.getTelefono().isEmpty()) {
            throw new IllegalArgumentException("El Telefono no puede estar vacío");
        }
    }

}
