package it.packovery.data.model.login;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "login")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "role")
    private Role role = Role.customer_care;

    @Column(name = "account_status", nullable = false)
    private boolean accountStatus = true;

    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts = 0;

    @Column(name = "blocked_until", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime blockedUntil;

    @Column(name = "permanently_blocked", nullable = false)
    private boolean permanentlyBlocked = false;

    public Login() {}

    public Login(String email, String password, Role role, boolean accountStatus, int failedAttempts, OffsetDateTime blockedUntil, boolean permanentlyBlocked) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.accountStatus = accountStatus;
        this.failedAttempts = failedAttempts;
        this.blockedUntil = blockedUntil;
        this.permanentlyBlocked = permanentlyBlocked;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public OffsetDateTime getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(OffsetDateTime blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public boolean isPermanentlyBlocked() {
        return permanentlyBlocked;
    }

    public void setPermanentlyBlocked(boolean permanentlyBlocked) {
        this.permanentlyBlocked = permanentlyBlocked;
    }
}
