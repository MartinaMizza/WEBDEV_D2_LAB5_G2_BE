package it.packovery.service;

import io.quarkus.security.UnauthorizedException;
import it.packovery.data.model.AlertConfig;
import it.packovery.data.model.login.Login;
import it.packovery.data.repository.AlertConfigRepository;
import it.packovery.data.repository.LoginRepository;
import it.packovery.service.exception.AlertConfigCreationException;
import it.packovery.service.exception.AlertConfigDeletionException;
import it.packovery.service.exception.NotFoundException;
import it.packovery.web.model.AlertConfigResponse;
import it.packovery.web.model.CreateAlertConfigRequest;
import it.packovery.web.model.UpdateAlertConfigRequest;
import it.packovery.web.model.UpdateStateAlertConfigRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AlertConfigService {

    private final AlertConfigRepository alertConfigRepository;
    private final LoginRepository loginRepository;

    public AlertConfigService(AlertConfigRepository alertConfigRepository, LoginRepository loginRepository) {
        this.alertConfigRepository = alertConfigRepository;
        this.loginRepository = loginRepository;
    }

    public List<AlertConfigResponse> getAllAlertsConfigByUserEmail(String email) {
        Login login = loginRepository.findByEmail(email);

        List<AlertConfig> alertConfigList = alertConfigRepository.findAllByUserId(login.getId());

        List<AlertConfigResponse> alertConfigResponseList = new ArrayList<>();
        for (AlertConfig alertConfig : alertConfigList) {
            alertConfigResponseList.add(toAlertConfigResponse(alertConfig));
        }

        return alertConfigResponseList;
    }

    @Transactional
    public AlertConfigResponse createAlertConfig(String email, CreateAlertConfigRequest request) {
        Login login = loginRepository.findByEmail(email);

        if (login == null) {
            throw new NotFoundException("User not found");
        }

        AlertConfig newAlertConfig = new AlertConfig(
                request.getType(),
                request.getName(),
                request.getDescription(),
                request.getThreshold(),
                request.isState(),
                OffsetDateTime.now(),
                login
        );

        try {
            alertConfigRepository.persist(newAlertConfig);
        }
        catch (PersistenceException e) {
            throw new AlertConfigCreationException("Failed to create alert config due to server error", e);
        }

        return toAlertConfigResponse(newAlertConfig);
    }

    @Transactional
    public AlertConfigResponse updateAlertConfig(String id, UpdateAlertConfigRequest updateAlertConfigRequest, String email) {
        AlertConfig alertConfig = alertConfigRepository.findById(id);

        if (alertConfig == null) {
            throw new NotFoundException("Alert config with id: " + id + " not found");
        }

        Login login = loginRepository.findByEmail(email);
        canUpdateAlertConfig(alertConfig, login);

        if (updateAlertConfigRequest.getThreshold() != null && !updateAlertConfigRequest.getThreshold().isBlank() &&
                !updateAlertConfigRequest.getThreshold().equals(alertConfig.getThreshold())
        ) {
            alertConfig.setThreshold(updateAlertConfigRequest.getThreshold());
        }

        if (updateAlertConfigRequest.getState() != null && updateAlertConfigRequest.getState() != alertConfig.isState()) {
            alertConfig.setState(updateAlertConfigRequest.getState());
        }

        return toAlertConfigResponse(alertConfig);
    }

    @Transactional
    public AlertConfigResponse updateStateAlertConfig(String id, UpdateStateAlertConfigRequest updateStateAlertConfigRequest, String email) {
        AlertConfig alertConfig = alertConfigRepository.findById(id);

        if (alertConfig == null) {
            throw new NotFoundException("Alert config with id: " + id + " not found");
        }

        Login login = loginRepository.findByEmail(email);
        canUpdateAlertConfig(alertConfig, login);

        if (updateStateAlertConfigRequest.getState() != alertConfig.isState()) {
            alertConfig.setState(updateStateAlertConfigRequest.getState());
        }

        return toAlertConfigResponse(alertConfig);
    }

    @Transactional
    public AlertConfigResponse deleteAlertConfig(String id, String email) {
        AlertConfig alertConfig = alertConfigRepository.findById(id);

        if (alertConfig == null) {
            throw new NotFoundException("Alert config with id: " + id + " not found");
        }

        Login login = loginRepository.findByEmail(email);
        canUpdateAlertConfig(alertConfig, login);

        try {
            alertConfigRepository.delete(alertConfig);
        }
        catch (PersistenceException e) {
            throw new AlertConfigDeletionException("Error deleting alert config", e);
        }

        return toAlertConfigResponse(alertConfig);
    }

    public AlertConfigResponse toAlertConfigResponse(AlertConfig alertConfig) {
        return new AlertConfigResponse(
                alertConfig.getId(),
                alertConfig.getType(),
                alertConfig.getName(),
                alertConfig.getDescription(),
                alertConfig.getThreshold(),
                alertConfig.isState(),
                alertConfig.getCreatedAt()
        );
    }

    private static void canUpdateAlertConfig(AlertConfig alertConfig, Login login) {
        if (!alertConfig.getLogin().getId().equals(login.getId())) {
            throw new UnauthorizedException("User not authorized to update alert config");
        }
    }
}
