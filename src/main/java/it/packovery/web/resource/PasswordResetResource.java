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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.Claims;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@DenyAll
@Path("/api/auth/password-reset")
public class PasswordResetResource {

    private static final Logger LOG = Logger.getLogger(PasswordResetResource.class);
    private final PasswordResetService passwordResetService;

    public PasswordResetResource(PasswordResetService orderService) {
        this.passwordResetService = orderService;
    }

    @POST
    @Path("/request")
    @PermitAll
    @Transactional
    public Response requestReset(PasswordResetRequest passwordResetRequest) {
        String email = passwordResetRequest.getEmail();

        LOG.infof("SECURITY EVENT - Password reset requested for user: [%s]", email);

        LoginResponse loginResponse = passwordResetService.processPasswordResetRequest(passwordResetRequest);

        if (loginResponse != null) {
            String passwordResetToken = getPasswordResetToken(loginResponse);
            return Response.ok(new PasswordResetTokenResponse(passwordResetToken)).build();
        } else {
            LOG.warnf("SECURITY EVENT - Password reset failed: user [%s] does not exist", email);
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    @POST
    @Path("/confirm")
    @RolesAllowed({"password_reset_token"})
    @Transactional
    public Response confirmReset(@Context SecurityContext securityContext,
                                 OtpVerificationRequest otpVerificationRequest
    ) {
        String email = securityContext.getUserPrincipal().getName();

        try {
            passwordResetService.processOtpVerificationRequest(otpVerificationRequest);

            LOG.infof("SECURITY EVENT - Password successfully reset for user: [%s]", email);
            return Response.ok().build();
        } catch (Exception e) {
            LOG.warnf("SECURITY EVENT - Failed password reset attempt (invalid OTP) for user: [%s]", email);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid or expired OTP")
                    .build();
        }
    }

    @POST
    @Path("/reset")
    @RolesAllowed({"password_reset_token"})
    @Transactional
    public Response confirmReset(@Context SecurityContext securityContext,
                                 NewPasswordRequest newPasswordRequest
    ) {
        String email = securityContext.getUserPrincipal().getName();

        try {
            passwordResetService.resetPassword(newPasswordRequest);

            LOG.infof("SECURITY EVENT - User [%s] has successfully changed his password.", email);

            return Response.ok().build();
        } catch (Exception e) {
            LOG.errorf("SECURITY EVENT - Critical error during password reset for user [%s]: %s", email, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Could not reset password. Please try again.")
                    .build();
        }
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
