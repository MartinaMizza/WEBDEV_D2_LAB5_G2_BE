package it.packovery.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.packovery.service.model.Route;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

@ApplicationScoped
public class RoutingService {

    private static final String OSRM_URL =
            "https://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=full&geometries=polyline";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Route createRoute(
            double startLongitude,
            double startLatitude,
            double endLongitude,
            double endLatitude
    ) {
        String url = String.format(Locale.US, OSRM_URL, startLongitude, startLatitude, endLongitude, endLatitude);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode route = root.path("routes").get(0);

            String polyline = route.path("geometry").asText();
            double distanceKm = route.path("distance").asDouble() / 1000.0;
            long durationMin = Math.round(route.path("duration").asDouble() / 60.0);

            return new Route(polyline, distanceKm, durationMin);

        }
        catch (Exception e) {
            throw new RuntimeException("Error calculating route with OSMR", e);
        }
    }
}
