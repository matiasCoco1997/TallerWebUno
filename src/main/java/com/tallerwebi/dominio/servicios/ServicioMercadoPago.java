package com.tallerwebi.dominio.servicios;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.tallerwebi.dominio.entidades.Plan;
import com.tallerwebi.dominio.entidades.Usuario;

public interface ServicioMercadoPago {
    PreferenceItemRequest crearPedido(Usuario usuario, Plan plan);
}
