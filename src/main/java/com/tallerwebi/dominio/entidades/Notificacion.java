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

    @ManyToOne
    @JoinColumn(name = "usuarioEmisor")
    private Usuario emisor;

    private String descripcion;
    private Boolean vista;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fecha;

    public Notificacion() {

    }

    public Notificacion(Usuario emisor, Usuario receptor, Noticia noticia) {
        this.emisor=emisor;
        this.usuarioNotificado=receptor;
        this.noticiaNotificada=noticia;
        this.descripcion=emisor.getNombre()+" ha compartido una noticia contigo!";
    }

    @PrePersist// Esto generararia automáticamente la fecha de creación antes que se persista
    protected void onCreate() {
        vista = false;
        fecha = LocalDate.now();

    }

    public Notificacion(Usuario usuarioNotificado, String nombreUsuarioSeguido, Noticia noticia) {
        this.usuarioNotificado = usuarioNotificado;
        this.noticiaNotificada=noticia;
        this.nombreUsuarioSeguido = nombreUsuarioSeguido;
        this.descripcion=nombreUsuarioSeguido+ " subió una noticia: "+ noticia.getTitulo();
    }
}
