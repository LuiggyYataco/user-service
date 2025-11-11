package com.example.userservice.business.structure;

import com.example.userservice.adapter.structure.UserAdapterRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserResponse> createUser(UserAdapterRequest request);
    Flux<UserResponse> getAllUsers();
    Mono<UserResponse> getUserById(Long id);
}
