package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Seguidos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Usuario idUsuarioPropio;
    @ManyToOne
    private Usuario idUsuarioSeguidor;
}
