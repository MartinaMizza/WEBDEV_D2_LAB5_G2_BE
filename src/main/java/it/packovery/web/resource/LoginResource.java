package it.packovery.web.resource;

import io.smallrye.jwt.build.Jwt;
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

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@DenyAll
@Path("/api/auth")
public class LoginResource {

    private static final Logger LOG = Logger.getLogger(LoginResource.class);
    private final LoginService loginService;

    public LoginResource(LoginService loginService) {
        this.loginService = loginService;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login(LoginRequest request) {
        LoginResponse user = loginService.authenticate(request.getEmail(), request.getPassword());

        if (user != null) {
            LOG.infof("SECURITY EVENT - Successful login for user: [%s]", request.getEmail());

            String accessToken = getAccessToken(user);
            String refreshToken = getRefreshToken(user);
            return Response.ok(new TokenResponse(accessToken, refreshToken)).build();
        }
        else {
            LOG.warnf("SECURITY EVENT - Failed login attempt for user: [%s]", request.getEmail());
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }

    }

    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"refresh_token"})
    public Response refresh(@Context SecurityContext securityContext) {
        String email = securityContext.getUserPrincipal().getName();

        LOG.infof("SECURITY EVENT - Session refresh requested for user: [%s]", email);

        LoginResponse loginResponse = loginService.getLoginByEmail(email);

        if (loginResponse == null) {
            LOG.warnf("SECURITY EVENT - Refresh failed: user [%s] not found", email);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

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
                .expiresIn(Duration.ofMinutes(1))
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
