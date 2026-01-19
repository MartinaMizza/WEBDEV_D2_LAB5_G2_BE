package it.packovery.data.model;

import io.quarkus.security.jpa.UserDefinition;
import it.packovery.data.model.login.Login;
import jakarta.persistence.*;

@Entity
@Table(name = "message")
@UserDefinition
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Login sender;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private User rider;

    @Column(name = "message_content", nullable = false)
    private String message;

    public Message(Long id, Login sender, User rider, String message) {
        this.id = id;
        this.sender = sender;
        this.rider = rider;
        this.message = message;
    }

    public Message() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Login getSender() {
        return sender;
    }

    public void setSender(Login sender) {
        this.sender = sender;
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
