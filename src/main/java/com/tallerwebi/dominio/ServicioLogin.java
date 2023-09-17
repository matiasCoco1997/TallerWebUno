package com.tallerwebi.dominio;

import com.tallerwebi.dominio.Usuario.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);
    void registrar(Usuario usuario) throws UsuarioExistente;

}
