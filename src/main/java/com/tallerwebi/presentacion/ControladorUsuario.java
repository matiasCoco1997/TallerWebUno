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
import org.springframework.web.bind.annotation.*;
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
            List<Notificacion> notificaciones=servicioUsuario.obtenerMisNotificacionesSinLeer(usuarioLogueado.getIdUsuario());
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

            Map<String,Integer> datosSeguidos= servicioUsuario.obtenerMisSeguidoresYSeguidos(usuarioBuscado.getIdUsuario());

            List<Notificacion> notificaciones=servicioUsuario.obtenerMisNotificacionesSinLeer(usuarioLogueado.getIdUsuario());

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

    @RequestMapping("/notificaciones")
    public ModelAndView notificaciones(HttpSession session){
        ModelMap model=new ModelMap();
        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Notificacion> notificaciones=servicioUsuario.obtenerMisNotificaciones(usuario.getIdUsuario());
        List<Notificacion> notificacionesSinLeer=servicioUsuario.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());
        servicioUsuario.marcarNotificacionesComoLeidas(usuario.getIdUsuario());
        model.put("notificaciones",notificaciones);
        model.put("cantidadNotificaciones",notificacionesSinLeer.size());
        model.put("usuario",usuario);
        return new ModelAndView("notificaciones", model);
    }


    @RequestMapping(path = "/usuario/borrar", method = RequestMethod.DELETE)
    public ModelAndView borrarUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        servicioUsuario.borrarUsuario(usuario.getIdUsuario());
        return new ModelAndView("redirect:/login");
    }

    @GetMapping(value = "/perfil/modificar")
    public ModelAndView mostrarFormularioModificar( HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        model.put("edicion", true);
        model.put("usuario", usuario);

        return new ModelAndView("/modificar", model); //formulario con los datos del usuario para editar
    }

    @RequestMapping(value = "/perfil/modificar", method = RequestMethod.POST)
    public ModelAndView modificarUsuario(@ModelAttribute("usuario") Usuario usuario, HttpSession session) {
        Usuario usuarioEditar = (Usuario) session.getAttribute("UsuarioAEditar");

        try{
            servicioUsuario.modificarDatosUsuario(usuario);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ModelAndView("redirect:/perfil/" + usuarioEditar.getIdUsuario());
    }

}
