package it.packovery.web.model;

public class PasswordResetTokenResponse {

    String passwordResetToken;

    public PasswordResetTokenResponse(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }
}

