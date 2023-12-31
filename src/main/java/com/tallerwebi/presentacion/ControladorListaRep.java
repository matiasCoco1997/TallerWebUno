package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.servicios.ServicioHome;
import com.tallerwebi.dominio.servicios.ServicioListaRep;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorListaRep {

    private final ServicioUsuario servicioUsuario;
    private ServicioListaRep servicioListaRep;
    private ServicioNoticia servicioNoticia;

    @Autowired
    public ControladorListaRep(ServicioListaRep servicioListaRep, ServicioNoticia servicioNoticia, ServicioUsuario servicioUsuario){
        this.servicioListaRep=servicioListaRep;
        this.servicioNoticia=servicioNoticia;
        this.servicioUsuario=servicioUsuario;
    }

    @RequestMapping("/listaReproduccion")
    public ModelAndView obtenerListas(HttpSession session){
        ModelMap model = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        List<ListaReproduccion> listaReproduccion = servicioListaRep.obtenerListaReproduccionDelUsuarioLogueado(usuario.getIdUsuario());

        List<Categoria> categorias = servicioNoticia.listarCategorias();

        List<Notificacion> notificaciones = servicioUsuario.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());

        model.put("listaReproduccion",listaReproduccion);
        model.put("categorias",categorias);
        model.put("usuario",usuario);
        model.put("notificaciones", notificaciones.size());

        return new ModelAndView("listasReproduccion",model);
    }

    @RequestMapping(value = "/listaReproduccion/agregarNoticia", method = RequestMethod.POST)
    public ModelAndView agregarNoticiaALista(@RequestParam("noticiaAgregar") Long idNoticia, HttpSession session){
        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        Noticia noticia = servicioNoticia.buscarNoticiaPorId(idNoticia);
        ListaReproduccion lista = new ListaReproduccion(usuario,noticia);
        servicioListaRep.agregarNoticiaALista(lista);
        return new ModelAndView("redirect:/listaReproduccion");
    }

}
