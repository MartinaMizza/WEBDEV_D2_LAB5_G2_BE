package it.packovery.web.model;

public class LoginResponse {

    private long id;
    private String email;
    private String role;
    private boolean accountStatus;

    public LoginResponse(long id, String email, String role, boolean accountStatus) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.accountStatus = accountStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
