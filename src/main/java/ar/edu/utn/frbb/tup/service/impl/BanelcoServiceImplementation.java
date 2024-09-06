package ar.edu.utn.frbb.tup.service.impl;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.service.BanelcoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BanelcoServiceImplementation implements BanelcoService {

    @Autowired
    private CuentaDao cuentaDao;

    @Override
    public TransferenciaResponseDto realizarTransferenciaEntreBancos(TransferenciaDto transferenciaDto) {
        Cuenta cuentaOrigen = cuentaDao.find(transferenciaDto.getCuentaOrigen());
        Cuenta cuentaDestino = cuentaDao.find(transferenciaDto.getCuentaDestino());

        if (cuentaOrigen == null) {
            return new TransferenciaResponseDto("FALLIDA", "La cuenta origen no existe en el banco.");
        }

        if (cuentaDestino == null) {
            return new TransferenciaResponseDto("FALLIDA", "La cuenta destino no existe.");
        }

        if (!cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda())) {
            return new TransferenciaResponseDto("FALLIDA", "Las monedas de las cuentas no coinciden.");
        }

        double montoFinal = transferenciaDto.getMonto();
        if (cuentaOrigen.getTipoBanco() == null || cuentaDestino.getTipoBanco() == null) {
            return new TransferenciaResponseDto("FALLIDA", "No se puede dejar el banco vacio");
        }

        if (cuentaOrigen.getTipoBanco() != cuentaDestino.getTipoBanco()) {
            montoFinal = calcularComision(montoFinal, transferenciaDto.getMoneda());
            boolean transferenciaExitosa = simularTransferenciaExterna();
            if (!transferenciaExitosa) {
                return new TransferenciaResponseDto("FALLIDA", "La transferencia interbancaria no se pudo completar.");
            }
        }

        actualizarSaldos(cuentaOrigen, cuentaDestino, montoFinal);
        return new TransferenciaResponseDto("EXITOSA", "Transferencia interbancaria realizada con éxito.");
    }

    private void actualizarSaldos(Cuenta cuentaOrigen, Cuenta cuentaDestino, double montoFinal) {
        cuentaOrigen.setBalance(cuentaOrigen.getBalance() - montoFinal);
        cuentaDestino.setBalance(cuentaDestino.getBalance() + montoFinal);
        cuentaDao.update(cuentaOrigen); 
        cuentaDao.update(cuentaDestino); 
    }

    private double calcularComision(double monto, String tipoMoneda) {
        if ("P".equalsIgnoreCase(tipoMoneda) && monto > 1000000) {
            return monto - (monto * 0.02); 
        } else if ("D".equalsIgnoreCase(tipoMoneda) && monto > 5000) {
            return monto - (monto * 0.005); 
        }
        return monto; 
    }

    private boolean simularTransferenciaExterna() {
        return Math.random() > 0.2; 
    }
}


