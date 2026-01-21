package it.packovery.web.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import it.packovery.service.SecurityService;
import it.packovery.web.resource.AlertConfigResource;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.io.IOException;

@ApplicationScoped
public class SanitizedStringDeserializer extends JsonDeserializer<String> {
    private static final Logger LOG = Logger.getLogger(AlertConfigResource.class);
    private final SecurityService securityService;

    public SanitizedStringDeserializer(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null) return null;

        String sanitized = securityService.sanitize(value);

        if (!value.equals(sanitized)) {
            LOG.warnf("SECURITY ALERT - XSS Attempt detected and blocked in field. Original: [%s]", value);
        }

        return sanitized;
    }
}