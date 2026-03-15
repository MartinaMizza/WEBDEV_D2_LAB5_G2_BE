package it.packovery.data.repository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import it.packovery.data.model.login.Login;
import it.packovery.service.LoggingService;
import it.packovery.service.exception.AccountPermanentlyBlockedException;
import it.packovery.service.exception.AccountTemporarilyBlockedException;
import it.packovery.service.exception.GenericException;
import it.packovery.web.resource.LoginResource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class LoginRepository implements PanacheRepository<Login> {

    private static final Logger LOG = Logger.getLogger(LoginResource.class);
    private final LoggingService loggingService;

    public LoginRepository(LoggingService loggingService)
    {
        this.loggingService = loggingService;
    }

    @Transactional
    public Login authenticate(String email, String password) {
        Login userLogin = findByEmail(email);

        if (userLogin != null) {
            OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

            if (userLogin.isPermanentlyBlocked()) {
                throw new AccountPermanentlyBlockedException("Account permanently blocked. Contact support");
            }

            if (userLogin.getBlockedUntil() != null && userLogin.getBlockedUntil().isAfter(now)) {
                ZonedDateTime localUnlockingDateTime = userLogin.getBlockedUntil().atZoneSameInstant(ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                throw new AccountTemporarilyBlockedException(
                        "Account blocked until " + localUnlockingDateTime.format(formatter)
                );
            }

            boolean matches = BcryptUtil.matches(password, userLogin.getPassword());

            if (matches) {
                userLogin.setFailedAttempts(0);
                userLogin.setBlockedUntil(null);

                try {
                    persist(userLogin);
                }
                catch (PersistenceException e) {
                    throw  new GenericException("Failed to save user due to server error");
                }

                return userLogin;
            }
            else {
                handleFailedAttempt(userLogin);
                return null;
            }
        }

        return null;
    }

    public Login findByEmail(String email) {
        return find(
                "SELECT l " +
                        "FROM Login l " +
                        "WHERE l.email = :email ",
                Parameters.with("email", email)
        ).firstResult();
    }

    private void handleFailedAttempt(Login login) {
        login.setFailedAttempts(login.getFailedAttempts() + 1);

        switch (login.getFailedAttempts()) {
            case 3 -> {
                LOG.warnf("SECURITY EVENT - Temporarily blocked user with email: [%s]", login.getEmail());
                login.setBlockedUntil(OffsetDateTime.now().plusMinutes(30));
                loggingService.logUserBlocked(login.getId());
            }
            case 5 -> {
                LOG.warnf("SECURITY EVENT - Temporarily blocked user with email: [%s]", login.getEmail());
                login.setBlockedUntil(OffsetDateTime.now().plusHours(1));
                loggingService.logUserBlocked(login.getId());
            }
            case 6 -> {
                //loggingRepository.createUserBlockedLogRecord(login.getId());
                LOG.warnf("SECURITY EVENT - Permanently blocked user with email: [%s]", login.getEmail());
                login.setPermanentlyBlocked(true);
                loggingService.logUserBlocked(login.getId());
            }
        }

        try {
            persist(login);
        }
        catch (Exception e) {
            throw new GenericException("Failed to save user due to server error");
        }

    }
}
