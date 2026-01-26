package it.packovery.data.model;

import it.packovery.data.model.login.Login;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "alerts_config")
public class AlertConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", columnDefinition = "VARCHAR", nullable = false)
    private String type;

    @Column(name = "name", columnDefinition = "VARCHAR", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "VARCHAR", nullable = false)
    private String description;

    @Column(name = "threshold", columnDefinition = "VARCHAR", nullable = false)
    private String threshold;

    @Column(name = "state", nullable = false)
    private boolean state;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public Login login;

    public AlertConfig() {
    }

    public AlertConfig(String type, String name, String description, String threshold, boolean state, OffsetDateTime createdAt, Login login) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.threshold = threshold;
        this.state = state;
        this.createdAt = createdAt;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
}
