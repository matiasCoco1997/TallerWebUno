package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioNoticia")
@Transactional
public class ServicioNoticiaImpl implements ServicioNoticia {
    private RepositorioNoticia servicioNoticiaDao;

    @Autowired
    public ServicioNoticiaImpl(RepositorioNoticia servicioNoticiaDao){
        this.servicioNoticiaDao = servicioNoticiaDao;
    }
    @Override
    public Boolean crearNoticia(Noticia noticia) {
        servicioNoticiaDao.guardar(noticia);
        return true;
    }

    @Override
    public Noticia consultarNoticiaTitulo(String titulo) {
        return servicioNoticiaDao.buscarNoticia(titulo);
    }
}
