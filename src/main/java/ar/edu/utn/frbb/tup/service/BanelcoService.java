package ar.edu.utn.frbb.tup.service;

import java.util.Random;

import org.springframework.stereotype.Service;


@Service
public class BanelcoService {
    private final Random random = new Random();

    public boolean realizarTransferenciaBanelco(long numeroCuentaOrigen, long numeroCuentaDestino, double monto) {
        return random.nextDouble() < 0.75;
    }
}

