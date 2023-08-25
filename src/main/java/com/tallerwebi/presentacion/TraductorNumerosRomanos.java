package com.tallerwebi.presentacion;

public class TraductorNumerosRomanos {


    public int traducirAArabigos(String numeroRomano){

        int resultado = 0;

        switch (numeroRomano){
            case "I":
                resultado = 1;
                break;

            case "X":
                resultado = 10;
                break;

            case "V":
                resultado = 5;
                break;
        }

        return resultado;
    }
}