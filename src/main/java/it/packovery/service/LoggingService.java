package it.packovery.service;

import it.packovery.data.model.login.Login;
import it.packovery.data.repository.LoggingRepository;
import it.packovery.data.repository.LoginRepository;
import it.packovery.service.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoggingService {

    private final LoggingRepository loggingRepository;
    private final LoginRepository loginRepository;

    public LoggingService(LoggingRepository loggingRepository, LoginRepository loginRepository) {
        this.loggingRepository = loggingRepository;
        this.loginRepository = loginRepository;
    }

    public void updateLogRecordsAtLogout(String email) {
        Login login = loginRepository.findByEmail(email);

        if (login == null) {
            throw new NotFoundException("User not found");
        }

        loggingRepository.updateLogRecordsAtLogout(login.getId());
    }

    public void logLogin(Long userId) {
        loggingRepository.createLoginLogRecord(userId);
    }

    public void logViewedOpenOrder(Long userId) {
        loggingRepository.createViewedOpenOrderLogRecord(userId);
    }

    public void logViewedClosedOrder(Long userId) {
        loggingRepository.createViewedClosedOrderLogRecord(userId);
    }

    public void logUserBlocked(Long userId) {
        loggingRepository.createUserBlockedLogRecord(userId);
    }
}
