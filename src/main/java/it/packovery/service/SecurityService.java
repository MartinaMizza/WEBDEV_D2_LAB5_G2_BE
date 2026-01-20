package it.packovery.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import org.jboss.logging.Logger;

@ApplicationScoped
public class SecurityService {
    private static final Logger LOG = Logger.getLogger(SecurityService.class);
    private final PolicyFactory strictPolicy = new HtmlPolicyBuilder().toFactory();

    public String sanitize(String input) {
        if (input == null) return null;

        String sanitized = strictPolicy.sanitize(input);

        if (!input.equals(sanitized)) {
            LOG.warnf("Rilevato tentativo di XSS o HTML non autorizzato: %s", input);
        }

        return sanitized;
    }
}
