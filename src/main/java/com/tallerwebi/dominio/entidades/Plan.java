package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlan;
    private String descripcion;
    private Double precio;
    private Integer cantidadUsuarioDisponibles;
}
