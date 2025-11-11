package com.example.userservice.resource;

import com.example.userservice.adapter.structure.UserAdapterRequest;
import com.example.userservice.business.structure.UserResponse;
import com.example.userservice.business.structure.UserService;
import com.example.userservice.library.util.LoggerUtil;
import com.example.userservice.resource.structure.UserRequest;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    private final UserService service;

    public UserResource(UserService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        LoggerUtil.info("Creación de Usuario: " + request.name());
        return service.createUser(new UserAdapterRequest(request.name(), request.email()));
    }

    @GetMapping
    public Flux<UserResponse> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<UserResponse> getById(@PathVariable Long id) {
        return service.getUserById(id);
    }
}
