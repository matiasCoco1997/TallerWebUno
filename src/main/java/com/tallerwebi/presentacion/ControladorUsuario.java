package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario){
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping(value = "/perfil",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView perfil(@RequestParam(value = "idUsuario",required = false)Long id, HttpSession session){
        ModelMap model=new ModelMap();
        List<Categoria> categorias = servicioUsuario.obtenerCategorias();
        model.put("categorias",categorias);
        try{

            Usuario usuarioBuscado = (servicioUsuario.verificarSiElIDEsNull(id)) ? (Usuario) session.getAttribute("sessionUsuarioLogueado") : servicioUsuario.obtenerUsuarioPorId(id);
            Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
            List<Noticia> noticiasDelUsuario = servicioUsuario.obtenerNoticiasDeUnUsuario(usuarioBuscado.getIdUsuario());
            Map<String,Integer> datosSeguidos= servicioUsuario.obtenerMisSeguidoresYSeguidos(usuarioBuscado.getIdUsuario());
            List<Notificacion> notificaciones=servicioUsuario.obtenerMisNotificaciones(usuarioLogueado.getIdUsuario());
            model.put("usuarioBuscado",usuarioBuscado);
            model.put("usuarioLogueado",usuarioLogueado);
            model.put("noticias",noticiasDelUsuario);
            model.put("datosSeguidos",datosSeguidos);
            model.put("cantidadNotificaciones",notificaciones.size());
            if(servicioUsuario.verificarSiLaDescripcionEsNull(usuarioBuscado.getDescripcion())){
                model.put("descripcionError", "No tiene una descripción.");
            }

        }catch (Exception e){
            model.put("errorUsuario","No existe un usuario con ese ID");
            return new ModelAndView("usuarioNoEncontrado",model);
        }
        return new ModelAndView("perfil",model);
    }

    @RequestMapping(value = "/perfil/borradores",method = RequestMethod.GET)
    public ModelAndView verNoticiasEnEstadoBorrador(@RequestParam(value = "idUsuario",required = false)Long id, HttpSession session){
        ModelMap model=new ModelMap();
        List<Categoria> categorias = servicioUsuario.obtenerCategorias();
        model.put("categorias",categorias);
        try{

            Usuario usuarioBuscado = (servicioUsuario.verificarSiElIDEsNull(id)) ? (Usuario) session.getAttribute("sessionUsuarioLogueado") : servicioUsuario.obtenerUsuarioPorId(id);
            Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
            List<Noticia> noticiasDelUsuario = servicioUsuario.obtenerNoticiasDeUnUsuarioEnEstadoBorrador(usuarioBuscado.getIdUsuario());
            model.put("usuarioBuscado",usuarioBuscado);
            model.put("usuarioLogueado",usuarioLogueado);
            model.put("noticias",noticiasDelUsuario);

            if(servicioUsuario.verificarSiLaDescripcionEsNull(usuarioBuscado.getDescripcion())){
                model.put("descripcionError", "No tiene una descripción.");
            }

        }catch (Exception e){
            model.put("errorUsuario","No existe un usuario con ese ID");
            return new ModelAndView("usuarioNoEncontrado",model);
        }
        return new ModelAndView("perfil",model);
    }

    @RequestMapping("/notificaciones")
    public ModelAndView notificaciones(HttpSession session){
        ModelMap model=new ModelMap();
        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Notificacion> notificaciones=servicioUsuario.obtenerMisNotificaciones(usuario.getIdUsuario());
        servicioUsuario.marcarNotificacionesComoLeidas(usuario.getIdUsuario());
        model.put("notificaciones",notificaciones);
        model.put("usuario",usuario);
        return new ModelAndView("notificaciones", model);
    }


}
