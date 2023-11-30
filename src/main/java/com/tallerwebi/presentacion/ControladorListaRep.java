package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.servicios.ServicioListaRep;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
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

        if(usuario != null){
            if(usuario.getRol() == (Rol.ADMIN)){
                return new ModelAndView("redirect:/home");
            }
        }else{
            return new ModelAndView("redirect:/login");
        }

        List<ListaReproduccion> listaReproduccion = servicioListaRep.obtenerListaReproduccionDelUsuarioLogueado(usuario.getIdUsuario());


        List<Notificacion> notificaciones = servicioUsuario.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());

        model.put("listaReproduccion",listaReproduccion);
        model.put("usuario",usuario);
        model.put("notificaciones", notificaciones.size());

        return new ModelAndView("playlist",model);
    }

    @RequestMapping(value = "/listaReproduccion/agregarNoticia", method = RequestMethod.POST)
    public ResponseEntity<String> agregarNoticiaALista(@RequestParam("noticiaAgregar") Long idNoticia, HttpSession session){
        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        Noticia noticia =   servicioNoticia.buscarNoticiaPorId(idNoticia);
        ListaReproduccion lista = new ListaReproduccion(usuario,noticia);
        servicioListaRep.agregarNoticiaALista(lista);
        return ResponseEntity.ok("Noticia agregada correctamente!");
    }
    @RequestMapping(value = "/listaReproduccion/eliminarNoticia", method = RequestMethod.POST)
    public ResponseEntity<String> eliminarNoticiaDeLista(@RequestParam("noticiaEliminar") Long idNoticia, HttpSession session){
        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        ListaReproduccion lista = null;
        try {
            lista = servicioListaRep.buscarListaReproduccion(idNoticia,usuario.getIdUsuario());
        } catch (Exception e) {
            return ResponseEntity.ok("No se encontró la noticia!!");
        }
        try {
            servicioListaRep.eliminarNoticiaDeLista(lista);
        } catch (Exception e) {
            return ResponseEntity.ok("No se pudo eliminar!");
        }
        return ResponseEntity.ok("Noticia eliminada correctamente!");
    }
    @GetMapping("/listaReproduccion/{id}")
    public ResponseEntity<Object> obtenerAudioDeLista(HttpSession session, @PathVariable Long id){
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        if(usuario != null){
            if(usuario.getRol() == (Rol.ADMIN)){
                return null;
            }
        }

        List<ListaReproduccion> listaReproduccion = servicioListaRep.obtenerListaReproduccionDelUsuarioLogueado(usuario.getIdUsuario());
        for (ListaReproduccion listaNoticia: listaReproduccion){
            if(listaNoticia.getNoticia().getIdNoticia().equals(id)){
                Noticia noticia = listaNoticia.getNoticia();
                return ResponseEntity.ok(noticia);
            }
        }

        return null;
    }
}
