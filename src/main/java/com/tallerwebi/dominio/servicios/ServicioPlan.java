package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Plan;

import java.util.List;

public interface ServicioPlan {
    List<Plan> traerPlanes();

    Plan buscarPlanPorID(Long idPlan);
}
