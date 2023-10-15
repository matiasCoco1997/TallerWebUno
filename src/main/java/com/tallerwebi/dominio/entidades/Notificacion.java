package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNotificacion;
    @ManyToOne
    private Usuario usuarioNotificado;
    @ManyToOne
    private Noticia noticiaNotificada;

    private String nombreUsuarioSeguido;

    private String descripcion;
    private Boolean vista;

    public Notificacion() {

    }

    @PrePersist// Esto generararia automáticamente la fecha de creación antes que se persista
    protected void onCreate() {
        vista = false;
    }

    public Notificacion(Usuario usuarioNotificado, String nombreUsuarioSeguido, Noticia noticia) {
        this.usuarioNotificado = usuarioNotificado;
        this.noticiaNotificada=noticia;
        this.nombreUsuarioSeguido = nombreUsuarioSeguido;
        this.descripcion=nombreUsuarioSeguido+ " subió una noticia: "+ noticia.getTitulo();
    }
}
