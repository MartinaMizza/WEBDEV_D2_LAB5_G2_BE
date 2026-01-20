package it.packovery.web.model;

public class UpdateStateAlertConfigRequest {

    private String id;
    private Boolean state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
