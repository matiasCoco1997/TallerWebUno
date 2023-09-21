package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorPago {
    @RequestMapping("/planes")
    public ModelAndView irAPagos(HttpServletRequest request){
        String rol = (String) request.getSession().getAttribute("ROL");
        if(rol != "Editor")
            return new ModelAndView("login");
        return new ModelAndView("subcripciones");
    }
}
