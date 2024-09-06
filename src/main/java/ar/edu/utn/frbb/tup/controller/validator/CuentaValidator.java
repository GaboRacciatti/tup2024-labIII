package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;


@Component
public class CuentaValidator {
    public void nohaycuentas (CuentaDto cuentaDto) {
        if (cuentaDto == null) {
            throw new IllegalArgumentException("No hay cuentas");
        }
    }

    public void validate(CuentaDto cuentaDto) {
        validateTipoCuenta(cuentaDto);
        validateMoneda(cuentaDto);
    }

    private void validateTipoCuenta(CuentaDto cuentaDto) {
        if (!"CC".equals(cuentaDto.getTipoCuenta()) && !"CA".equals(cuentaDto.getTipoCuenta())) {
            throw new IllegalArgumentException("El tipo de cuenta no es correcto");
        }
    }

    private void validateMoneda(CuentaDto cuentaDto) {
        if (!"P".equals(cuentaDto.getMoneda()) && !"D".equals(cuentaDto.getMoneda())) {
            throw new IllegalArgumentException("El tipo de moneda no es correcto");
        }
    }
}
