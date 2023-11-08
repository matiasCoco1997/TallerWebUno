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

    /*ExecutorService: Permite la ejecución
    asíncrona de tareas sin necesidad de crear hilos de forma manual*/

    /*ExecutorService utiliza un grupo de hilos fijos,
    lo que significa que siempre habrá 5 hilos disponibles
    para ejecutar tareas. Si envías más tareas que la capacidad del grupo,
    estas se pondrán en cola hasta que haya un hilo disponible.
    */
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
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
        executorService.submit(() -> {
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
        });
    }

    @Override
    public void compartirNoticiaPorEmail(Usuario emisor, Usuario receptor, Noticia noticia) throws IOException, MessagingException {

        executorService.submit(() -> {


                context.setVariable("nombreReseptor", receptor.getNombre());
                context.setVariable("nombreEmisor", emisor.getNombre());
                context.setVariable("idNoticia", noticia.getIdNoticia());
                context.setVariable("noticiaTitulo", noticia.getTitulo());
                context.setVariable("noticiaResumen", noticia.getResumen());
            byte[] imagenBytes = new byte[0];
            try {
                imagenBytes = servicioImagen.cargarImagenComoBytes(noticia.getRutaDeimagen());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String contenidoHtml = templateEngine.process("noticiaCompartidaEmail", context);

                MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = null;
            try {
                helper = new MimeMessageHelper(message, true);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
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

        });
    }

}
