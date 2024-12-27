package com.homeshare.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.homeshare.domain.User;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.homeshare.util.Helper.log;

@Repository
public class UserFileRepository {

    private static final String JSON_PATH = "users.json";
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .registerModule(new JavaTimeModule());

    @SneakyThrows
    public List<User> findAll() {
        File file = new File(JSON_PATH);
        log("JSON file location: " + file.getAbsolutePath());
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<>() {});
    }

    @SneakyThrows
    public void saveUser(User user) {
        List<User> users = findAll();
        // If user email already exists, replace it; otherwise add new
        Optional<User> existing = users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(user.getEmail()))
                .findFirst();

        existing.ifPresent(users::remove);
        users.add(user);

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JSON_PATH), users);
    }

    @PreDestroy
    public void destroyFile() {
        File file = new File(JSON_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

}
