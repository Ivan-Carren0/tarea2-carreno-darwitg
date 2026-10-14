package com.example.tarea2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CompraDetalleResponse(
        long id,
        String producto,
        double monto,
        String unidad,
        String fecha,
        @JsonProperty("valor_unidad") double valorUnidad,
        @JsonProperty("monto_pesos") long montoPesos
) {}