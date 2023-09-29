package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.util.ArrayList;

@Entity
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String pais;
    private String ciudad;
    private Date fechaDeNacimiento;
    private String email;
    private String password;
    private String fotoPerfil; //falta
    //private String rol;
    private Boolean activo = false;
    private Integer idPlan; //Relacionado a la tabla plan, idPlan
    private Integer cantidadSeguidores;
    private ArrayList<Usuario> seguidos;

}
