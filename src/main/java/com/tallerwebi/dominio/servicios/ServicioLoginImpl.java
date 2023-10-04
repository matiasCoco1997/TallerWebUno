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

    private RepositorioUsuario servicioLoginDao;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario servicioLoginDao){
        this.servicioLoginDao = servicioLoginDao;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return servicioLoginDao.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws UsuarioExistente, CampoVacio {
        Usuario usuarioEncontrado = servicioLoginDao.buscarUsuario(usuario.getEmail(), usuario.getPassword());

        if(usuarioEncontrado != null){
            throw new UsuarioExistente();
        }

        if(usuario.getNombre() == null ){
            throw new CampoVacio();
        }

        //servicioLoginDao.guardar(usuario);
    }

}

