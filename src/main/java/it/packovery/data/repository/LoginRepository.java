package it.packovery.data.repository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import it.packovery.data.model.login.Login;
import it.packovery.service.exception.AccountPermanentlyBlockedException;
import it.packovery.service.exception.AccountTemporarilyBlockedException;
import it.packovery.web.resource.LoginResource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class LoginRepository implements PanacheRepository<Login> {

    private final LoginResource loginResource;

    public LoginRepository(LoginResource loginResource) {
        this.loginResource = loginResource;
    }

    @Transactional
    public Login authenticate(String email, String password) {
        Login userLogin = findByEmail(email);
        if (userLogin != null) {
            boolean matches = BcryptUtil.matches(password, userLogin.getPassword());

            if (matches) {
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

                userLogin.setFailedAttempts(0);
                userLogin.setBlockedUntil(null);
                persist(userLogin);
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
            case 3 -> login.setBlockedUntil(OffsetDateTime.now().plusMinutes(30));
            case 5 -> login.setBlockedUntil(OffsetDateTime.now().plusHours(1));
            case 6 -> login.setPermanentlyBlocked(true);
        }

        persist(login);
    }
}
