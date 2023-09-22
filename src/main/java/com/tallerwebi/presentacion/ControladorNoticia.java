package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Entidades.Noticia;
import com.tallerwebi.dominio.Servicios.ServicioNoticia;
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



    @RequestMapping(path = "/noticia/listar", method = RequestMethod.GET)
    public ModelAndView listarNoticias() {

        ModelMap model = new ModelMap();

        try{
            List<Noticia> noticias = servicioNoticia.listarNoticias();
            model.put("noticias", noticias);

        } catch (Exception e) {
            model.put("error", "Error al crear la noticia");
            return new ModelAndView("error");
        }

        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(path = "/noticia/crear", method = RequestMethod.POST)
    //@RequestParam("imagenArchivo") String multipartFile
    public ModelAndView crearNuevaNoticia( @ModelAttribute("datosNoticia") Noticia noticia ) {

        ModelMap model = new ModelMap();

        try{
            servicioNoticia.crearNoticia(noticia);
        } catch (Exception e) {
            model.put("error", "Error al crear la noticia");
            return new ModelAndView("home");
        }

        return new ModelAndView("redirect:/home" );
    }


    @RequestMapping(path = "/noticia/borrar", method = RequestMethod.POST)
    //@RequestParam("imagenArchivo") String multipartFile
    public ModelAndView borrarNoticiaPorId( @ModelAttribute("datosNoticia") Long idNoticia ) {

        ModelMap model = new ModelMap();

        try{
            servicioNoticia.borrarNoticiaPorId(idNoticia);
        } catch (Exception e) {
            model.put("error", "Error al borrar la noticia.");
            return new ModelAndView("error");
        }

        return new ModelAndView("redirect:/noticiaBorrada");
    }

    @RequestMapping(path = "/noticia/listar?busqueda=", method = RequestMethod.POST)
    //@RequestParam("imagenArchivo") String multipartFile
    public ModelAndView buscarNoticiaPorId( @ModelAttribute("datosNoticia") String tituloNoticia ) {

        ModelMap model = new ModelMap();

        try{
            //model.put(servicioNoticia.buscarNoticiaPorTitulo(tituloNoticia));
        } catch (Exception e) {
            model.put("error", "Error al buscar noticia.");
            return new ModelAndView("error");
        }
        return new ModelAndView("redirect:/noticiaBuscada", model);
    }


}
