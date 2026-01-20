package it.packovery.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.quarkus.jackson.ObjectMapperCustomizer;
import jakarta.inject.Singleton;

@Singleton
public class JacksonConfig implements ObjectMapperCustomizer {
    private final SanitizedStringDeserializer sanitizedStringDeserializer;

    public JacksonConfig(SanitizedStringDeserializer sanitizedStringDeserializer) {
        this.sanitizedStringDeserializer = sanitizedStringDeserializer;
    }

    @Override
    public void customize(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, sanitizedStringDeserializer);
        objectMapper.registerModule(module);
    }
}