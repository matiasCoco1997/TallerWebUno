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
    @ManyToOne
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "idNoticia")
    private Noticia noticia;
    @Column(nullable = false)
    private String descripcion;
    private LocalDateTime fechaCreacion;// Fecha y hora


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
        if(this.descripcion == null || !(this.descripcion.trim().length() > 0 && this.descripcion.trim().length()<256)){
            throw new ComentarioException("La descripción debe tener entre 1 y 256 caracteres");
        }
    }
}
