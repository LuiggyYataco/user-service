package com.example.userservice.business.connector;

import com.example.userservice.adapter.connector.UsuarioEntity;
import com.example.userservice.adapter.connector.UsuarioRepository;
import com.example.userservice.adapter.structure.UsuarioAdapterRequest;
import com.example.userservice.business.UsuarioServiceImpl;
import com.example.userservice.business.structure.CrearUsuarioResponse;
import com.example.userservice.library.exception.CustomException;
import com.example.userservice.library.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class UsuarioServiceImplTest {
    private UsuarioRepository repository;
    private UsuarioServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(UsuarioRepository.class);
        service = new UsuarioServiceImpl(repository);
    }

    @Test
    void createUser_ok() {
        UsuarioAdapterRequest req = UsuarioAdapterRequest.builder()
                .name("Carlos Mendoza")
                .email("carlos.mendoza@gmail.com")
                .build();

        when(repository.save(ArgumentMatchers.any(UsuarioEntity.class)))
                .thenAnswer(inv -> {
                    UsuarioEntity e = inv.getArgument(0);
                    return Mono.just(UsuarioEntity.builder()
                            .id(1L)
                            .name(e.getName())
                            .email(e.getEmail())
                            .build());
                });

        Mono<CrearUsuarioResponse> result = service.createUser(req);

        StepVerifier.create(result)
                .expectNextMatches(r -> r.id().equals(1L)
                        && r.name().equals("Carlos Mendoza")
                        && r.email().equals("carlos.mendoza@gmail.com"))
                .verifyComplete();

        verify(repository, times(1)).save(any(UsuarioEntity.class));
    }

    @Test
    void getAllUsers_ok() {
        when(repository.findAll()).thenReturn(Flux.just(
                UsuarioEntity.builder().id(1L).name("Ana Pérez").email("ana.perez@gmail.com").build(),
                UsuarioEntity.builder().id(2L).name("Diego Salas").email("diego.salas@outlook.com").build()
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
