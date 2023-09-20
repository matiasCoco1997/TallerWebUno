package com.tallerwebi.dominio.Servicios;

import com.tallerwebi.dominio.Entidades.Noticia;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import org.springframework.stereotype.Service;

@Service("servicioNoticia")
public class ServicioNoticiaImpl implements ServicioNoticia {

    private final RepositorioNoticia repositorioNoticia;

    public ServicioNoticiaImpl(RepositorioNoticia repositorioNoticia) {
        this.repositorioNoticia = repositorioNoticia;
    }

    @Override
    public void crearNoticia(Noticia noticia) {
        repositorioNoticia.guardar(noticia);
    }

    @Override
    public void borrarNoticiaPorId(Long idNoticia) {

        Noticia noticia = this.buscarNoticiaPorId(idNoticia);

        repositorioNoticia.borrarNoticia(noticia);
    }

    @Override
    public Noticia buscarNoticiaPorId(Long idNoticia) {

        return repositorioNoticia.buscarPorId(idNoticia);
    }

    @Override
    public void editarNoticia(Long idNoticia) {

    }


}
