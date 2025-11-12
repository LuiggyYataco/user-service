package com.example.userservice.resource.structure;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CrearUsuarioRequest(
        @NotBlank(message = "El nombre es requerido")
        String name,
        @Email(message = "Formato Invalido")
        String email
) {
}
