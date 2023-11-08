package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;

import javax.mail.MessagingException;
import java.io.IOException;

public interface ServicioEmail {
    void enviarCorreoBienvenida(String emailUsuario, String nombreUsuario);
    void compartirNoticiaPorEmail(Usuario emisor, Usuario receptor, Noticia noticia) throws IOException, MessagingException;
}
