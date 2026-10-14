package com.example.tarea2.model;

public class Compra {

    private Long id;
    private String producto;
    private double monto;
    private String unidad;
    private String fecha;

    public Compra() {
    }

    public Compra(Long id, String producto, double monto,
                  String unidad, String fecha) {
        this.id = id;
        this.producto = producto;
        this.monto = monto;
        this.unidad = unidad;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public String getProducto() {
        return producto;
    }

    public double getMonto() {
        return monto;
    }

    public String getUnidad() {
        return unidad;
    }

    public String getFecha() {
        return fecha;
    }
}
