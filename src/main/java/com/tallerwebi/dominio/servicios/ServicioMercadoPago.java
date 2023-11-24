package com.tallerwebi.dominio.servicios;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.tallerwebi.dominio.entidades.Plan;
import com.tallerwebi.dominio.entidades.Usuario;

import java.util.Map;

public interface ServicioMercadoPago {
    Map crearPago(Usuario usuario, Plan plan) throws MPException, MPApiException;
}
