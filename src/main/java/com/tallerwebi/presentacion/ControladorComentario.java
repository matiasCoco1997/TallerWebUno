package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.ComentarioException;
import com.tallerwebi.dominio.servicios.ServicioComentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ControladorComentario {
    private ServicioComentario servicioComentario;
    @Autowired
    public ControladorComentario(ServicioComentario servicioComentario) {
        this.servicioComentario = servicioComentario;
    }
    @PostMapping("/comentario")
    public ModelAndView guardarComentario(@ModelAttribute("comentario") Comentario comentario,
                                          HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        comentario.setIdUsuario(usuarioLogueado.getIdUsuario());
        ModelMap model = new ModelMap();

        try {
            servicioComentario.guardarComentario(comentario);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            String fechaFormateada = comentario.getFechaCreacion().format(formatter);

            model.put("propietario", true);
            model.put("fecha", fechaFormateada);
            model.put("comentario", comentario);
            model.put("usuarioFoto", usuarioLogueado.getFotoPerfil());
            model.put("usuarioNombre", usuarioLogueado.getNombre());
            return new ModelAndView("fragment/comentario-response", model);
        } catch (ComentarioException e) {
            model.put("error", e.getMessage());
            return new ModelAndView("fragment/comentario-response", model);
        }
    }
    @GetMapping("/comentarios/publicacion/{idPublicacion}")
    public ModelAndView listarComentario(@PathVariable Long idPublicacion, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        try {
            List<Comentario> comentarios =servicioComentario.buscarComentarios(idPublicacion);
            ModelMap model = new ModelMap();
            model.put("comentarios", comentarios);
            model.put("usuarioLogueado", usuario.getIdUsuario());
            return new ModelAndView("fragment/comentario-response", model);
        } catch (Exception e) {
            return null;
        }
    }
    @DeleteMapping("/comentario/{idComentario}")
    public ResponseEntity<Object> eliminarComentario(@PathVariable Long idComentario, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        Boolean eliminado = servicioComentario.eliminarComentario(idComentario, usuarioLogueado.getIdUsuario());

        return eliminado ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND); // un "204 No Content" indica que la solicitud se ha procesado correctamente, pero no hay contenido para enviar en la respuesta.
    }
    @GetMapping("/comentario/{idComentario}")
    public ModelAndView enviarFormModificarComentario(@PathVariable Long idComentario, HttpSession session) {
        Comentario comentario = servicioComentario.buscarComentarioPorId(idComentario);
        session.setAttribute("comentarioEnEdicion", comentario);

        ModelMap model = new ModelMap();
        model.put("edicion", true);
        model.put("comentario", comentario);
        return new ModelAndView("fragment/form-comentario", model);
    }

    //El PatchMapping no funciona
    @PostMapping("/comentario/editar")//Patch se utiliza para actualizar parcialmente un recurso
    public ResponseEntity<Object> modificarComentario(@ModelAttribute("comentario") Comentario comentario, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        Comentario comentarioEnEdicion = (Comentario) session.getAttribute("comentarioEnEdicion");

        comentarioEnEdicion.setDescripcion(comentario.getDescripcion());

        try {
            System.out.println(comentario.getDescripcion());
            servicioComentario.modificarComentario(comentarioEnEdicion, usuarioLogueado.getIdUsuario());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ComentarioException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
