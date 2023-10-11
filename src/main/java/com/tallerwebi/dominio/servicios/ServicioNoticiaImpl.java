package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import com.tallerwebi.presentacion.DatosLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Service("servicioNoticia")
@Transactional
public class ServicioNoticiaImpl implements ServicioNoticia {

    private final RepositorioNoticia repositorioNoticia;

    @Autowired
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
        List<Noticia> noticiasActivas = new ArrayList<>();

        for (Noticia noticia : noticias) {
            if (noticia.getActiva()){
                noticiasActivas.add(noticia);
            }
        }

        return noticiasActivas;
    }

    @RequestMapping("/noticia/login")
    public ModelAndView cerrarSesion() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("redirect:/login", modelo);
    }

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
    @Override
    public void darMeGusta(Noticia noticia) {
        noticia.setLikes(noticia.getLikes() + 1);
        repositorioNoticia.modificar(noticia);
    }

    @Override
    public boolean verificarQueNoEsNull(Noticia noticia) {
        return noticia==null;
    }


}
