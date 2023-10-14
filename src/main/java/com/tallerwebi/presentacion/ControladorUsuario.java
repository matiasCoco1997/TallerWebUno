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

    @RequestMapping("/perfil")
    public ModelAndView perfil(@RequestParam(value = "idUsuario",required = false)Long id, HttpSession session){
        ModelMap model=new ModelMap();
        List<Categoria> categorias=servicioUsuario.obtenerCategorias();
        model.put("categorias",categorias);
        try{
            Usuario usuario= (id!=null) ? servicioUsuario.obtenerUsuarioPorId(id) : (Usuario) session.getAttribute("sessionUsuarioLogueado");
            List<Noticia> noticiasDelUsuario= servicioUsuario.obtenerNoticiasDeUnUsuario(usuario.getIdUsuario());
            model.put("usuario",usuario);
            model.put("noticias",noticiasDelUsuario);
            if(usuario.getDescripcion()==null){
                String descripcionError="No tiene una descripción.";
                model.put("descripcionError",descripcionError);
            }
        }catch (Exception e){
            model.put("errorUsuario","No existe un usuario con ese ID");
            return new ModelAndView("usuarioNoEncontrado",model);
        }
        return new ModelAndView("perfil",model);
    }


}
