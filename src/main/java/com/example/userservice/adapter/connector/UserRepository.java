package com.example.userservice.adapter.connector;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Flux<UserEntity> findByNameContainingIgnoreCase(String name);
}
