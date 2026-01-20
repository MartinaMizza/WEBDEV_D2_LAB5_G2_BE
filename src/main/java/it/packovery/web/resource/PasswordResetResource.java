package it.packovery.web.resource;

import io.smallrye.jwt.build.Jwt;
import it.packovery.service.PasswordResetService;
import it.packovery.web.model.*;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@DenyAll
@Path("/api/auth/password-reset")
public class PasswordResetResource {

    private final PasswordResetService passwordResetService;

    public PasswordResetResource(PasswordResetService orderService) {
        this.passwordResetService = orderService;
    }

    @POST
    @Path("/request")
    @PermitAll
    @Transactional
    public Response requestReset(PasswordResetRequest passwordResetRequest) {
        LoginResponse loginResponse = passwordResetService.processPasswordResetRequest(passwordResetRequest);

        String passwordResetToken = getPasswordResetToken(loginResponse);

        return Response.ok(new PasswordResetTokenResponse(passwordResetToken)).build();
    }

    @POST
    @Path("/confirm")
    @RolesAllowed({"password_reset_token"})
    @Transactional
    public Response confirmReset(OtpVerificationRequest otpVerificationRequest) {
        passwordResetService.processOtpVerificationRequest(otpVerificationRequest);

        return Response.ok().build();
    }

    @POST
    @Path("/reset")
    @RolesAllowed({"password_reset_token"})
    @Transactional
    public Response confirmReset(NewPasswordRequest newPasswordRequest) {
        passwordResetService.resetPassword(newPasswordRequest);

        return Response.ok().build();
    }

    private String getPasswordResetToken(LoginResponse loginResponse) {
        return Jwt
                .issuer("packovery-backend-jwt")
                .subject(loginResponse.getEmail())
                .upn(loginResponse.getEmail())
                .groups(Set.of("password_reset_token"))
                .claim(Claims.nickname.name(), loginResponse.getEmail())
                .claim("id", loginResponse.getId())
                .expiresIn(Duration.ofMinutes(10))
                .issuedAt(Instant.now())
                .sign();
    }
}
