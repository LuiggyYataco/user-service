package com.example.userservice.business.connector;

import com.example.userservice.adapter.connector.UserEntity;
import com.example.userservice.adapter.connector.UserRepository;
import com.example.userservice.adapter.structure.UserAdapterRequest;
import com.example.userservice.business.UserServiceImpl;
import com.example.userservice.business.structure.UserResponse;
import com.example.userservice.library.exception.CustomException;
import com.example.userservice.library.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserRepository repository;
    private UserServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        service = new UserServiceImpl(repository);
    }

    @Test
    void createUser_ok() {
        UserAdapterRequest req = UserAdapterRequest.builder()
                .name("Andrea")
                .email("andrea@example.com")
                .build();

        when(repository.save(ArgumentMatchers.any(UserEntity.class)))
                .thenAnswer(inv -> {
                    UserEntity e = inv.getArgument(0);
                    return Mono.just(UserEntity.builder()
                            .id(1L)
                            .name(e.getName())
                            .email(e.getEmail())
                            .build());
                });

        Mono<UserResponse> result = service.createUser(req);

        StepVerifier.create(result)
                .expectNextMatches(r -> r.id().equals(1L)
                        && r.name().equals("Andrea")
                        && r.email().equals("andrea@example.com"))
                .verifyComplete();

        verify(repository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void getAllUsers_ok() {
        when(repository.findAll()).thenReturn(Flux.just(
                UserEntity.builder().id(1L).name("A").email("a@x.com").build(),
                UserEntity.builder().id(2L).name("B").email("b@x.com").build()
        ));

        StepVerifier.create(service.getAllUsers())
                .expectNextCount(2)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void getUserById_notFound() {
        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.getUserById(99L))
                .expectErrorMatches(ex -> ex instanceof CustomException
                        && ((CustomException) ex).getCode() == ErrorCode.NOT_FOUND)
                .verify();

        verify(repository, times(1)).findById(99L);
    }

}
