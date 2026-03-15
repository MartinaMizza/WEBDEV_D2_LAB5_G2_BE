package it.packovery.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import it.packovery.data.model.Message;
import it.packovery.data.model.Order;
import it.packovery.data.model.enumModel.OrderStatus;
import it.packovery.data.model.login.Login;
import it.packovery.data.repository.LoginRepository;
import it.packovery.data.repository.MessageRepository;
import it.packovery.data.repository.OrderRepository;
import it.packovery.service.exception.NotFoundException;
import it.packovery.service.exception.EmailSendingException;
import it.packovery.service.exception.SendMessageException;
import it.packovery.web.model.SendMessageRequest;
import it.packovery.web.resource.MessageResource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

@ApplicationScoped
public class MessageService {

    private static final Logger LOG = Logger.getLogger(MessageResource.class);

    private final MessageRepository messageRepository;
    private final LoginRepository loginRepository;
    private final OrderRepository orderRepository;
    private final Mailer mailer;

    public MessageService(
            MessageRepository messageRepository,
            LoginRepository loginRepository,
            OrderRepository orderRepository,
            Mailer mailer
    ) {
        this.messageRepository = messageRepository;
        this.loginRepository = loginRepository;
        this.orderRepository = orderRepository;
        this.mailer = mailer;
    }

    @Transactional
    public void createMessage(SendMessageRequest sendMessageRequest, String senderEmail) {
        Order order = orderRepository.findById(sendMessageRequest.getOrderId());

        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        // Chiarire cosa voglia dire "preso in carico"
        if (order.getMapAndGps().getRider().getId() == null) {
            MDC.put("event_outcome", "failure");
            LOG.error("Order with ID: " + order.getId() + " has no rider");
            throw new SendMessageException("Order has no rider");
        }

        if (!order.getOrderStatus().equals(OrderStatus.IN_TRANSIT) && !order.getOrderStatus().equals(OrderStatus.READY)) {
            MDC.put("event_outcome", "failure");
            LOG.error("Order with ID: " + order.getId() + " was already delivered or canceled");
            throw new SendMessageException("Order was already delivered or canceled");
        }

        Login user = loginRepository.findByEmail(senderEmail);

        if (user == null) {
            MDC.put("event_outcome", "failure");
            LOG.warn("User not found");
            throw new NotFoundException("User not found");
        }

        Message message = new Message(
                user,
                order.getMapAndGps().getRider(),
                sendMessageRequest.getMessage()
        );

        try {
            messageRepository.persist(message);
        }
        catch (PersistenceException e) {
            MDC.put("event_outcome", "failure");
            LOG.error("Database error during message creation", e);
            throw new EmailSendingException("Failed to create message due to server error", e);
        }

        try {
            sendMessageNotification(
                    senderEmail,
                    order.getId(),
                    order.getMapAndGps().getRider().getId(),
                    sendMessageRequest.getMessage()
            );
        }
        catch (RuntimeException e) {
            MDC.put("event_outcome", "failure");
            LOG.error("Failed to send message notification to " + senderEmail, e);
            throw new EmailSendingException(
                    "Failed to send message notification to " + senderEmail + " due to server error", e
            );
        }
    }

    public void sendMessageNotification(String email, Long orderId, Long riderId, String message) {
        mailer.send(
                Mail.withText(
                        email,
                        "Message sent notification",
                        "Order id: " + orderId + "\nRider id: " + riderId + "\nMessage: " + message
                )
        );
    }
}
