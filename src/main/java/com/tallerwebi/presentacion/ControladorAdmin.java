package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Rol;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @GetMapping("/admin/usuarios")
    public ModelAndView verUsuarios(HttpSession session ){

        ModelMap model=new ModelMap();

        Usuario usuario= (Usuario) session.getAttribute("sessionUsuarioLogueado");

        if(usuario == null || (usuario.getRol() != (Rol.ADMIN))){
            return new ModelAndView("redirect:/login");
        }

        List<Usuario> usuariosActivos = servicioHome.listarUsuarios(usuario.getIdUsuario());

        model.put("usuario",usuario);
        model.put("usuariosActivos", usuariosActivos);

        return new ModelAndView("usuariosActivos",model);
    }

    @RequestMapping(value = "/eliminarUsuario/{idUsuario}", method = RequestMethod.GET)
    public ModelAndView eliminarUsuario(HttpSession session , @PathVariable(required = false) Long idUsuario){

        ModelMap model=new ModelMap();

        Usuario usuario= (Usuario) session.getAttribute("sessionUsuarioLogueado");

        if(usuario == null || (usuario.getRol() != (Rol.ADMIN))){
            return new ModelAndView("redirect:/login");
        }

        try {
            //servicioUsuario.borrarUsuario(idUsuario); borrar el usuario
        } catch (Exception e) {
            model.put("error", "Error al borrar usuario.");
            return new ModelAndView("usuariosActivos", model);
        }

        return new ModelAndView("redirect:/admin/usuarios");
    }
}
