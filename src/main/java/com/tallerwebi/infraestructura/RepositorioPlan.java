package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidades.Plan;

import java.util.List;

public interface RepositorioPlan {
    List<Plan> traerPlanes();

    Plan traerPlanPorId(Long idPlan);
}
