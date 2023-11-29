package com.tallerwebi.servicios;

import com.tallerwebi.dominio.entidades.Plan;
import com.tallerwebi.dominio.servicios.ServicioPlanImpl;
import com.tallerwebi.infraestructura.RepositorioPlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class ServicioPlanTest {
    private Plan planMock;
    private Plan plan2Mock;
    private RepositorioPlan repositorioPlanMock;
    private ServicioPlanImpl servicioPlan;
    @BeforeEach
    public void init() {
        planMock = mock(Plan.class);
        when(planMock.getIdPlan()).thenReturn(1L);
        plan2Mock = mock(Plan.class);
        repositorioPlanMock = mock(RepositorioPlan.class);
        servicioPlan = new ServicioPlanImpl(repositorioPlanMock);
    }
    @Test
    public void queSePuedaBuscarPlanPorIDExistente() {
        when(repositorioPlanMock.traerPlanPorId(1L)).thenReturn(planMock);

        Plan plan = servicioPlan.buscarPlanPorID(1L);

        assertEquals(planMock, plan);
        verify(repositorioPlanMock, times(1)).traerPlanPorId(planMock.getIdPlan());
    }
    @Test
    public void queSePuedaBuscarPlanPorIDNoExistente() {
        when(repositorioPlanMock.traerPlanPorId(planMock.getIdPlan())).thenReturn(null);

        Plan plan = servicioPlan.buscarPlanPorID(planMock.getIdPlan());

        assertNull(plan);
        verify(repositorioPlanMock, times(1)).traerPlanPorId(planMock.getIdPlan());
    }
    @Test
    public void queSePuedaTraerPlanes() {
        List<Plan> planesMock = new ArrayList<>();
        planesMock.add(planMock);
        planesMock.add(plan2Mock);

        when(repositorioPlanMock.traerPlanes()).thenReturn(planesMock);

        List<Plan> planes = servicioPlan.traerPlanes();

        assertEquals(planesMock.size(), planes.size());
        verify(repositorioPlanMock, times(1)).traerPlanes();
    }
}

