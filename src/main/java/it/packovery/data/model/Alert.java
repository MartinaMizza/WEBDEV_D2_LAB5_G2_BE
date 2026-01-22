package it.packovery.data.model;

import it.packovery.data.model.enumModel.AlertStatus;
import it.packovery.data.model.enumModel.AlertType;
import it.packovery.data.model.enumModel.IssueResolution;
import it.packovery.data.model.login.Login;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "alert_status_enum")
    private AlertStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_resolution", nullable = false, columnDefinition = "issue_resolution_enum")
    private IssueResolution issueResolution;

    @Column(name = "issue_creation_time")
    private OffsetDateTime issueCreationTime;

    @ManyToOne
    @JoinColumn(
            name = "issue_related_order_id",
            foreignKey = @ForeignKey(name = "fk_order"),
            nullable = true
    )
    private Order relatedOrder;

    @Column(name = "created_time")
    private OffsetDateTime createdTime;

    @Column(name = "resolved_time")
    private OffsetDateTime resolvedTime;

    @Column(name = "resolution_description")
    private String resolutionDescription;

    @ManyToOne
    @JoinColumn(
            name = "resolved_by",
            foreignKey = @ForeignKey(name = "fk_resolved_by"),
            nullable = true
    )
    private Login resolvedBy;

    @ManyToOne
    @JoinColumn(name = "alter_configuration_id") // corrisponde alla colonna nel DB
    public AlertConfig alterConfig;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false, columnDefinition = "alert_type_enum")
    private AlertType typeAlert;

    public Alert() {}

    public Alert(AlertStatus status, IssueResolution issueResolution, OffsetDateTime issueCreationTime, Order relatedOrder, OffsetDateTime createdTime, OffsetDateTime resolvedTime, String resolutionDescription, Login resolvedBy, AlertConfig alterConfig, AlertType typeAlert) {
        this.status = status;
        this.issueResolution = issueResolution;
        this.issueCreationTime = issueCreationTime;
        this.relatedOrder = relatedOrder;
        this.createdTime = createdTime;
        this.resolvedTime = resolvedTime;
        this.resolutionDescription = resolutionDescription;
        this.resolvedBy = resolvedBy;
        this.alterConfig = alterConfig;
        this.typeAlert = typeAlert;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlertStatus getStatus() {
        return status;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    public IssueResolution getIssueResolution() {
        return issueResolution;
    }

    public void setIssueResolution(IssueResolution issueResolution) {
        this.issueResolution = issueResolution;
    }

    public OffsetDateTime getIssueCreationTime() {
        return issueCreationTime;
    }

    public void setIssueCreationTime(OffsetDateTime issueCreationTime) {
        this.issueCreationTime = issueCreationTime;
    }

    public Order getRelatedOrder() {
        return relatedOrder;
    }

    public void setRelatedOrder(Order relatedOrder) {
        this.relatedOrder = relatedOrder;
    }

    public OffsetDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(OffsetDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public OffsetDateTime getResolvedTime() {
        return resolvedTime;
    }

    public void setResolvedTime(OffsetDateTime resolvedTime) {
        this.resolvedTime = resolvedTime;
    }

    public String getResolutionDescription() {
        return resolutionDescription;
    }

    public void setResolutionDescription(String resolutionDescription) {
        this.resolutionDescription = resolutionDescription;
    }

    public Login getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(Login resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public AlertConfig getAlterConfig() {
        return alterConfig;
    }

    public void setAlterConfig(AlertConfig alterConfig) {
        this.alterConfig = alterConfig;
    }

    public AlertType getTypeAlert() {
        return typeAlert;
    }

    public void setTypeAlert(AlertType typeAlert) {
        this.typeAlert = typeAlert;
    }
}




