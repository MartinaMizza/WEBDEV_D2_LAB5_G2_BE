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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class MessageService {

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
            throw new SendMessageException("Order has no rider");
        }

        if (!order.getOrderStatus().equals(OrderStatus.IN_TRANSIT) || !order.getOrderStatus().equals(OrderStatus.READY)) {
            throw new SendMessageException("Order is already delivered or canceled");
        }

        Login user = loginRepository.findByEmail(senderEmail);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        Message message = new Message(
                user,
                order.getMapAndGps().getRider(),
                sendMessageRequest.getMessage()
        );
        messageRepository.persist(message);

        try {
            sendMessageNotification(
                    senderEmail,
                    order.getId(),
                    order.getMapAndGps().getRider().getId(),
                    sendMessageRequest.getMessage()
            );
        }
        catch (RuntimeException e) {
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
