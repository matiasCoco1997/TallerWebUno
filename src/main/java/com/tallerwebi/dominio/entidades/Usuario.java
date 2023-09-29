package com.tallerwebi.dominio.entidades;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    private Boolean activo = false;
    private Integer idPlan; //Relacionado a la tabla plan, idPlan
    private Integer cantidadSeguidores;
    @Transient
    private List<Usuario> seguidos; //revisar
    @Transient
    private List<Usuario> seguidores; //revisar

}
