package it.packovery.data.model;

import jakarta.persistence.*;

@Entity
@Table(name = "alerts_config")
public class AlertConfig {

    @Id
    private String id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "threshold", nullable = false)
    private String threshold;

    @Column(name = "state", nullable = false)
    private boolean state;

    public AlertConfig(String id, String type, String name, String description, String threshold, boolean state) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.threshold = threshold;
        this.state = state;
    }

    public AlertConfig() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
