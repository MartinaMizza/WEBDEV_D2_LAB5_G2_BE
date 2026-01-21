package it.packovery.service;

import it.packovery.data.model.AlertConfig;
import it.packovery.data.repository.AlertConfigRepository;
import it.packovery.service.exception.AlertConfigAlreadyExistsException;
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

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AlertConfigService {

    private final AlertConfigRepository alertConfigRepository;

    public AlertConfigService(AlertConfigRepository alertConfigRepository) {
        this.alertConfigRepository = alertConfigRepository;
    }

    public List<AlertConfigResponse> getAllAlertsConfig() {
        List<AlertConfig> alertConfigList = alertConfigRepository.findAll().list();

        List<AlertConfigResponse> alertConfigResponseList = new ArrayList<>();
        for (AlertConfig alertConfig : alertConfigList) {
            alertConfigResponseList.add(toAlertConfigResponse(alertConfig));
        }

        return alertConfigResponseList;
    }

    @Transactional
    public AlertConfigResponse createAlertConfig(CreateAlertConfigRequest request) {
        AlertConfig existingAlertConfig = alertConfigRepository.findById(request.getId());

        if (existingAlertConfig != null) {
            throw new AlertConfigAlreadyExistsException(
                    "Alert config with id: " + request.getId() + " already exists"
            );
        }

        AlertConfig newAlertConfig = new AlertConfig(
                request.getId(),
                request.getType(),
                request.getName(),
                request.getDescription(),
                request.getThreshold(),
                request.isState()
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
    public AlertConfigResponse updateAlertConfig(String id, UpdateAlertConfigRequest updateAlertConfigRequest) {
        AlertConfig alertConfig = alertConfigRepository.findById(id);

        if (alertConfig == null) {
            throw new NotFoundException("Alert config with id: " + id + " not found");
        }

        if (updateAlertConfigRequest.getId() != null && !updateAlertConfigRequest.getId().isBlank()
                && !updateAlertConfigRequest.getId().equals(alertConfig.getId())
        ) {
            AlertConfig alreadyExistingAlertConfig = alertConfigRepository.findById(updateAlertConfigRequest.getId());

            if (alreadyExistingAlertConfig != null) {
                throw new AlertConfigAlreadyExistsException(
                        "Alert config with id: " + updateAlertConfigRequest.getId() + " already exists"
                );
            }

            alertConfig.setId(updateAlertConfigRequest.getId());
        }

        if (updateAlertConfigRequest.getThreshold() != null && !updateAlertConfigRequest.getThreshold().isBlank()
                && !updateAlertConfigRequest.getThreshold().equals(alertConfig.getThreshold())) {
            alertConfig.setThreshold(updateAlertConfigRequest.getThreshold());
        }

        if (updateAlertConfigRequest.getState() != null && updateAlertConfigRequest.getState() != alertConfig.isState()) {
            alertConfig.setState(updateAlertConfigRequest.getState());
        }

        return toAlertConfigResponse(alertConfig);
    }

    @Transactional
    public AlertConfigResponse updateStateAlertConfig(String id, UpdateStateAlertConfigRequest updateStateAlertConfigRequest) {
        AlertConfig alertConfig = alertConfigRepository.findById(id);

        if (alertConfig == null) {
            throw new NotFoundException("Alert config with id: " + id + " not found");
        }

        if (updateStateAlertConfigRequest.getId() != null && !updateStateAlertConfigRequest.getId().isBlank()
                && !updateStateAlertConfigRequest.getId().equals(alertConfig.getId())
        ) {
            AlertConfig alreadyExistingAlertConfig = alertConfigRepository.findById(updateStateAlertConfigRequest.getId());

            if (alreadyExistingAlertConfig != null) {
                throw new AlertConfigAlreadyExistsException(
                        "Alert config with id: " + updateStateAlertConfigRequest.getId() + " already exists"
                );
            }

            alertConfig.setId(updateStateAlertConfigRequest.getId());
        }

        if (updateStateAlertConfigRequest.getState() != null && updateStateAlertConfigRequest.getState() != alertConfig.isState()) {
            alertConfig.setState(updateStateAlertConfigRequest.getState());
        }

        return toAlertConfigResponse(alertConfig);
    }

    @Transactional
    public AlertConfigResponse deleteAlertConfig(String id) {
        AlertConfig alertConfig = alertConfigRepository.findById(id);

        if (alertConfig == null) {
            throw new NotFoundException("Alert config with id: " + id + " not found");
        }

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
                alertConfig.isState()
        );
    }

}
