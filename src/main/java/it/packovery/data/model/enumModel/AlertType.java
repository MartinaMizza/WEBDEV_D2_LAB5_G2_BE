package it.packovery.data.model.enumModel;

public enum AlertType {
    GPS_INTERRUPTED("Segnale GPS interrotto"),
    DELIVERY_DELAY("Ritardo consegna ordine"),
    DEPARTURE_DELAY("Ritardo partenza ordine");

    private final String label;

    AlertType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
