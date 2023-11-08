package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioEmail;
import com.tallerwebi.dominio.servicios.ServicioEmailImpl;
import com.tallerwebi.dominio.servicios.ServicioImagen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ServiciosEmailTest {
    private JavaMailSender javaMailSenderMock;
    private ServicioEmail servicioEmail;
    private String emailDestinatario;
    private String nombreDestinatario;
    private ITemplateEngine templateEngine;
    private MimeMessage mensajeMimeMock;
    @Captor //permite capturar argumentos pasados a métodos de objetos simulados durante la ejecución de las pruebas
    private ArgumentCaptor<MimeMessage> captor;
    private Usuario emisorMock;
    private Usuario receptorMock;
    private Noticia noticiaMock;
    private ServicioImagen servicioImagenMock;
    private MimeMessageHelper mimeHelper;

    @BeforeEach
    public void init() {
        nombreDestinatario = "Lando Calrissian";
        emailDestinatario = "usuario@example.com";
        mensajeMimeMock = mock(MimeMessage.class);
        javaMailSenderMock = mock(JavaMailSender.class);
        templateEngine = mock(ITemplateEngine.class);
        servicioImagenMock = mock(ServicioImagen.class);
        servicioEmail = new ServicioEmailImpl(javaMailSenderMock, templateEngine, servicioImagenMock);
        MockitoAnnotations.initMocks(this);//se utiliza para inicializar los campos anotados con @Mock, @Spy y @Captor
        emisorMock = mock(Usuario.class);
        when(emisorMock.getNombre()).thenReturn("NombreEmisor");
        receptorMock = mock(Usuario.class);
        when(receptorMock.getNombre()).thenReturn("NombreReceptor");
        when(receptorMock.getEmail()).thenReturn("receptor@com");
        noticiaMock = mock(Noticia.class);
        when(noticiaMock.getIdNoticia()).thenReturn(1L);
        when(noticiaMock.getTitulo()).thenReturn("Titulo");
        when(noticiaMock.getRutaDeimagen()).thenReturn("Ruta");
        when(noticiaMock.getResumen()).thenReturn("Resumen");
        mimeHelper = mock(MimeMessageHelper.class);
    }

    @Test
    public void queSePuedeMandarUnMensajeDeBienvenidaAUnUsuario() {
        when(templateEngine.process(eq("mensaje"), any(Context.class))).thenReturn("<html>Contenido del correo</html>");
        when(javaMailSenderMock.createMimeMessage()).thenReturn(mensajeMimeMock);

        servicioEmail.enviarCorreoBienvenida(emailDestinatario, nombreDestinatario);

        verify(templateEngine, times(1)).process(eq("mensaje"), any(Context.class));
        verify(javaMailSenderMock, times(1)).send(any(MimeMessage.class));
    }
}

