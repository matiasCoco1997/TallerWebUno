package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Notificacion;

public interface RepositorioNotificacion {
    void generarNotificacion(Notificacion notificacion);
}
