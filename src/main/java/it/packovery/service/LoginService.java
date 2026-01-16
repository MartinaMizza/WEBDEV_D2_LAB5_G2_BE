package it.packovery.service;

import it.packovery.data.model.Login;
import it.packovery.data.repository.LoginRepository;
import it.packovery.service.exception.GenericException;
import it.packovery.service.exception.InvalidCredentialsException;
import it.packovery.service.exception.NotFoundException;
import it.packovery.web.model.LoginResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoginService {

    private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LoginResponse authenticate(String email, String password) {
        Login login = loginRepository.authenticate(email, password);

        if (login == null) {
            throw new InvalidCredentialsException("Email or password are incorrect");
        }

        return toLoginResponse(login);
    }

    public LoginResponse getLoginByEmail(String email) {
        Login login;
        try {
            login = loginRepository.findByEmail(email);
        }
        catch (Exception e) {
            throw new GenericException("Failed to retrieve user due to server error");
        }

        if (login != null) {
            return toLoginResponse(login);
        }
        else {
            throw new NotFoundException("No user found with email: " + email);
        }
    }

    private static LoginResponse toLoginResponse(Login login) {
        return new LoginResponse(
                login.getId(),
                login.getEmail(),
                login.getRole(),
                login.isAccountStatus()
        );
    }
}
