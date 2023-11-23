package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.excepcion.MismoRol;
import com.tallerwebi.dominio.excepcion.UsuarioInexistente;
import com.tallerwebi.dominio.servicios.ServicioAdmin;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.List;

@Controller
public class ControladorAdmin {
    private final ServicioHome servicioHome;
    private final ServicioUsuario servicioUsuario;
    private final ServicioNoticia servicioNoticia;
    private ServicioAdmin servicioAdmin;

    @Autowired
    public ControladorAdmin(ServicioHome servicioHome, ServicioUsuario servicioUsuario, ServicioNoticia servicioNoticia, ServicioAdmin servicioAdmin){
        this.servicioHome = servicioHome;
        this.servicioUsuario = servicioUsuario;
        this.servicioNoticia = servicioNoticia;
        this.servicioAdmin = servicioAdmin;
    }

    @RequestMapping("/informes")
    public ModelAndView informes(HttpSession session){
        ModelMap model=new ModelMap();
        Usuario usuario= (Usuario) session.getAttribute("sessionUsuarioLogueado");
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
        model.put("deportes", servicioAdmin.obtenerNroNoticiasPorCategoria("Deportes"));
        model.put("politica", servicioAdmin.obtenerNroNoticiasPorCategoria("Politica"));
        model.put("programacion", servicioAdmin.obtenerNroNoticiasPorCategoria("Programacion"));
        model.put("arte", servicioAdmin.obtenerNroNoticiasPorCategoria("Arte"));
        model.put("juegos", servicioAdmin.obtenerNroNoticiasPorCategoria("Juegos"));

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
            Usuario usuarioABorrar = servicioUsuario.obtenerUsuarioPorId(idUsuario);
            servicioUsuario.borrarUsuario(usuarioABorrar);
        } catch (Exception e) {
            model.put("error", "Error al borrar usuario.");
            return new ModelAndView("usuariosActivos", model);
        }

        return new ModelAndView("redirect:/admin/usuarios");
    }

    @RequestMapping(value = "/hacerAdmin/{idUsuario}")
    public ModelAndView darRolAdmin(@PathVariable Long idUsuario, HttpSession session) throws Exception {
        ModelMap model = new ModelMap();
        Usuario admin = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario);
        if(!admin.getRol().equals(Rol.ADMIN)){
            return new ModelAndView("redirect:/login");
        }

        try{
            servicioUsuario.darRolAdmin(usuario);

        }catch (UsuarioInexistente e){
            model.put("error", "El usuario no existe");
            return new ModelAndView("usuariosActivos", model);
        }catch (MismoRol e){
            model.put("error", "No se puede cambiar el rol porque ya esta seteado.");
            return new ModelAndView("usuariosActivos", model);
        }
        return new ModelAndView("redirect:/admin/usuarios");
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

    @RequestMapping("/fecha")
    public ModelAndView filtrarPorFecha(@RequestParam("fecha-publicacion")String fechaPublicacion, HttpSession session){
        ModelMap model=new ModelMap();

        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");

        List<Categoria> categorias=servicioHome.obtenerCategorias();

        Integer notificaciones=servicioHome.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario()).size();

        model.put("usuario",usuario);
        model.put("categorias",categorias);
        model.put("notificaciones", notificaciones);

        List<Noticia> noticias = servicioNoticia.obtenerNoticiasPorFecha(fechaPublicacion);
        noticias = servicioNoticia.setNoticiasLikeadas(noticias, usuario.getIdUsuario());
        if(servicioHome.validarQueHayNoticias(noticias)){
            model.put("error","No se encontraron noticias de esa fecha.");
        }else{
            model.put("noticias",noticias);
        }

        return new ModelAndView("home-fecha",model);
    }
}
