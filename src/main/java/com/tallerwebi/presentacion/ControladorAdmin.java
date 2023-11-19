package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorAdmin {
    private final ServicioHome servicioHome;
    private final ServicioUsuario servicioUsuario;
    private final ServicioNoticia servicioNoticia;

    @Autowired
    public ControladorAdmin(ServicioHome servicioHome, ServicioUsuario servicioUsuario, ServicioNoticia servicioNoticia){
        this.servicioHome = servicioHome;
        this.servicioUsuario = servicioUsuario;
        this.servicioNoticia = servicioNoticia;
    }

    @RequestMapping("/informes")
    public ModelAndView informes(HttpSession session){
        ModelMap model=new ModelMap();
        Usuario usuario= (Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Notificacion> notificaciones = servicioHome.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());


        model.put("notificaciones",notificaciones);
        model.put("usuario",usuario);
        return new ModelAndView("informes",model);
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

        return new ModelAndView("home-admin",model);
    }

    @RequestMapping("/noticias-mas-likeadas")
    public ModelAndView mostrarNoticiasMasLikeadas(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        if (usuario.getRol().equals(Rol.ADMIN)) {
            List<Noticia> noticias = servicioNoticia.listarNoticiasMasLikeadas();
            model.put("noticias", noticias);
            model.put("usuario", usuario);
        }
        return new ModelAndView("noticias-mas-likeadas", model);
    }
}
