package com.example.userservice.resource;

import com.example.userservice.business.structure.CrearUsuarioResponse;
import com.example.userservice.business.structure.UsuarioService;
import com.example.userservice.resource.structure.CrearUsuarioRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class UsuarioResourceTest {
    private UsuarioService usuarioService;
    private WebTestClient webClient;

    @BeforeEach
    void setup() {
        usuarioService = Mockito.mock(UsuarioService.class);
        UsuarioResource controller = new UsuarioResource(usuarioService);
        webClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void crearUsuario_ok() {
        CrearUsuarioRequest req = CrearUsuarioRequest.builder()
                .name("Luis Fernández")
                .email("luis.fernandez@outlook.com")
                .build();

        Mockito.when(usuarioService.createUser(Mockito.any()))
                .thenReturn(Mono.just(CrearUsuarioResponse.builder()
                        .id(1L).name("Luis Fernández").email("luis.fernandez@outlook.com").build()));

        webClient.post()
                .uri("/bs-usuarios/gestion-usuarios/v1/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Luis Fernández")
                .jsonPath("$.email").isEqualTo("luis.fernandez@outlook.com");
    }

    @Test
    void listarUsuarios_ok() {
        Mockito.when(usuarioService.getAllUsers())
                .thenReturn(Flux.just(
                        CrearUsuarioResponse.builder().id(1L).name("Valeria Castro").email("valeria.castro@gmail.com").build(),
                        CrearUsuarioResponse.builder().id(2L).name("Miguel Torres").email("miguel.torres@yahoo.com").build()
                ));

        webClient.get()
                .uri("/bs-usuarios/gestion-usuarios/v1/listar")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("Valeria Castro")
                .jsonPath("$[0].email").isEqualTo("valeria.castro@gmail.com")
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].name").isEqualTo("Miguel Torres")
                .jsonPath("$[1].email").isEqualTo("miguel.torres@yahoo.com");
    }

    @Test
    void obtenerPorId_ok() {
        Mockito.when(usuarioService.getUserById(1L))
                .thenReturn(Mono.just(CrearUsuarioResponse.builder()
                        .id(1L).name("Sofía Rojas").email("sofia.rojas@outlook.com").build()));

        webClient.get()
                .uri("/bs-usuarios/gestion-usuarios/v1/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Sofía Rojas")
                .jsonPath("$.email").isEqualTo("sofia.rojas@outlook.com");
    }
}
