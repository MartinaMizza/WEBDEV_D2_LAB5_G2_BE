package it.packovery.service;

import it.packovery.data.model.login.Login;
import it.packovery.data.repository.LoggingRepository;
import it.packovery.data.repository.LoginRepository;
import it.packovery.service.exception.*;
import it.packovery.web.model.LoginResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoginService {

    private final LoginRepository loginRepository;
    private final LoggingRepository loggingRepository;

    public LoginService(LoginRepository loginRepository, LoggingRepository loggingRepository) {
        this.loginRepository = loginRepository;
        this.loggingRepository = loggingRepository;
    }

    public LoginResponse authenticate(String email, String password) {
        Login login = loginRepository.authenticate(email, password);

        if (login == null) {
            throw new InvalidCredentialsException("Email or password are incorrect");
        }

        loggingRepository.createLoginLogRecord(login.getId());
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
                login.getRole().name(),
                login.isAccountStatus()
        );
    }
}
