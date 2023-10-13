package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario){
        this.servicioUsuario=servicioUsuario;
    }

    @RequestMapping("/perfil")
    public ModelAndView perfil(HttpSession session){
        ModelMap model=new ModelMap();
        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        model.put("usuario",usuario);
        return new ModelAndView("perfil",model);
    }

}
