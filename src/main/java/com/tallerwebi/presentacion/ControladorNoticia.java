package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorNoticia {
    private ServicioNoticia servicioNoticia;

    @Autowired
    public ControladorNoticia(ServicioNoticia servicioNoticia) {
        this.servicioNoticia = servicioNoticia;
    }

    @RequestMapping(path = "/noticia/listar")
    public ModelAndView listarNoticias() {

        ModelMap modelo = new ModelMap();

        try{
            List<Noticia> noticias = servicioNoticia.listarNoticias();
            modelo.put("noticias", noticias);

        } catch (Exception e) {
            modelo.put("error", "Error al listar las noticias.");
            return new ModelAndView("error", modelo);
        }

        return new ModelAndView("home", modelo);
    }

    @RequestMapping(path = "/noticia/cargar", method = RequestMethod.GET)
    public ModelAndView crearNoticia() {
        ModelMap modelo = new ModelMap();

        modelo.put("datosNoticia", new Noticia());

        return new ModelAndView("cargar-noticia", modelo);
    }

    @RequestMapping(path = "/noticia/crear", method = RequestMethod.POST)
    public ModelAndView crearNuevaNoticia( @ModelAttribute("datosNoticia") Noticia noticia ) {

        ModelMap modelo = new ModelMap();

        try{
            servicioNoticia.crearNoticia(noticia);
        } catch (Exception e) {
            modelo.put("error", "Error al crear la noticia.");
            return new ModelAndView("error", modelo);
        }

        return new ModelAndView("cargar-noticia" , modelo);
    }



    @RequestMapping(path = "/noticia/borrar", method = RequestMethod.DELETE)
    public ModelAndView borrarNoticiaPorId( @ModelAttribute("datosNoticia") Long idNoticia ) {

        ModelMap modelo = new ModelMap();

        try{
            servicioNoticia.borrarNoticiaPorId(idNoticia);
        } catch (Exception e) {
            modelo.put("error", "Error al borrar la noticia.");
            return new ModelAndView("error", modelo);
        }

        return new ModelAndView("home", modelo);
    }

    @RequestMapping(path = "/noticia/buscarNoticiaPorTitulo", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView buscarNoticiaPorTitulo( @ModelAttribute("datosNoticia") String tituloNoticia ) {

        ModelMap model = new ModelMap();

        try{
            servicioNoticia.buscarNoticiaPorTitulo(tituloNoticia);

        }  catch (Exception e) {
            model.put("error", "Error al buscar noticia.");
            return new ModelAndView("error", model);
        }
        return new ModelAndView("home", model);
    }

    @RequestMapping(path = "/noticia/buscarNoticiaPorCategoria", method = RequestMethod.POST)
    public ModelAndView buscarNoticiaPorCategoria( @ModelAttribute("datosNoticia") String categoria ) {

        ModelMap model = new ModelMap();

        try{
            servicioNoticia.buscarNoticiaPorCategoria(categoria);
        }  catch (Exception e) {
            model.put("error", "Error al buscar noticia por categoria.");
            return new ModelAndView("error", model);
        }
        return new ModelAndView("home", model);
    }

}
