package com.example.userservice.business;

import com.example.userservice.adapter.connector.UserEntity;
import com.example.userservice.adapter.connector.UserRepository;
import com.example.userservice.adapter.structure.UserAdapterRequest;
import com.example.userservice.business.structure.UserResponse;
import com.example.userservice.business.structure.UserService;
import com.example.userservice.library.exception.CustomException;
import com.example.userservice.library.exception.ErrorCode;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<UserResponse> createUser(UserAdapterRequest request) {
        return Mono.just(request)
                .map((UserAdapterRequest r) -> UserEntity.builder()
                        .name(r.name())
                        .email(r.email())
                        .build())
                .flatMap(repository::save)
                .map((UserEntity u) -> UserResponse.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .build())
                .onErrorMap(e -> new CustomException(ErrorCode.INTERNAL_ERROR, e.getMessage()));
    }

    @Override
    public Flux<UserResponse> getAllUsers() {
        return repository.findAll()
                .map(u -> UserResponse.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .build());
    }

    @Override
    public Mono<UserResponse> getUserById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(ErrorCode.NOT_FOUND, "User not found")))
                .map(u -> UserResponse.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .build());
    }
}
