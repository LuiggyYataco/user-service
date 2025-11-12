package com.example.userservice.adapter.connector;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UsuarioRepository extends ReactiveCrudRepository<UsuarioEntity, Long> {
    Flux<UsuarioEntity> findByNameContainingIgnoreCase(String name);
}
