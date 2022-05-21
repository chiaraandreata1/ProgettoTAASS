package com.example.shared.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrappedRepositoryGet {

    public static <T, K> T get(JpaRepository<T, K> repository, K id, String className) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("%s for id %s not found", className, id)));
    }
}
