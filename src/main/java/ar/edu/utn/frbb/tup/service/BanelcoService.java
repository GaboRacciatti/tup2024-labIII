package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Service;

import ar.edu.utn.frbb.tup.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.dto.TransferenciaResponseDto;

@Service
public interface BanelcoService {
    TransferenciaResponseDto realizarTransferenciaEntreBancos(TransferenciaDto transferenciaDto);
}

