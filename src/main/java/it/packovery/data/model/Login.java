package it.packovery.data.model;

import io.quarkus.security.jpa.*;
import jakarta.persistence.*;

@Entity
@Table(name = "login")
@UserDefinition
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Username
    @Column(name = "email", nullable = false)
    private String email;

    @Password
    @Column(name = "password", nullable = false)
    private String password;

    @Roles
    @Column(name = "role", nullable = false)
    private String role = "customer_care";

    @Column(name = "account_status", nullable = false)
    private boolean accountStatus = true;

    public Login() {}

    public Login(String email, String password, String role, boolean accountStatus) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }
}
