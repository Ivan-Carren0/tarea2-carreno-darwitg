package com.example.tarea2.service;

import com.example.tarea2.model.Compra;
import com.example.tarea2.model.CompraDetalleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CompraDetalleService {

    private static final DateTimeFormatter FORMATO_API =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final CompraService compraService;

    public CompraDetalleService(CompraService compraService) {
        this.compraService = compraService;
    }

    public CompraDetalleResponse detalle(long id) {
        Compra c = compraService.leerCompras().stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe la compra " + id));

        String indicador = c.getUnidad().equalsIgnoreCase("UF") ? "uf" : "dolar";
        String fechaApi = LocalDate.parse(c.getFecha()).format(FORMATO_API);

        double valor = compraService.consultarValor(indicador, fechaApi);
        long pesos = Math.round(c.getMonto() * valor);

        return new CompraDetalleResponse(c.getId(), c.getProducto(), c.getMonto(),
                c.getUnidad(), c.getFecha(), valor, pesos);
    }
}