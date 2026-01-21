package it.packovery.web.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;

public class LoginRequest {

    @JsonDeserialize(using= StringDeserializer.class)
    private String email;
    @JsonDeserialize(using= StringDeserializer.class)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
