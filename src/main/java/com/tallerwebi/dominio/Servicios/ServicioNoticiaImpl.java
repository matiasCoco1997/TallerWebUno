package com.tallerwebi.dominio.Servicios;

import com.tallerwebi.dominio.Entidades.Noticia;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Noticia> listarNoticias() {

        List<Noticia> noticias = repositorioNoticia.listarNoticias();

        return noticias;
    }

    /*
    @Override
    public Noticia buscarNoticiaPorTitulo(String tituloNoticia) {

        List

        return repositorioNoticia.buscarPorId(idNoticia);
    }*/

    @Override
    public Noticia buscarNoticiaPorId(Long idNoticia) {
        return repositorioNoticia.buscarPorId(idNoticia);
    }

    @Override
    public List<Noticia> buscarNoticiaPorTitulo(String tituloNoticia) {

        List<Noticia> noticiasEncontradas = repositorioNoticia.buscarPorTitulo(tituloNoticia);

        return noticiasEncontradas;
    }

    @Override
    public List<Noticia> buscarNoticiaPorCategoria(String categoria) {

        List<Noticia> noticiasEncontradas = repositorioNoticia.buscarPorCategoria(categoria);

        return noticiasEncontradas;
    }

    @Override
    public void editarNoticia(Long idNoticia) {

    }


}
