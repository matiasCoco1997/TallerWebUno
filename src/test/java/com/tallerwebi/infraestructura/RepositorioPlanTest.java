package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Plan;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = { SpringWebTestConfig.class, HibernateTestConfig.class })
public class RepositorioPlanTest {
    @Autowired
    private RepositorioPlan repositorioPlan;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this); // Inicializar los mocks antes de cada prueba
    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedaTraerPlanesYNoTieneNadaTraerUnaListaVacia() {
        List<Plan> planes = repositorioPlan.traerPlanes();

        assertNotNull(planes);
        assertTrue(planes.isEmpty());
    }

    @Transactional
    @Rollback
    @Test
    public void queSePuedaTraerPlanPorIdPlanExistente() {
        Long idPlanExistente = 1L;
        Plan plan = new Plan();
        plan.setIdPlan(idPlanExistente);

        repositorioPlan.guardarPlan(plan);

        Plan planEncontrado = repositorioPlan.traerPlanPorId(idPlanExistente);

        assertNotNull(planEncontrado);
        assertEquals(idPlanExistente, planEncontrado.getIdPlan());
    }


}
