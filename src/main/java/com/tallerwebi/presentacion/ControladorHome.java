package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioHome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorHome {

    private ServicioHome servicioHome;

    @Autowired
    public ControladorHome(ServicioHome servicioHome){
        this.servicioHome=servicioHome;
    }

    @RequestMapping("/home")
    public ModelAndView home(HttpSession session){
        ModelMap model=new ModelMap();
        List<Noticia> noticias = servicioHome.listarNoticias();
        List<Usuario> usuarios= servicioHome.listarUsuarios();
        List<Categoria> categorias=servicioHome.obtenerCategorias();
        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        model.put("noticias", noticias);
        model.put("usuarios",usuarios);
        model.put("categorias",categorias);
        model.put("usuario",usuario);
        return new ModelAndView("home-vista",model);
    }

    @RequestMapping(value = "/categoria")
    public ModelAndView validarCategoria(@RequestParam("categoria")String categoria, HttpSession session){
        ModelMap model=new ModelMap();
        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Categoria> categorias=servicioHome.obtenerCategorias();
        List<Noticia> noticiasCategorias = servicioHome.obtenerNoticiasPorCategoria(categoria);
        model.put("categorias",categorias);
        model.put("usuario",usuario);
        model.put("noticias",noticiasCategorias);
        return new ModelAndView("home-categoria", model);
    }
}
