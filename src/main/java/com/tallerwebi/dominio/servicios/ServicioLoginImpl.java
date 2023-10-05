package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.excepcion.CampoVacio;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void registrar(Usuario usuario) throws UsuarioExistente, CampoVacio {

        /*
        if(     usuario.getNombre().equals("") ||
                usuario.getApellido().equals("") ||
                usuario.getEmail().equals("") ||
                usuario.getPassword().equals("") ||
                usuario.getPais().equals("") ||
                usuario.getCiudad().equals("")||
                usuario.getFechaDeNacimiento().equals("")
        ){

            throw new CampoVacio();
        }

         */
        System.out.println(usuario.getFechaDeNacimiento());
        Usuario usuarioEncontrado = repositorioLogin.consultarMailExistente(usuario.getEmail());

        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }

        repositorioLogin.guardar(usuario);
    }

}

