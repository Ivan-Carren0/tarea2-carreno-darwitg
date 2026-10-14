package com.example.tarea2.service;

import com.example.tarea2.model.Compra;
import com.example.tarea2.model.CompraConvertida;
import com.example.tarea2.model.Respuesta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
public class CompraService {

    private final RestClient restClient = RestClient.create();

    @Value("${CSV_PATH:compras.csv}")
    private String csvPath;

    private final DateTimeFormatter formatoEntrada =
            DateTimeFormatter.ISO_LOCAL_DATE;

    private final DateTimeFormatter formatoApi =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Respuesta obtenerCompras(String unidadSolicitada) {

        if (unidadSolicitada == null) {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Debe indicar una unidad: uf o dolar."
            );
        }

        String unidad = unidadSolicitada.toLowerCase(Locale.ROOT);

        String unidadCsv;
        String indicador;

        if (unidad.equals("uf")) {
            unidadCsv = "UF";
            indicador = "uf";
        } else if (unidad.equals("dolar")) {
            unidadCsv = "USD";
            indicador = "dolar";
        } else {
            throw new ResponseStatusException(
                    BAD_REQUEST,
                    "Unidad no válida. Utilice uf o dolar."
            );
        }

        List<Compra> compras = leerCompras();
        List<CompraConvertida> filtradas = new ArrayList<>();
        long totalPesos = 0;

        for (Compra compra : compras) {

            if (!compra.getUnidad().equalsIgnoreCase(unidadCsv)) {
                continue;
            }

            try {
                LocalDate fecha = LocalDate.parse(
                        compra.getFecha(),
                        formatoEntrada
                );

                String fechaApi = fecha.format(formatoApi);

                double valorUnidad =
                        consultarValor(indicador, fechaApi);

                long montoPesos = Math.round(
                        compra.getMonto() * valorUnidad
                );

                CompraConvertida convertida = new CompraConvertida(
                        compra.getId(),
                        compra.getProducto(),
                        compra.getMonto(),
                        compra.getFecha(),
                        valorUnidad,
                        montoPesos
                );

                filtradas.add(convertida);
                totalPesos += montoPesos;

            } catch (java.time.format.DateTimeParseException e) {
                throw new ResponseStatusException(
                        INTERNAL_SERVER_ERROR,
                        "La fecha de una compra no tiene el formato yyyy-MM-dd."
                );
            }
        }

        return new Respuesta(
                unidadCsv,
                filtradas,
                totalPesos
        );
    }

    public List<Compra> leerCompras() {

        try {
            List<String> lineas = Files.readAllLines(
                    Path.of(csvPath)
            );

            List<Compra> compras = new ArrayList<>();

            for (int i = 1; i < lineas.size(); i++) {

                String linea = lineas.get(i);

                if (linea.isBlank()) {
                    continue;
                }

                String[] datos = linea.split(",", -1);

                if (datos.length != 5) {
                    throw new IllegalArgumentException();
                }

                Compra compra = new Compra(
                        Long.parseLong(datos[0].trim()),
                        datos[1].trim(),
                        Double.parseDouble(datos[2].trim()),
                        datos[3].trim(),
                        datos[4].trim()
                );

                compras.add(compra);
            }

            return compras;

        } catch (IOException | IllegalArgumentException e) {
            throw new ResponseStatusException(
                    INTERNAL_SERVER_ERROR,
                    "No fue posible leer correctamente el archivo CSV."
            );
        }
    }

    public double consultarValor(String indicador, String fecha) {

        String url = "https://mindicador.cl/api/"
                + indicador + "/" + fecha;

        try {
            RespuestaIndicador respuesta = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(RespuestaIndicador.class);

            if (respuesta == null
                    || respuesta.serie() == null
                    || respuesta.serie().isEmpty()) {

                throw new ResponseStatusException(
                        BAD_GATEWAY,
                        "No existe un valor publicado para la fecha "
                                + fecha
                );
            }

            return respuesta.serie().get(0).valor();

        } catch (RestClientException e) {
            throw new ResponseStatusException(
                    BAD_GATEWAY,
                    "No fue posible consultar mindicador.cl."
            );
        }
    }

    public record RespuestaIndicador(List<ValorIndicador> serie) {
    }

    public record ValorIndicador(String fecha, double valor) {
    }
}