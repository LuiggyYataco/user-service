package com.example.userservice.adapter.structure;

import lombok.Builder;

@Builder
public record UsuarioAdapterRequest(
        String name,
        String email
) {
}
