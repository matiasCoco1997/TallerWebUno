package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Categoria.Categoria;
import com.tallerwebi.dominio.Noticia.Noticia;
import com.tallerwebi.dominio.Categoria.RepositorioCategoria;
import com.tallerwebi.dominio.Noticia.ServicioNoticia;
import com.tallerwebi.dominio.Usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ControladorNoticia {
    private ServicioNoticia servicioNoticia;

    @Autowired
    public ControladorNoticia(ServicioNoticia servicioNoticia) {
        this.servicioNoticia = servicioNoticia;
    }

    @RequestMapping(path = "/crearNuevaNoticia", method = RequestMethod.POST)
    //@RequestParam("imagenArchivo") String multipartFile
    public ModelAndView crearNuevaNoticia( @ModelAttribute("datosNoticia") Noticia noticia ) {

        ModelMap model = new ModelMap();

        try{
            servicioNoticia.crearNoticia(noticia);
            return new ModelAndView("home");
        } catch (Exception e) {
            model.put("error", "Error al crear la noticia");
            return new ModelAndView("home");
        }
    }

    @RequestMapping(path = "/borrarNoticia", method = RequestMethod.POST)
    //@RequestParam("imagenArchivo") String multipartFile
    public ModelAndView borrarNoticiaPorId( @ModelAttribute("datosNoticia") Long idNoticia ) {

        ModelMap model = new ModelMap();

        try{
            servicioNoticia.borrarNoticiaPorId(idNoticia);
            return new ModelAndView("home");
        } catch (Exception e) {
            model.put("error", "Error al borrar la noticia.");
            return new ModelAndView("home");
        }
    }
}
