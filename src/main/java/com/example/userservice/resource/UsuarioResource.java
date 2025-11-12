package com.example.userservice.resource;

import com.example.userservice.adapter.structure.UsuarioAdapterRequest;
import com.example.userservice.business.structure.CrearUsuarioResponse;
import com.example.userservice.business.structure.UsuarioService;
import com.example.userservice.library.util.LoggerUtil;
import com.example.userservice.resource.structure.CrearUsuarioRequest;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bs-usuarios/gestion-usuarios/v1")
public class UsuarioResource {

    private final UsuarioService usuarioService;

    public UsuarioResource(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crear")
    public Mono<CrearUsuarioResponse> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        LoggerUtil.info("Creación de Usuario: " + request.name());
        return usuarioService.createUser(new UsuarioAdapterRequest(request.name(), request.email()));
    }

    @GetMapping("/listar")
    public Flux<CrearUsuarioResponse> listarUsuarios() {
        return usuarioService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<CrearUsuarioResponse> obtenerPorId(@PathVariable Long id) {
        return usuarioService.getUserById(id);
    }
}
