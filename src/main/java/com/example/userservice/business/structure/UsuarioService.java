package com.example.userservice.business.structure;

import com.example.userservice.adapter.structure.UsuarioAdapterRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsuarioService {
    Mono<CrearUsuarioResponse> createUser(UsuarioAdapterRequest request);
    Flux<CrearUsuarioResponse> getAllUsers();
    Mono<CrearUsuarioResponse> getUserById(Long id);
}
