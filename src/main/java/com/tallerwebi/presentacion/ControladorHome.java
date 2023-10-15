package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorHome {

    private final ServicioHome servicioHome;

    @Autowired
    public ControladorHome(ServicioHome servicioHome){
        this.servicioHome=servicioHome;
    }

    @RequestMapping("/home")
    public ModelAndView home(HttpSession session){
        ModelMap model=new ModelMap();

        List<Noticia> noticias = servicioHome.listarNoticias();


        List<Categoria> categorias=servicioHome.obtenerCategorias();

        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Notificacion> notificaciones=servicioHome.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());

        List<Usuario> usuarios= servicioHome.listarUsuarios(usuario.getIdUsuario()); //Acá debería ir el id del usuario que inició sesión pero, si lo hago, me tira mal los test

        model.put("noticias", noticias);
        model.put("notificaciones", notificaciones.size());
        model.put("usuarios",usuarios);
        model.put("categorias",categorias);
        model.put("usuario",usuario);

        return new ModelAndView("home-vista",model);
    }

    @RequestMapping(value = "/categoria")
    public ModelAndView filtrarPorCategoria(@RequestParam("categoria")String categoria, HttpSession session){
        ModelMap model=new ModelMap();

        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Categoria> categorias=servicioHome.obtenerCategorias();
        List<Noticia> noticiasCategorias = servicioHome.obtenerNoticiasPorCategoria(categoria);

        model.put("categorias",categorias);
        model.put("usuario",usuario);
        model.put("noticias",noticiasCategorias);
        return new ModelAndView("home-categoria", model);
    }

    @RequestMapping("/titulo")
    public ModelAndView filtrarPorTitulo(@RequestParam("titulo")String titulo,HttpSession session){
        ModelMap model=new ModelMap();

        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Categoria> categorias=servicioHome.obtenerCategorias();
        model.put("usuario",usuario);
        model.put("categorias",categorias);

        List<Noticia> noticias=servicioHome.obtenerNoticiasPorTitulo(titulo);
        if(servicioHome.validarQueHayNoticias(noticias)){
            String error="No se encontraron noticias con este título: "+titulo;
            model.put("error",error);
        }else{
            model.put("noticias",noticias);
        }
        return new ModelAndView("home-titulo",model);
    }
}
