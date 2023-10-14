package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
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

@Controller
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario){
        this.servicioUsuario=servicioUsuario;
    }

    @RequestMapping(value = "/perfil",method = {RequestMethod.GET,RequestMethod.POST})
    public ModelAndView perfil(@RequestParam(value = "idUsuario",required = false)Long id, HttpSession session){
        ModelMap model=new ModelMap();
        List<Categoria> categorias=servicioUsuario.obtenerCategorias();
        model.put("categorias",categorias);
        try{

            Usuario usuario= (servicioUsuario.verificarSiElIDEsNull(id)) ?
                                    (Usuario) session.getAttribute("sessionUsuarioLogueado") :
                                            servicioUsuario.obtenerUsuarioPorId(id);
            Usuario usuario2=(Usuario) session.getAttribute("sessionUsuarioLogueado");
            List<Noticia> noticiasDelUsuario= servicioUsuario.obtenerNoticiasDeUnUsuario(usuario.getIdUsuario());
            model.put("usuario",usuario);
            model.put("usuario2",usuario2);
            model.put("noticias",noticiasDelUsuario);

            if(servicioUsuario.verificarSiLaDescripcionEsNull(usuario.getDescripcion())){
                model.put("descripcionError", "No tiene una descripción.");
            }

        }catch (Exception e){
            model.put("errorUsuario","No existe un usuario con ese ID");
            return new ModelAndView("usuarioNoEncontrado",model);
        }
        return new ModelAndView("perfil",model);
    }


}
