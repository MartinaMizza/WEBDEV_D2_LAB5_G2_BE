package it.packovery.web.logger;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.core.Context;
import org.jboss.logging.MDC;

@Provider
public class SecurityLoggingFilter implements ContainerRequestFilter {

    private final HttpServerRequest request;
    private final GeoService geoService;

    public SecurityLoggingFilter(HttpServerRequest request, GeoService geoService) {
        this.request = request;
        this.geoService = geoService;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {

        // String ipAddress = request.getHeader("X-Forwarded-For");
        String ipAddress = "8.8.8.8";

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.remoteAddress().host();
        }
        else {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        MDC.put("client_ip", ipAddress);

        String coords = geoService.getCoordinates(ipAddress);
        MDC.put("GeoLocation.location", coords);

        MDC.put("http_method", requestContext.getMethod());
        MDC.put("http_path", requestContext.getUriInfo().getPath());
        MDC.put("event_category", "iam");
    }
}
