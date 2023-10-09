package com.tallerwebi.dominio.entidades;

import com.tallerwebi.dominio.excepcion.ComentarioException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idUsuario;
    private Long idNoticia;
    @Column(nullable = false)
    private String descripcion;
    private LocalDateTime fechaCreacion;// Fecha y hora

   /* public Comentario() {
        this.date = new Date();
    }*/
    @PrePersist// Esto generararia automáticamente la fecha de creación antes que se persista
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    public void validar() throws ComentarioException {
        validarDescripcion();
        //validarIdUsuario();
        //validarIdNoticia();
    }

    private void validarDescripcion() throws ComentarioException {
        if(this.descripcion == null || !(this.descripcion.length() > 0 && this.descripcion.length()<256)){
            throw new ComentarioException("La descripción debe tener entre 1 y 5 caracteres");
        }
    }
}
