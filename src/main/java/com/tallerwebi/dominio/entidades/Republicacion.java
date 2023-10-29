package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@DiscriminatorValue("republicacion")
public class Republicacion{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_republicador")
    private Usuario id_republicador;
    @ManyToOne()
    @JoinColumn(name = "id_noticiaRepublicada")
    private Noticia id_noticiaRepublicada;
    public Republicacion() {

    }

    public Republicacion(Usuario usuario, Noticia noticia) {
        id_republicador=usuario;
        id_noticiaRepublicada=noticia;
    }
}
