package com.tallerwebi.dominio.Usuario;

import com.tallerwebi.dominio.Usuario.Usuario;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void modificar(Usuario usuario);
}
