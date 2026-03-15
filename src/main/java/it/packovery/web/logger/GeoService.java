package it.packovery.web.logger;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;

@ApplicationScoped
public class GeoService {
    private DatabaseReader reader;

    @PostConstruct
    void init() throws IOException {
        InputStream dbStream = getClass().getResourceAsStream("/GeoLite2-City.mmdb");
        reader = new DatabaseReader.Builder(dbStream).build();
    }

    public String getCoordinates(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = reader.city(ipAddress);

            return response.getLocation().getLatitude() + "," + response.getLocation().getLongitude();
        }
        catch (Exception e) {
            return "0,0"; // Default o gestisci errore
        }
    }
}
