package com.tallerwebi.dominio.servicios;

import org.springframework.stereotype.Service;

@Service
public class ServicioAdminImpl implements ServicioAdmin {

    @Override
    public Integer obtenerNroNoticiasPorCategoria(String categoria) {
        return 1;
    }
}
