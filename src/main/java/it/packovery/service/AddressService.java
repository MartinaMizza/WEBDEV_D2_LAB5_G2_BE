package it.packovery.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

@ApplicationScoped
public class AddressService {

    private static final String NOMINATIM_URL =
            "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getAddress(String cityAndProvinceCode, double latitude, double longitude) {
        String url = String.format(Locale.US, NOMINATIM_URL, latitude, longitude);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "QuarkusRoutingApp")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode address = root.path("address");

            String road = address.path("road").asText();
            String houseNumber = address.path("house_number").asText();
            String postalCode = address.path("postcode").asText();

            String[] splitCityAndProvinceCode = cityAndProvinceCode.split(",");
            String city = splitCityAndProvinceCode[0];
            String province = splitCityAndProvinceCode[1];

            return road + " " + houseNumber + ", " + city + ", " + postalCode + ", " + province;
        }
        catch (Exception e) {
            throw new RuntimeException("Error retrieving address from Nominatim", e);
        }
    }
}
