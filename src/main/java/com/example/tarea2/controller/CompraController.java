package com.example.tarea2.controller;

import com.example.tarea2.model.Respuesta;
import com.example.tarea2.service.CompraService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping("/{unidad}")
    public Respuesta porUnidad(@PathVariable String unidad) {
        return compraService.obtenerCompras(unidad);
    }
}