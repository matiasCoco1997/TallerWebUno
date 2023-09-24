package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Comentario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPublicacion {
    @RequestMapping(path = "/publicacion")
    public ModelAndView irApublicacion(){
        ModelMap model = new ModelMap();
        model.put("comentario", new Comentario());
        return new ModelAndView("noticia", model);
    }
}
