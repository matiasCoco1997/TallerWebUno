package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.servicios.ServicioEmail;
import freemarker.core.ParseException;
import freemarker.template.*;
import io.opencensus.resource.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ControladorEmail {
    private final ServicioEmail servicioEmail;
    public ControladorEmail(ServicioEmail servicioEmail) {
        this.servicioEmail = servicioEmail;
    }

    @GetMapping("/enviarCorreo")
    public String enviarCorreo() {
        String nombreUsuario = "Lando Calrissian";
        String email = "";
        servicioEmail.enviarCorreoBienvenida(email, nombreUsuario);
        return "Correo enviado";
    }

}
