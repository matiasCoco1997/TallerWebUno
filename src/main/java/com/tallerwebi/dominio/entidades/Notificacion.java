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

    private String nombreUsuarioSeguido;

    private String tituloNoticia;

    private String descripcion;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaDeCreacion;

    public Notificacion() {

    }

    @PrePersist// Esto generararia automáticamente la fecha de creación antes que se persista
    protected void onCreate() {
        fechaDeCreacion = LocalDate.now();
    }

    public Notificacion(Usuario usuarioNotificado, String nombreUsuarioSeguido, String tituloNoticia) {
        this.usuarioNotificado = usuarioNotificado;
        this.nombreUsuarioSeguido = nombreUsuarioSeguido;
        this.tituloNoticia = tituloNoticia;
        this.descripcion=nombreUsuarioSeguido+ " subió una noticia: "+ tituloNoticia;
    }
}
