package com.example.tarea2.model;

public class CompraConvertida {

    private Long id;
    private String producto;
    private double monto;
    private String fecha;
    private double valor_unidad;
    private long monto_pesos;

    public CompraConvertida(Long id, String producto, double monto, String fecha,
                            double valor_unidad, long monto_pesos) {
        this.id = id;
        this.producto = producto;
        this.monto = monto;
        this.fecha = fecha;
        this.valor_unidad = valor_unidad;
        this.monto_pesos = monto_pesos;
    }

    public Long getId() { return id; }

    public String getProducto() { return producto; }

    public double getMonto() { return monto; }

    public String getFecha() { return fecha; }

    public double getValor_unidad() { return valor_unidad; }

    public long getMonto_pesos() { return monto_pesos; }
}