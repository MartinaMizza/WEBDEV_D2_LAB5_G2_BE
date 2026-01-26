package it.packovery.data.repository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import it.packovery.data.model.PasswordResetToken;
import it.packovery.data.model.login.Login;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PasswordResetTokenRepository implements PanacheRepository<PasswordResetToken> {

    public PasswordResetToken findValidByUser(Login login) {
        return find("""
                    SELECT t
                    FROM PasswordResetToken t
                    WHERE t.login.email = :email
                    ORDER BY createdAt DESC
                """,
                Parameters.with("email", login.getEmail())
        ).firstResult();
    }

    public List<PasswordResetToken> findAllByLogin(Login login) {
        return find("""
                    SELECT t
                    FROM PasswordResetToken t
                    WHERE t.login.email = :email
                """,
                Parameters.with("email", login.getEmail())
        ).list();
    }

}
