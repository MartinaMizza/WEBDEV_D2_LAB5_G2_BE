package it.packovery.web.resource;

import it.packovery.service.PasswordResetService;
import it.packovery.web.model.OtpVerificationRequest;
import it.packovery.web.model.PasswordResetRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/api/auth/password-reset")
public class PasswordResetResource {

    private final PasswordResetService passwordResetService;

    public PasswordResetResource(PasswordResetService orderService) {
        this.passwordResetService = orderService;
    }

    @POST
    @Path("/request")
    @Transactional
    public Response requestReset(PasswordResetRequest passwordResetRequest) {
        passwordResetService.processPasswordResetRequest(passwordResetRequest);

        return Response.ok().build();
    }

    @POST
    @Path("/confirm")
    @Transactional
    public Response confirmReset(OtpVerificationRequest otpVerificationRequest) {
        passwordResetService.processOtpVerificationRequest(otpVerificationRequest);

        return Response.ok().build();
    }
}
