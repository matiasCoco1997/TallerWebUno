package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ServicioEmailImpl implements ServicioEmail {
    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;
    private final Context context;
    private ExecutorService executorService = Executors.newFixedThreadPool(5); // Puedes ajustar el número de hilos según tus necesidades
    private ServicioImagen servicioImagen;

    @Autowired
    public ServicioEmailImpl(JavaMailSender javaMailSender, ITemplateEngine templateEngine, ServicioImagen servicioImagen) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.context = new Context();
        this.servicioImagen = servicioImagen;
    }

    @Override
    public void enviarCorreoBienvenida(String emailUsuario, String nombreUsuario) {
        context.setVariable("nombreUsuario", nombreUsuario);

        String contenidoHtml = templateEngine.process("mensaje", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setFrom("noticierosunn@outlook.com");
            helper.setTo(emailUsuario);
            helper.setSubject("Bienvenido a Sunn");
            helper.setText(contenidoHtml, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void compartirNoticiaPorEmail(Usuario emisor, Usuario receptor, Noticia noticia) throws IOException, MessagingException {

        executorService.submit(() -> {
            try {

                context.setVariable("nombreReseptor", receptor.getNombre());
                context.setVariable("nombreEmisor", emisor.getNombre());
                context.setVariable("idNoticia", noticia.getIdNoticia());
                context.setVariable("noticiaTitulo", noticia.getTitulo());
                context.setVariable("noticiaResumen", noticia.getResumen());
                byte[] imagenBytes = servicioImagen.cargarImagenComoBytes(noticia.getRutaDeimagen());

                String contenidoHtml = templateEngine.process("noticiaCompartidaEmail", context);

                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                try {
                    helper.setFrom("noticierosunn@outlook.com");
                    helper.setTo(receptor.getEmail());
                    helper.setSubject("Sunn - Te compartieron una noticia");
                    helper.setText(contenidoHtml, true);
                    helper.addInline("imagenIncrustada", new ByteArrayResource(imagenBytes), "image/png");

                    javaMailSender.send(message);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {

            }
        });
    }

}
