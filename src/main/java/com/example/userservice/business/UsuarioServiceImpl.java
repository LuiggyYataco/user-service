package com.example.userservice.business;

import com.example.userservice.adapter.connector.UsuarioEntity;
import com.example.userservice.adapter.connector.UsuarioRepository;
import com.example.userservice.adapter.structure.UsuarioAdapterRequest;
import com.example.userservice.business.structure.CrearUsuarioResponse;
import com.example.userservice.business.structure.UsuarioService;
import com.example.userservice.library.exception.CustomException;
import com.example.userservice.library.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;

    @Override
    public Mono<CrearUsuarioResponse> createUser(UsuarioAdapterRequest request) {
        return Mono.justOrEmpty(request)
                .switchIfEmpty(Mono.error(new CustomException(ErrorCode.BAD_REQUEST, "El cuerpo de la solicitud es nulo")))
                .map(req -> UsuarioEntity.builder()
                        .name(Optional.ofNullable(req.name())
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "El nombre es obligatorio")))
                        .email(Optional.ofNullable(req.email())
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "El correo es obligatorio")))
                        .build())
                .flatMap(repository::save)
                .map(this::toResponse)
                .onErrorMap(DataIntegrityViolationException.class,
                        e -> new CustomException(ErrorCode.BAD_REQUEST, "Violación de integridad o duplicado"))
                .onErrorMap(e -> !(e instanceof CustomException),
                        e -> new CustomException(ErrorCode.INTERNAL_ERROR, e.getMessage()));
    }

    @Override
    public Flux<CrearUsuarioResponse> getAllUsers() {
        return repository.findAll()
                .map(this::toResponse)
                .switchIfEmpty(Flux.error(new CustomException(ErrorCode.NOT_FOUND, "No existen usuarios registrados")))
                .onErrorMap(e -> !(e instanceof CustomException),
                        e -> new CustomException(ErrorCode.INTERNAL_ERROR, e.getMessage()));
    }

    @Override
    public Mono<CrearUsuarioResponse> getUserById(Long id) {
        Long validId = Optional.ofNullable(id)
                .filter(i -> i > 0)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST, "ID de usuario inválido"));

        return repository.findById(validId)
                .switchIfEmpty(Mono.error(new CustomException(ErrorCode.NOT_FOUND, "Usuario no encontrado")))
                .map(this::toResponse)
                .onErrorMap(e -> !(e instanceof CustomException),
                        e -> new CustomException(ErrorCode.INTERNAL_ERROR, e.getMessage()));
    }

    private CrearUsuarioResponse toResponse(UsuarioEntity entity) {
        return Optional.ofNullable(entity)
                .map(e -> CrearUsuarioResponse.builder()
                        .id(Optional.ofNullable(e.getId()).orElse(0L))
                        .name(Optional.ofNullable(e.getName()).orElse("Desconocido"))
                        .email(Optional.ofNullable(e.getEmail()).orElse("sin-correo@empresa.com"))
                        .build())
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_ERROR, "Entidad de usuario nula"));
    }
}
