package com.example.userservice.resource;

import com.example.userservice.business.structure.UserResponse;
import com.example.userservice.business.structure.UserService;
import com.example.userservice.resource.structure.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class UserResourceTest {
    private UserService userService;
    private WebTestClient webClient;

    @BeforeEach
    void setup() {
        userService = Mockito.mock(UserService.class);
        UserResource controller = new UserResource(userService);
        webClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void createUser_ok() {
        UserRequest req = UserRequest.builder()
                .name("Andrea")
                .email("andrea@example.com")
                .build();

        Mockito.when(userService.createUser(Mockito.any()))
                .thenReturn(Mono.just(UserResponse.builder()
                        .id(1L).name("Andrea").email("andrea@example.com").build()));

        webClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Andrea")
                .jsonPath("$.email").isEqualTo("andrea@example.com");
    }

    @Test
    void getAll_ok() {
        Mockito.when(userService.getAllUsers())
                .thenReturn(Flux.just(
                        UserResponse.builder().id(1L).name("A").email("a@x.com").build(),
                        UserResponse.builder().id(2L).name("B").email("b@x.com").build()
                ));

        webClient.get()
                .uri("/api/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[1].id").isEqualTo(2);
    }

    @Test
    void getById_ok() {
        Mockito.when(userService.getUserById(1L))
                .thenReturn(Mono.just(UserResponse.builder()
                        .id(1L).name("A").email("a@x.com").build()));

        webClient.get()
                .uri("/api/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("A");
    }
}
