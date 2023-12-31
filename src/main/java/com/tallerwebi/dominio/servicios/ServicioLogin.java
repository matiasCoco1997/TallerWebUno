package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.excepcion.CampoVacio;
import com.tallerwebi.dominio.excepcion.FormatoDeImagenIncorrecto;
import com.tallerwebi.dominio.excepcion.TamanioDeArchivoSuperiorALoPermitido;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);

    void registrar(Usuario usuario, MultipartFile imagen) throws UsuarioExistente, CampoVacio, IOException, FormatoDeImagenIncorrecto, TamanioDeArchivoSuperiorALoPermitido;

}
