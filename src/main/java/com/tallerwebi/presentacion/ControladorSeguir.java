package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class ControladorSeguir {
    private ServicioUsuario servicioUsuario;
    @Autowired
    public ControladorSeguir(ServicioUsuario servicioUsuario){
        this.servicioUsuario = servicioUsuario;
    }
    @PostMapping("/seguir")
    public ResponseEntity<Void> seguir(@RequestParam("id")Long idUsuarioASeguir, HttpSession session) {
        if(idUsuarioASeguir == null || idUsuarioASeguir <= 0){
            return ResponseEntity.badRequest().build();
        }

        Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");

        if(usuarioLogueado == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            Usuario usuarioSeguir = servicioUsuario.obtenerUsuarioPorId(idUsuarioASeguir);
            if(idUsuarioASeguir.equals(usuarioLogueado.getIdUsuario())){
                return ResponseEntity.badRequest().build();
            }
            servicioUsuario.agregarSeguido(usuarioLogueado, usuarioSeguir);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/dejar-de-seguir")
    public ResponseEntity<Void> dejarDeSeguir(@RequestParam("id")Long idUsuarioSeguiendo, HttpSession session) {
        Usuario usuarioLogueado = (Usuario) session.getAttribute("sessionUsuarioLogueado");
        if(usuarioLogueado == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(idUsuarioSeguiendo == null || idUsuarioSeguiendo <= 0){
            return ResponseEntity.badRequest().build();
        }
        try {
            Usuario usuarioSeguido = servicioUsuario.obtenerUsuarioPorId(idUsuarioSeguiendo);
            servicioUsuario.dejarDeSeguirUsuario(usuarioSeguido.getIdUsuario(), usuarioLogueado.getIdUsuario());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }


    }
}
