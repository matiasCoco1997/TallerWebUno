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
    private RepositorioCategoria repositorioCategoria;

    @Autowired
    public ControladorNoticia(ServicioNoticia servicioNoticia, RepositorioCategoria repositorioCategoria) {
        this.servicioNoticia = servicioNoticia;
        this.repositorioCategoria = repositorioCategoria;
    }

    /*
    @RequestMapping("/nueva-noticia")
    public ModelAndView irNuevaNoticia() {
        ModelMap modelo = new ModelMap();
        modelo.put("datosNoticia", new Noticia());
        modelo.put("categorias", repositorioCategoria.buscarTodasCategoria());
        return new ModelAndView("cargar-noticia", modelo);
    }
*/
    @RequestMapping(path = "/crearNuevaNoticia", method = RequestMethod.POST)
    //@RequestParam("imagenArchivo") String multipartFile
    public String crearNuevaNoticia(  @ModelAttribute("datosNoticia") Noticia noticia, HttpSession session) {

        ModelAndView resultado;

        String rolUsuario = (String)session.getAttribute("ROL");

        return rolUsuario;
/*
        if (rolUsuario == "Editor") {


            Categoria categoriaSeleccionada = repositorioCategoria.buscarPorDescripcion(String.valueOf(noticia.getCategoria()));

            if (categoriaSeleccionada != null) {
                noticia.setCategoria(categoriaSeleccionada.getDescripcion());
            }

            servicioNoticia.crearNoticia(noticia, rolDelUsuarioLogueado);

            ModelAndView modelAndView = new ModelAndView("home");

            modelAndView.addObject("noticia", noticia);


            resultado = new ModelAndView("redirect:/pruebas");
        }

        resultado = new ModelAndView("redirect:/pruebas");
*/

    }

}
