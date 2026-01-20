package it.packovery.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import it.packovery.data.model.PasswordResetToken;
import it.packovery.data.model.login.Login;
import it.packovery.data.repository.LoginRepository;
import it.packovery.data.repository.PasswordResetTokenRepository;
import it.packovery.service.exception.*;
import it.packovery.web.model.OtpVerificationRequest;
import it.packovery.web.model.PasswordResetRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;

import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class PasswordResetService {

    private final LoginRepository loginRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final Mailer mailer;

    public PasswordResetService(
            LoginRepository loginRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            Mailer mailer
    ) {
        this.loginRepository = loginRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailer = mailer;
    }

    public void processPasswordResetRequest(PasswordResetRequest passwordResetRequest) {
        Login login = loginRepository.findByEmail(passwordResetRequest.getEmail());

        if (login == null) {
            throw new NotFoundException("User not found");
        }

        String otp = generateOtp();

        PasswordResetToken passwordResetToken = new PasswordResetToken(
                login,
                BcryptUtil.bcryptHash(otp),
                getExpirationTime(),
                false,
                OffsetDateTime.now()
        );

        try {
            passwordResetTokenRepository.persist(passwordResetToken);
        }
        catch (PersistenceException e) {
            throw new PasswordResetTokenCreationException("Failed to save otp due to server error", e);
        }

        try {
            sendOtp(passwordResetRequest.getEmail(), otp);
        }
        catch (RuntimeException e) {
            throw new EmailSendingException("Failed to send otp due to server error", e);
        }
    }

    public void processOtpVerificationRequest(OtpVerificationRequest otpVerificationRequest) {
        Login login = loginRepository.findByEmail(otpVerificationRequest.getEmail());

        if (login == null) {
            throw new NotFoundException("User not found");
        }

        PasswordResetToken token = passwordResetTokenRepository.findValidByUser(login);

        if (token == null) {
            throw new NotFoundException("No valid token found for user");
        }

        if (OffsetDateTime.now().isAfter(token.getExpiresAt())) {
            throw new InvalidOtpException("OTP has expired");
        }

        if (!BcryptUtil.matches(otpVerificationRequest.getOtp(), token.getOtpHash())) {
            throw new InvalidOtpException("OTP is not valid");
        }

        login.setPassword(BcryptUtil.bcryptHash(otpVerificationRequest.getNewPassword()));

        try {
            passwordResetTokenRepository.delete(token);
        }
        catch (PersistenceException e) {
            throw new PasswordResetTokenDeletionException("Failed to delete otp token due to server error", e);
        }

        try {
            loginRepository.persist(login);
        }
        catch (PersistenceException e) {
            throw new PasswordUpdateException("Failed to update password due to server error", e);
        }

    }

    public String generateOtp() {
        return String.valueOf(
                ThreadLocalRandom.current().nextInt(100000, 999999)
        );
    }

    public OffsetDateTime getExpirationTime() {
        return OffsetDateTime.now().plusMinutes(5);
    }

    public void sendOtp(String email, String otp) {
        mailer.send(
                Mail.withText(
                        email,
                        "Reset password",
                        "Il tuo codice OTP è: " + otp + "\nValido per 5 minuti."
                )
        );
    }
}
