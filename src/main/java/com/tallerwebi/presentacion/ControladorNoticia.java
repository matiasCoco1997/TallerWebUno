package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.CampoVacio;
import com.tallerwebi.dominio.excepcion.CategoriaInexistente;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import com.tallerwebi.dominio.servicios.ServicioNoticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorNoticia {
    private ServicioNoticia servicioNoticia;
    @Autowired
    public ControladorNoticia(ServicioNoticia servicioNoticia) {
        this.servicioNoticia = servicioNoticia;
    }

    /*
    @RequestMapping(path = {"/noticia/listar", "/noticia","/noticia/"})
    public ModelAndView listarNoticias(HttpSession session) {
        ModelMap modelo = new ModelMap();

        try{
            Usuario UsuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");

            modelo.put("sessionUsuarioLogueado", UsuarioLogueado);

            List<Noticia> noticias = servicioNoticia.listarNoticias();

            modelo.put("noticias", noticias);

        } catch (Exception e) {
            modelo.put("error", "Error al listar las noticias.");
            modelo.put("noticias", modelo);
        }

        return new ModelAndView("home", modelo);
    }

     */

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
            Usuario UsuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
            modelo.put("sessionUsuarioLogueado", UsuarioLogueado);
            servicioNoticia.crearNoticia(noticia, UsuarioLogueado, imagen, audio);
        }catch (CampoVacio e) {
            modelo.put("error", "Error, para crear la nota debe completar todos los campos.");
            return new ModelAndView("crear_noticia", modelo);
        }catch (TamanioDeArchivoSuperiorALoPermitido e) {
            modelo.put("error", "Error, la imagen seleccionada pesa demasiado.");
            return new ModelAndView("crear_noticia", modelo);
        }catch (FormatoDeImagenIncorrecto e) {
            modelo.put("error", "Error, el formato de la imagen no esta permitido.");
            return new ModelAndView("crear_noticia", modelo);
        }
        catch (Exception e) {
            modelo.put("error", "Error al crear la noticia.");
            return new ModelAndView("crear_noticia", modelo);
        }

        return new ModelAndView("redirect:/home" , modelo);
    }

    @RequestMapping(path = "/noticia/borrar", method = RequestMethod.DELETE)
    public ModelAndView borrarNoticiaPorId( @ModelAttribute("datosNoticia") Long idNoticia ) {

        ModelMap modelo = new ModelMap();

        try{
            servicioNoticia.borrarNoticiaPorId(idNoticia);
        } catch (Exception e) {
            modelo.put("error", "Error al borrar la noticia.");
            return new ModelAndView("error", modelo);
        }

        return new ModelAndView("home", modelo);
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

    /*
    @RequestMapping(path = "/noticia/buscarNoticiaPorCategoria", method = RequestMethod.POST)
    public ModelAndView buscarNoticiaPorCategoria( @ModelAttribute("datosNoticia") String categoria ) {

        ModelMap model = new ModelMap();

        try{
            servicioNoticia.buscarNoticiaPorCategoria(categoria);
        }  catch (Exception e) {
            model.put("error", "Error al buscar noticia por categoria.");
            return new ModelAndView("error", model);
        }
        return new ModelAndView("home", model);
    }

     */
    /*
    @RequestMapping("/noticia/login")
    public ModelAndView cerrarSesion() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("redirect:/login", modelo);
    }
    
     */

    @RequestMapping(value = "/darLike",method = RequestMethod.POST)
    public ModelAndView darLike(@RequestParam("noticiaLike") Long noticiaLike,HttpSession session) throws Exception {
        ModelMap modelo = new ModelMap();
        Noticia noticia=servicioNoticia.buscarNoticiaPorId(noticiaLike);
        if (servicioNoticia.verificarQueNoEsNull(noticia)) {
            throw new Exception("La noticia fue eliminada");
        }
        try{
            servicioNoticia.darMeGusta(noticia);
            modelo.addAttribute("meGusta", noticia.getLikes());
        }catch (Exception e){
            modelo.put("error", "No se puede dar me gusta a la noticia");
            return new ModelAndView("redirect:/home", modelo);
        }
        return new ModelAndView("redirect:/home", modelo);
    }
}
