package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Notificacion;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioNotificacionTest {
    @Autowired
    private RepositorioNotificacion repositorioNotificacion;

    @Transactional
    @Rollback
    @Test
    public void queSeGuardenLasNotificaciones(){
        repositorioNotificacion.generarNotificacion(new Notificacion());
        Integer size= repositorioNotificacion.obtenerTodasLasNotificaciones().size();
        assertThat(size,is(1));
    }
}
