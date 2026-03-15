package it.packovery.web.resource;

import io.smallrye.jwt.build.Jwt;
import it.packovery.service.LoggingService;
import it.packovery.service.LoginService;
import it.packovery.web.model.AccessTokenResponse;
import it.packovery.web.model.LoginRequest;
import it.packovery.web.model.LoginResponse;
import it.packovery.web.model.TokenResponse;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@DenyAll
@Path("/api/auth")
public class LoginResource {

    private static final Logger LOG = Logger.getLogger(LoginResource.class);
    private final LoginService loginService;
    private final LoggingService loggingService;

    public LoginResource(LoginService loginService, LoggingService loggingService) {
        this.loginService = loginService;
        this.loggingService = loggingService;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(LoginRequest request) {

        MDC.put("user_email", request.getEmail());
        MDC.put("event_type", "login");
        LoginResponse user = loginService.authenticate(request.getEmail(), request.getPassword());

        if (user != null) {
            MDC.put("event_outcome", "success");
            LOG.info("SECURITY EVENT - User login successful");

            loggingService.logLogin(user.getId());

            String accessToken = getAccessToken(user);
            String refreshToken = getRefreshToken(user);
            return Response.ok(new TokenResponse(accessToken, refreshToken)).build();
        }
        else {

            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid credentials")
                    .build();
        }
    }

    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"refresh_token"})
    public Response refresh(@Context SecurityContext securityContext) {

        String email = securityContext.getUserPrincipal().getName();
        MDC.put("user_email", email);
        MDC.put("event_type", "refresh_token");

        LoginResponse loginResponse = loginService.getLoginByEmail(email);

        if (loginResponse == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        MDC.put("event_outcome", "success");
        LOG.info("SECURITY EVENT - User refresh successful");

        String accessToken = getAccessToken(loginResponse);
        return Response.ok(new AccessTokenResponse(accessToken)).build();
    }

    private String getAccessToken(LoginResponse loginResponse) {
        return Jwt
                .issuer("packovery-backend-jwt")
                .subject(loginResponse.getEmail())
                .upn(loginResponse.getEmail())
                .groups(Set.of("access_token", loginResponse.getRole()))
                .claim(Claims.nickname.name(), loginResponse.getEmail())
                .claim("id", loginResponse.getId())
                .expiresIn(Duration.ofMinutes(20))
                .issuedAt(Instant.now())
                .sign();
    }

    private String getRefreshToken(LoginResponse loginResponse) {
        return Jwt
                .issuer("packovery-backend-jwt")
                .subject(loginResponse.getEmail())
                .upn(loginResponse.getEmail())
                .groups(Set.of("refresh_token", loginResponse.getRole()))
                .claim(Claims.nickname.name(), loginResponse.getEmail())
                .claim("id", loginResponse.getId())
                .expiresIn(Duration.ofHours(1))
                .issuedAt(Instant.now())
                .sign();
    }
}
