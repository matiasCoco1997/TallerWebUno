package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.*;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.servicios.ServicioComentario;
import com.tallerwebi.dominio.servicios.ServicioEmail;
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
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ControladorNoticia {
    private ServicioNoticia servicioNoticia;
    private ServicioComentario servicioComentario;
    private ServicioUsuario servicioUsuario;
    private ServicioEmail servicioEmail;
    @Autowired
    public ControladorNoticia(ServicioNoticia servicioNoticia, ServicioComentario servicioComentario, ServicioUsuario servicioUsuario, ServicioEmail servicioEmail ) {
        this.servicioNoticia = servicioNoticia;
        this.servicioComentario = servicioComentario;
        this.servicioUsuario = servicioUsuario;
        this.servicioEmail = servicioEmail;
    }

    @RequestMapping(path = "/noticia/crear", method = RequestMethod.GET)
    public ModelAndView cargarNoticia() {
        ModelMap modelo = new ModelMap();

        modelo.put("datosNoticia", new Noticia());
        modelo.put("categorias", servicioNoticia.listarCategorias());

        return new ModelAndView("crear_noticia", modelo);
    }

    @RequestMapping(path = "/noticia/crear", method = RequestMethod.POST)
    public ModelAndView crearNuevaNoticia(@ModelAttribute("datosNoticia") Noticia noticia , HttpSession session, @RequestParam("imagenFile") MultipartFile imagen, @RequestParam("audioFile") MultipartFile audio){
        ModelMap modelo = new ModelMap();
        try{
            Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");

            servicioNoticia.crearNoticia(noticia, usuarioLogueado, imagen, audio);

            servicioNoticia.generarNotificacion(usuarioLogueado.getIdUsuario(),usuarioLogueado.getNombre(),noticia.getTitulo(),noticia);

            modelo.put("sessionUsuarioLogueado", usuarioLogueado);

        }catch (CampoVacio e) {
            modelo.put("error", "Error, para crear la nota debe completar todos los campos.");
            return new ModelAndView("crear_noticia", modelo);

        }catch (TamanioDeArchivoSuperiorALoPermitido e) {
            modelo.put("error", "Error, El archivo seleccionado es demasiado pesado.");
            return new ModelAndView("crear_noticia", modelo);

        }catch (FormatoDeImagenIncorrecto e) {
            modelo.put("error", "Error, el formato de la imagen no esta permitido.");
            return new ModelAndView("crear_noticia", modelo);

        }catch (FormatoDeAudioIncorrecto e) {
            modelo.put("error", "Error, el formato del audio no esta permitido, solo es posible un tipo de audio ' .mp3 '.");
            return new ModelAndView("crear_noticia", modelo);

        }catch (Exception e) {
            modelo.put("error", "Error al crear la noticia.");
            return new ModelAndView("crear_noticia", modelo);
        }

        return new ModelAndView("redirect:/home" , modelo);
    }

    @RequestMapping(path = "/noticia/editar/{id}", method = RequestMethod.GET)
    public ModelAndView editarNoticia(@PathVariable(value = "id") Long idNoticia, HttpSession session) {
        ModelMap modelo = new ModelMap();

        Usuario UsuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        if (idNoticia > 0 && UsuarioLogueado != null){

            Noticia noticiaEncontrada = servicioNoticia.buscarNoticiaPorId(idNoticia);

            if(noticiaEncontrada.getUsuario().getIdUsuario() == UsuarioLogueado.getIdUsuario()){
                modelo.put("datosNoticia", noticiaEncontrada);
                modelo.put("categorias", servicioNoticia.listarCategorias());
            }
        }

        return new ModelAndView("editar_noticia", modelo);
    }

    @RequestMapping(path = "/noticia/editar", method = RequestMethod.POST)
    public ModelAndView editarNoticia(@ModelAttribute("datosNoticia") Noticia noticia , HttpSession session, @RequestParam("imagenFile") MultipartFile imagen, @RequestParam("audioFile") MultipartFile audio){
        ModelMap modelo = new ModelMap();
        try{
            Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
            servicioNoticia.editarNoticia(noticia, usuarioLogueado, imagen, audio);
        }catch (CampoVacio e) {
            modelo.put("error", "Error, para crear la nota debe completar todos los campos.");
            return new ModelAndView("editar_noticia", modelo);
        }catch (TamanioDeArchivoSuperiorALoPermitido e) {
            modelo.put("error", "Error, El archivo seleccionado es demasiado pesado.");
            return new ModelAndView("editar_noticia", modelo);
        }catch (FormatoDeImagenIncorrecto e) {
            modelo.put("error", "Error, el formato de la imagen no esta permitido.");
            return new ModelAndView("editar_noticia", modelo);
        }catch (FormatoDeAudioIncorrecto e) {
            modelo.put("error", "Error, el formato del audio no esta permitido, solo es posible un tipo de audio ' .mp3 '.");
            return new ModelAndView("editar_noticia", modelo);
        }catch (Exception e) {
            modelo.put("error", "Error al editar la noticia.");
            return new ModelAndView("editar_noticia", modelo);
        }
        return new ModelAndView("redirect:/home" , modelo);
    }

    @RequestMapping(path = "/noticia/borrar/{idNoticia}", method = {RequestMethod.GET})
    public ModelAndView borrarNoticiaPorId( @PathVariable Long idNoticia , HttpSession session) {

        ModelMap modelo = new ModelMap();

        Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        Noticia noticiaBuscada = servicioNoticia.buscarNoticiaPorId(idNoticia);
        try {
            if (noticiaBuscada.getUsuario().getIdUsuario().equals(usuarioLogueado.getIdUsuario())) {
                servicioNoticia.borrarNoticia(noticiaBuscada);
            }
        } catch (Exception e) {
            modelo.put("error", "Error al borrar la noticia.");
            return new ModelAndView("redirect:/home", modelo);
        } catch (NoticiaInexistente e) {
            modelo.put("error", "La noticia que quiere borrar no existe");
            return new ModelAndView("home-admin", modelo);
        }
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(path = "/noticia/buscarNoticiaPorTitulo", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView buscarNoticiaPorTitulo( @ModelAttribute("datosNoticia") String tituloNoticia ) {

        ModelMap model = new ModelMap();

        try{
            servicioNoticia.buscarNoticiaPorTitulo(tituloNoticia);

        }  catch (Exception e) {
            model.put("error", "Error al buscar noticia.");
            return new ModelAndView("error", model);
        }
        return new ModelAndView("home", model);
    }

    @RequestMapping(value = "/noticia/likear",method = RequestMethod.POST)
    public ResponseEntity<Object> darLike(@RequestParam Long idNoticia , HttpSession session) {

        Boolean resultadoMeGusta;

        try {
            Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
            Noticia noticia = servicioNoticia.buscarNoticiaPorId(idNoticia);
            resultadoMeGusta = servicioNoticia.darMeGusta(noticia, usuarioLogueado);
        } catch (NoticiaInexistente e) {
            return ResponseEntity.badRequest().build();
        } catch (UsuarioDeslogueado e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(resultadoMeGusta);
    }

    @GetMapping("/publicacion/{idNoticia}")
    public ModelAndView irApublicacion(@PathVariable Long idNoticia, HttpSession session){

        ModelMap model = new ModelMap();

        Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        Noticia noticia =  servicioNoticia.buscarNoticiaPorId(idNoticia);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        String fechaFormateada = noticia.getFechaDePublicacion().format(formatter);

        List<Comentario> comentarios = servicioComentario.buscarComentarios(idNoticia);

        Comentario comentarioForm = new Comentario();

        List<Notificacion> notificaciones=servicioUsuario.obtenerMisNotificacionesSinLeer(usuarioLogueado.getIdUsuario());

        comentarioForm.setNoticia(noticia);

        if(usuarioLogueado != null)
            model.put("usuarioLogueado", usuarioLogueado);
            model.put("comentarios", comentarios);
            model.put("notificaciones", notificaciones.size());
            model.put("fechaPublicacion", fechaFormateada);
            model.put("noticia", noticia);
            model.put("comentarioForm", comentarioForm);

        return new ModelAndView("noticia", model);
    }

    @RequestMapping(value = "/republicar",method = RequestMethod.POST)
    public ModelAndView republicar(@RequestParam("idNoticiaRepublicar")Long idNoticia, HttpSession session){
        Noticia noticia=servicioNoticia.buscarNoticiaPorId(idNoticia);
        Usuario usuario=(Usuario) session.getAttribute("sessionUsuarioLogueado");
        Republicacion republicacion=new Republicacion(usuario,noticia);

        servicioNoticia.republicarNoticia(republicacion);
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/compartir", method = RequestMethod.POST)
    public ModelAndView compartir(@RequestParam("idNoticiaCompartida")Long idNoticia,@RequestParam("receptor") Long idUsuario, HttpSession session){
        ModelMap model=new ModelMap();
        Noticia noticia=servicioNoticia.buscarNoticiaPorId(idNoticia);
        Usuario receptor= null;
        try {
            receptor = servicioUsuario.obtenerUsuarioPorId(idUsuario);
            Usuario emisor=(Usuario) session.getAttribute("sessionUsuarioLogueado");
            servicioNoticia.compartirNoticia(new Notificacion(emisor,receptor,noticia));
            servicioEmail.compartirNoticiaPorEmail(emisor, receptor, noticia);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ModelAndView("redirect:/home",model);
    }

    @RequestMapping(value = "/eliminar-noticia")
    public ModelAndView listarNoticias(HttpSession session){
        ModelMap model = new ModelMap();
        Usuario usuario = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        List<Noticia> noticias = servicioNoticia.listarNoticiasMasLikeadas();
        model.addAttribute("noticias", noticias);
        model.put("usuario", usuario);
        model.put("noticias", noticias);

        return new ModelAndView("eliminar-noticias-admin", model);

    }
    @RequestMapping(value = "/borrar-Noticia/{idNoticia}", method = RequestMethod.GET)
    public ModelAndView borrarNoticiaAdmin(@PathVariable Long idNoticia, HttpSession session){

        ModelMap model = new ModelMap();
        Usuario admin = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        Noticia noticia = servicioNoticia.buscarNoticiaPorId(idNoticia);

        try{
            if(admin.getRol().equals(Rol.ADMIN)){
                servicioNoticia.borrarNoticia(noticia);
                model.put("usuario", admin);
            }
        } catch (IOException e) {
            model.put("error", "Error al eliminar la noticia");
            return new ModelAndView("/eliminar-noticia", model);
        } catch (NoticiaInexistente e){
            model.put("error", "La noticia que quiere borrar no existe");
            return new ModelAndView("/eliminar-noticia", model);
        }
        return new ModelAndView("redirect:/eliminar-noticia", model);
    }
}


