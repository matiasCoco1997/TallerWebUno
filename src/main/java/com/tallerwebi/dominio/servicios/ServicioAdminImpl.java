package com.tallerwebi.dominio.servicios;

import com.tallerwebi.infraestructura.RepositorioNoticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("servicioAdmin")
@Transactional
public class ServicioAdminImpl implements ServicioAdmin {

    RepositorioNoticia repositorioNoticia;

    @Autowired
    public ServicioAdminImpl(RepositorioNoticia repositorioNoticia) {
        this.repositorioNoticia = repositorioNoticia;
    }

    @Override
    public Integer obtenerNroNoticiasPorCategoria(String categoria) {
        return repositorioNoticia.buscarPorCategoria(categoria).size();
    }
}
