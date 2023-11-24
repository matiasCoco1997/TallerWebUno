package com.tallerwebi.dominio.servicios;

import com.tallerwebi.dominio.entidades.Plan;
import com.tallerwebi.infraestructura.RepositorioPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioPlan")
@Transactional
public class ServicioPlanImpl implements ServicioPlan{
    private final RepositorioPlan repositorioPlan;

    @Autowired
    public ServicioPlanImpl(RepositorioPlan repositorioPlan) {
        this.repositorioPlan = repositorioPlan;
    }

    @Override
    public List<Plan> traerPlanes() {
        return repositorioPlan.traerPlanes();
    }

    @Override
    public Plan buscarPlanPorID(Long idPlan) {
        return repositorioPlan.traerPlanPorId(idPlan);
    }
}
