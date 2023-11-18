package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.servicios.ServicioAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller()
public class ControladorAdminHome {

    private final ServicioAdmin servicioAdmin;

    @Autowired
    public ControladorAdminHome(ServicioAdmin servicioAdmin) {
        this.servicioAdmin = servicioAdmin;
    }

    @GetMapping("/admin/home")
    public ModelAndView irAHomeAdmin(HttpSession session ){
        ModelMap model=new ModelMap();

        Usuario usuario= (Usuario) session.getAttribute("sessionUsuarioLogueado");

        if(usuario == null)
            return new ModelAndView("redirect:/login");

        if(usuario.getRol() != (Rol.ADMIN))
            return new ModelAndView("redirect:/login");

        model.put("usuario",usuario);
        model.put("deportes", servicioAdmin.obtenerNroNoticiasPorCategoria("Deportes"));
        model.put("politica", servicioAdmin.obtenerNroNoticiasPorCategoria("Politica"));
        model.put("programacion", servicioAdmin.obtenerNroNoticiasPorCategoria("Programacion"));
        model.put("arte", servicioAdmin.obtenerNroNoticiasPorCategoria("Arte"));
        model.put("juegos", servicioAdmin.obtenerNroNoticiasPorCategoria("Juegos"));

        return new ModelAndView("home-admin",model);
    }
}
