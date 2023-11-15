package com.tallerwebi.dominio.servicios;

import com.mercadopago.client.preference.PreferenceItemRequest;


import com.tallerwebi.dominio.entidades.Plan;
import com.tallerwebi.dominio.entidades.Usuario;
//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

public class ServicioMercadoPagoImpl implements ServicioMercadoPago{
    @Value("${mercadopago.clientId}")
    private String clientId;
    @Value("${mercadopago.clientSecret}")
    private String clientSecret;

    @Override
    public PreferenceItemRequest crearPedido(Usuario usuario, Plan plan) {
        return null;
    }

}
