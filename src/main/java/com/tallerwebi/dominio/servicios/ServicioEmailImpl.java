package com.tallerwebi.dominio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class ServicioEmailImpl implements ServicioEmail{
    private final JavaMailSender javaMailSender;
    private ITemplateEngine templateEngine;
    @Autowired
    public ServicioEmailImpl(JavaMailSender javaMailSender,  ITemplateEngine templateEngine){
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }
    @Override
    public void enviarCorreoBienvenida(String emailUsuario, String nombreUsuario) {
        Context context = new Context();
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
}
