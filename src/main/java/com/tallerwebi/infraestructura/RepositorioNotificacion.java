package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Notificacion;

import java.util.List;

public interface RepositorioNotificacion {
    void generarNotificacion(Notificacion notificacion);

    List<Notificacion> obtenerTodasLasNotificaciones();
}
