package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.NoticiaInexistente;
import com.tallerwebi.dominio.excepcion.UsuarioDeslogueado;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;
    private final ServicioNoticia servicioNoticia;

    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario, ServicioNoticia servicioNoticia) {
        this.servicioUsuario = servicioUsuario;
        this.servicioNoticia = servicioNoticia;
    }

    @RequestMapping(value = "/perfil", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView perfil(@RequestParam(value = "idUsuario", required = false) Long id, HttpSession session) {

        ModelMap model = new ModelMap();

        try {

            Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");

            Usuario usuarioBuscado = (servicioUsuario.verificarSiElIDEsNull(id)) ? usuarioLogueado : servicioUsuario.obtenerUsuarioPorId(id);

            List<Noticia> noticiasDelUsuario = servicioNoticia.obtenerNoticiasDeUnUsuario(usuarioBuscado.getIdUsuario());

            noticiasDelUsuario = servicioNoticia.setNoticiasLikeadas(noticiasDelUsuario, usuarioLogueado.getIdUsuario());

            Map<String, Integer> datosSeguidos = servicioUsuario.obtenerMisSeguidoresYSeguidos(usuarioBuscado.getIdUsuario());

            List<Notificacion> notificaciones = servicioUsuario.obtenerMisNotificacionesSinLeer(usuarioBuscado.getIdUsuario());



            model.put("usuarioLogueado", usuarioLogueado);
            model.put("usuarioBuscado", usuarioBuscado);
            model.put("noticias", noticiasDelUsuario);
            model.put("datosSeguidos", datosSeguidos);
            model.put("cantidadNotificaciones", notificaciones.size());
            model.put("misCompartidos", false);

            if (servicioUsuario.verificarSiLaDescripcionEsNull(usuarioBuscado.getDescripcion())) {
                model.put("descripcionError", "No tiene una descripción.");
            }

        } catch (Exception e) {
            model.put("errorUsuario", "No existe un usuario con ese ID");
            return new ModelAndView("usuarioNoEncontrado", model);
        }
        return new ModelAndView("perfil", model);
    }

    @RequestMapping(value = "/perfil/borradores", method = RequestMethod.GET)
    public ModelAndView verNoticiasEnEstadoBorrador(@RequestParam(value = "idUsuario", required = false) Long id, HttpSession session) {

        ModelMap model = new ModelMap();

        try {

            Usuario usuarioBuscado = (servicioUsuario.verificarSiElIDEsNull(id)) ? (Usuario) session.getAttribute("sessionUsuarioLogueado") : servicioUsuario.obtenerUsuarioPorId(id);

            Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");

            List<Noticia> noticiasDelUsuario = servicioUsuario.obtenerNoticiasDeUnUsuarioEnEstadoBorrador(usuarioBuscado.getIdUsuario());

            noticiasDelUsuario = servicioNoticia.setNoticiasLikeadas(noticiasDelUsuario, usuarioLogueado.getIdUsuario());

            Map<String, Integer> datosSeguidos = servicioUsuario.obtenerMisSeguidoresYSeguidos(usuarioBuscado.getIdUsuario());

            List<Notificacion> notificaciones = servicioUsuario.obtenerMisNotificacionesSinLeer(usuarioLogueado.getIdUsuario());

            model.put("usuarioBuscado", usuarioBuscado);
            model.put("usuarioLogueado", usuarioLogueado);
            model.put("noticias", noticiasDelUsuario);
            model.put("datosSeguidos", datosSeguidos);
            model.put("cantidadNotificaciones", notificaciones.size());
            model.put("misCompartidos", false);

            if (servicioUsuario.verificarSiLaDescripcionEsNull(usuarioBuscado.getDescripcion())) {
                model.put("descripcionError", "No tiene una descripción.");
            }

        } catch (Exception e) {
            model.put("errorUsuario", "No existe un usuario con ese ID");
            return new ModelAndView("usuarioNoEncontrado", model);
        }
        return new ModelAndView("perfil", model);
    }

    @RequestMapping("/notificaciones")
    public ModelAndView notificaciones(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Notificacion> notificaciones = servicioUsuario.obtenerMisNotificaciones(usuario.getIdUsuario());
        servicioUsuario.marcarNotificacionesComoLeidas(usuario.getIdUsuario());
        List<Notificacion> notificacionesSinLeer = servicioUsuario.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());
        model.put("notificaciones", notificaciones);
        model.put("cantidadNotificaciones", notificacionesSinLeer.size());
        model.put("usuario", usuario);
        return new ModelAndView("notificaciones", model);
    }


    @RequestMapping(path = "/usuario/borrar", method = RequestMethod.DELETE)
    public ModelAndView borrarUsuario(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        servicioUsuario.borrarUsuario(usuario.getIdUsuario());
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/perfil/misLikes")
    public ModelAndView verLikes(HttpSession session) {
        ModelMap model = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        model.put("usuarioLogueado", usuario);

        List<Noticia> noticiasDelUsuarioLikeadas = servicioUsuario.obtenerNoticiasLikeadas(usuario.getIdUsuario());

        Map<String, Integer> datosSeguidos = servicioUsuario.obtenerMisSeguidoresYSeguidos(usuario.getIdUsuario());

        List<Notificacion> notificaciones = servicioUsuario.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());

        model.put("noticias", noticiasDelUsuarioLikeadas);
        model.put("datosSeguidos", datosSeguidos);
        model.put("cantidadNotificaciones", notificaciones.size());
        model.put("usuarioLogueado", usuario);
        model.put("usuarioBuscado", usuario);
        model.put("misCompartidos", false);

        return new ModelAndView("perfil", model);
    }

    @RequestMapping(value = "/perfil/misCompartidos", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView verHistorialCompartidos(HttpSession session, @RequestParam(defaultValue = "0") String idUsuarioBuscado) {

        ModelMap model = new ModelMap();

        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        Map<String, Integer> datosSeguidos = servicioUsuario.obtenerMisSeguidoresYSeguidos(usuario.getIdUsuario());

        List<Notificacion> notificaciones = servicioUsuario.obtenerMisNotificacionesSinLeer(usuario.getIdUsuario());

        List<Usuario> usuariosSeguidos = servicioUsuario.listarUsuarioseguidos(usuario.getIdUsuario());

        List<Notificacion> noticiasCompartidas = servicioUsuario.obtenerMisNoticiasCompartidas(usuario.getIdUsuario(), idUsuarioBuscado);

        model.put("usuarioLogueado", usuario);
        model.put("usuarioBuscado", usuario);
        model.put("datosSeguidos", datosSeguidos);
        model.put("cantidadNotificaciones", notificaciones.size());
        model.put("usuariosSeguidos", usuariosSeguidos);
        model.put("listaDeCompartidos", noticiasCompartidas);
        model.put("misCompartidos", true);

        return new ModelAndView("perfil", model);
    }

    @GetMapping(value = "/perfil/modificar")
    public ModelAndView mostrarFormularioModificar(HttpSession session) {
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        model.put("edicion", true);
        model.put("usuario", usuario);

        return new ModelAndView("modificar-usuario", model);
    }

    @RequestMapping(value = "/perfil/modificar", method = RequestMethod.POST)
    public ModelAndView modificarUsuario(@ModelAttribute("usuario") Usuario usuario, HttpSession session, @RequestParam("imagenFile") MultipartFile imagen) {

        ModelMap modelo = new ModelMap();
        try {
            servicioUsuario.modificarDatosUsuario(usuario, imagen);
            session.setAttribute("sessionUsuarioLogueado", usuario);
        } catch (FormatoDeImagenIncorrecto e) {
            modelo.put("error", "El formato de imagen es incorrecto.");
            return new ModelAndView("modificar-usuario", modelo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ModelAndView("redirect:/perfil");
    }

}
