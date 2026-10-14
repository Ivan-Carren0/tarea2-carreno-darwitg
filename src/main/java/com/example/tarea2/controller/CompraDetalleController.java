package com.example.tarea2.controller;

import com.example.tarea2.model.CompraDetalleResponse;
import com.example.tarea2.service.CompraDetalleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompraDetalleController {

    private final CompraDetalleService servicio;

    public CompraDetalleController(CompraDetalleService servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/compras/detalle/{id}")
    public CompraDetalleResponse detalle(@PathVariable long id) {
        return servicio.detalle(id);
    }
}