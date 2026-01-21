package it.packovery.web.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

public class OtpVerificationRequest {

    @JsonDeserialize(using= StringDeserializer.class)
    private String email;

    @JsonDeserialize(using= StringDeserializer.class)
    private String otp;

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
