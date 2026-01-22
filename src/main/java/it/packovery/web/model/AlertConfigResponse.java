package it.packovery.web.model;

public class AlertConfigResponse {

    private Long id;
    private String type;
    private String name;
    private String description;
    private String threshold;
    private boolean state;

    public AlertConfigResponse(Long id, String type, String name, String description, String threshold, boolean state) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.threshold = threshold;
        this.state = state;
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
}
