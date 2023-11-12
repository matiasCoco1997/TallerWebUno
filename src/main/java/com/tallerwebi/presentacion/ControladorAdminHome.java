package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.*;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller()
public class ControladorAdminHome {

    @GetMapping("/admin/home")
    public ModelAndView irAHomeAdmin(HttpSession session ){
        ModelMap model=new ModelMap();

        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");

        if(usuario == null && usuario.getRol() != (Rol.ADMIN)){
            return new ModelAndView("redirect:/login");
        }

        model.put("usuario",usuario);

        return new ModelAndView("home-admin",model);
    }
}
