package it.packovery.web.model;

public class UpdateAlertConfigRequest {

    private String threshold;
    private Boolean state;

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}
