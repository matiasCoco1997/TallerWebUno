package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidades.Comentario;
import com.tallerwebi.dominio.servicios.ServicioComentario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ControladorComentario {
    private ServicioComentario servicioComentario;
    @Autowired
    public ControladorComentario(ServicioComentario servicioComentario) {
        this.servicioComentario = servicioComentario;
    }

    @PostMapping("/comentario")
    public ResponseEntity<Object> guardarComentario(@RequestParam("idNoticia") Long idPublicacion,
                                                        @ModelAttribute("comentario") Comentario comentario) {
        try {
            comentario.setId(idPublicacion);
            servicioComentario.guardarComentario(comentario);
            return ResponseEntity.ok(comentario); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el comentario mono");
        }
    }

@GetMapping("/comentarios/publicacion/{idPublicacion}")
    public ResponseEntity<Object> listarComentario(@PathVariable Long idPublicacion) {
        try {
            List<Comentario> comentarios =servicioComentario.buscarComentarios(idPublicacion);
            return ResponseEntity.ok(comentarios); // 200 OK
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Devuelve 404 ;
        }
    }

}
