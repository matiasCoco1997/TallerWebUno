package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorHome {

    private final ServicioHome servicioHome;
    private final ServicioUsuario servicioUsuario;
    private final ServicioNoticia servicioNoticia;

    @Autowired
    public ControladorHome(ServicioHome servicioHome, ServicioUsuario servicioUsuario, ServicioNoticia servicioNoticia){
        this.servicioHome = servicioHome;
        this.servicioUsuario = servicioUsuario;
        this.servicioNoticia = servicioNoticia;
    }

    @RequestMapping("/home")
    public ModelAndView home(HttpSession session){
        ModelMap model=new ModelMap();

        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");

        if(usuario == null){
            return new ModelAndView("redirect:/login");
        }

        List<Noticia> noticias = servicioNoticia.listarNoticias();

        noticias = servicioNoticia.setNoticiasLikeadas(noticias, usuario.getIdUsuario());

        List<Categoria> categorias = servicioHome.obtenerCategorias();

        List<Notificacion> notificaciones = servicioHome.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());

        List<Usuario> usuarios = null; //Acá debería ir el id del usuario que inició sesión pero, si lo hago, me tira mal los test

        usuarios = servicioUsuario.listarUsuarioParaSeguir(usuario.getIdUsuario());

        List<Usuario> usuariosSeguidos = servicioUsuario.listarUsuarioseguidos(usuario.getIdUsuario());

        List<Noticia> noticiasCarrousel  = servicioNoticia.obtenerNoticiasCategoria(usuario.getIdUsuario(), 5);

        model.put("noticias", noticias);
        model.put("notificaciones", notificaciones.size());
        model.put("usuarios",usuarios);
        model.put("usuariosSeguidos",usuariosSeguidos);
        model.put("categorias",categorias);
        model.put("usuario",usuario);
        model.put("noticiasCarrousel", noticiasCarrousel);
        return new ModelAndView("home-vista",model);
    }

    @RequestMapping(value = "/categoria")
    public ModelAndView filtrarPorCategoria(@RequestParam("categoria")String categoria, HttpSession session){
        ModelMap model=new ModelMap();

        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Categoria> categorias=servicioHome.obtenerCategorias();
        List<Noticia> noticiasCategorias = servicioHome.obtenerNoticiasPorCategoria(categoria);

        noticiasCategorias = servicioNoticia.setNoticiasLikeadas(noticiasCategorias, usuario.getIdUsuario());

        List<Notificacion> notificaciones=servicioHome.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());

        model.put("categorias",categorias);
        model.put("usuario",usuario);
        model.put("noticias",noticiasCategorias);
        model.put("notificaciones", notificaciones.size());
        return new ModelAndView("home-categoria", model);
    }

    @RequestMapping("/titulo")
    public ModelAndView filtrarPorTitulo(@RequestParam("titulo")String titulo,HttpSession session){
        ModelMap model=new ModelMap();

        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");

        List<Categoria> categorias=servicioHome.obtenerCategorias();

        Integer notificaciones=servicioHome.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario()).size();

        model.put("usuario",usuario);
        model.put("categorias",categorias);
        model.put("notificaciones", notificaciones);

        List<Noticia> noticias=servicioHome.obtenerNoticiasPorTitulo(titulo);

        noticias = servicioNoticia.setNoticiasLikeadas(noticias, usuario.getIdUsuario());

        if(servicioHome.validarQueHayNoticias(noticias)){
            model.put("error","No se encontraron noticias con este título: " + titulo);
        }else{
            model.put("noticias",noticias);
        }
        return new ModelAndView("home-titulo",model);
    }
    @GetMapping("/noticia-de-seguidos")
    public ModelAndView mostrarNoticiaDeSeguidos(HttpSession session){
        Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Noticia> noticias = servicioHome.obtenerNoticiaDeSeguidos(usuarioLogueado.getIdUsuario());

        ModelMap model = new ModelMap();
        model.put("noticias", noticias);

        List<Categoria> categorias=servicioHome.obtenerCategorias();

        List<Notificacion> notificaciones=servicioHome.obtenerMisNotificacionesSinLeer(usuarioLogueado.getIdUsuario());

        List<Usuario> usuarios = servicioUsuario.listarUsuarioParaSeguir(usuarioLogueado.getIdUsuario());

        model.put("notificaciones", notificaciones.size());
        model.put("usuarios",usuarios);
        model.put("categorias",categorias);
        model.put("usuario",usuarioLogueado);

        return new ModelAndView("home-vista",model);
    }

}
