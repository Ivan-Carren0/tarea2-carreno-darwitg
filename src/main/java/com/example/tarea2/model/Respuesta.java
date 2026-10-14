package com.example.tarea2.model;

import java.util.List;

public class Respuesta {

    private String unidad;
    private List<CompraConvertida> compras;
    private long total_pesos;

    public Respuesta(String unidad, List<CompraConvertida> compras, long total_pesos) {
        this.unidad = unidad;
        this.compras = compras;
        this.total_pesos = total_pesos;
    }

    public String getUnidad() { return unidad; }
    public List<CompraConvertida> getCompras() { return compras; }
    public long getTotal_pesos() { return total_pesos; }
}
