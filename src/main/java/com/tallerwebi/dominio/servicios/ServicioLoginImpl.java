package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.excepcion.CampoVacio;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private RepositorioUsuario repositorioLogin;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario servicioLoginDao){
        this.repositorioLogin = servicioLoginDao;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return repositorioLogin.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario, MultipartFile imagen) throws UsuarioExistente, CampoVacio {

        if(imagen.isEmpty()){
            throw new CampoVacio();
        }

        /*
        if(     usuario.getNombre().isEmpty() ||
                usuario.getApellido().isEmpty() ||
                usuario.getEmail().isEmpty() ||
                usuario.getPassword().isEmpty() ||
                usuario.getPais().isEmpty() ||
                usuario.getCiudad().isEmpty() ||
                usuario.getFechaDeNacimiento() == null
        ){

            throw new CampoVacio();
        }



        Usuario usuarioEncontrado = repositorioLogin.consultarMailExistente(usuario.getEmail());

        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }

        repositorioLogin.guardar(usuario);

         */
    }

}

