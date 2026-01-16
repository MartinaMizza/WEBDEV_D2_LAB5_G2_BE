package it.packovery.data.repository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import it.packovery.data.model.Login;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoginRepository implements PanacheRepository<Login> {

    public Login authenticate(String email, String password) {
        Login userLogin = findByEmail(email);
        if (userLogin != null) {
            boolean matches = BcryptUtil.matches(password, userLogin.getPassword());

            if (matches) {
                return userLogin;
            }
            else {
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
}
