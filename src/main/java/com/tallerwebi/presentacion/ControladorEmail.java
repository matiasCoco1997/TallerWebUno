package com.tallerwebi.presentacion;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@RestController
public class ControladorEmail {
    private final JavaMailSender javaMailSender;

    public ControladorEmail(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/enviarCorreo")
    public String enviarCorreo() {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Configura los detalles del correo
            helper.setFrom("noticierosunn@outlook.com");
            helper.setTo("biez591@gmail.com");
            helper.setSubject("Saludo desde Spring MVC");
            helper.setText("Hola, este es un mensaje de prueba desde Spring MVC.");

            // Envia el correo
            javaMailSender.send(message);

            return "Correo enviado con éxito.";
        } catch (MessagingException e) {
            return "Error al enviar el correo: " + e.getMessage();
        }
    }
}
