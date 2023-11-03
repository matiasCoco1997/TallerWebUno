package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.excepcion.CampoVacio;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import com.tallerwebi.dominio.servicios.ServicioEmail;
import com.tallerwebi.dominio.servicios.ServicioLogin;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;
    private ServicioEmail servicioEmail;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin, ServicioEmail servicioEmail) {
        this.servicioLogin = servicioLogin;
        this.servicioEmail = servicioEmail;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public ModelAndView irALogin() {
        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }


    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {

        ModelMap model = new ModelMap();

        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());

        try {
            if (usuarioBuscado != null) {

                if (usuarioBuscado.getActivo()) {
                    HttpSession session = request.getSession();
                    session.setAttribute("sessionUsuarioLogueado", usuarioBuscado);
                    return new ModelAndView("redirect:/home");
                } else {
                    model.put("error", "Usuario bloqueado.");
                }

            } else {
                model.put("error", "Usuario o clave incorrecta");
            }

        } catch (Exception e) {
            model.put("error", "Ocurrio un error al loguearse.");
        }

        return new ModelAndView("login", model);
    }

    @RequestMapping(path = "/validar-registro", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario, @RequestParam("imagenFile") MultipartFile imagen) {
    //
        ModelMap model = new ModelMap();

        try {
            servicioLogin.registrar(usuario, imagen);
            servicioEmail.enviarCorreoBienvenida(usuario.getEmail(), usuario.getNombre());
        } catch (UsuarioExistente e) {
            model.put("error", "El email ya esta en uso.");
            return new ModelAndView("registro", model);
        } catch (CampoVacio e) {
            model.put("error", "Debe completar todos los campos.");
            return new ModelAndView("registro", model);
        } catch (TamanioDeArchivoSuperiorALoPermitido e) {
            model.put("error", "La imagen ingresada demasiado pesada.");
            return new ModelAndView("registro", model);
        } catch (FormatoDeImagenIncorrecto e) {
            model.put("error", "El tipo de archivo ingresado no esta permitido.");
            return new ModelAndView("registro", model);
        } catch (Exception e) {
            model.put("error", "Error al registrar el nuevo usuario");
            return new ModelAndView("registro", model);
        }

        return new ModelAndView("redirect:/login", model);
    }

    @RequestMapping(path = "/registrarse", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("registro", model);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");

    }

    @RequestMapping(path = "/cerrar-sesion")
    public ModelAndView cerrarSesion(HttpSession session){
        ModelMap modelo = new ModelMap();
        try{
            session.invalidate();
        }catch(Exception e){
            modelo.put("error", "Error al cerrar sesion");
            return new ModelAndView("redirect:/home", modelo);
        }
        return new ModelAndView("redirect:/login");
    }

}

