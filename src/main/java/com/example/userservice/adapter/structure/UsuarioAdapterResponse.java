package com.example.userservice.adapter.structure;

import lombok.Builder;

@Builder
public record UsuarioAdapterResponse(
        Long id,
        String name,
        String email
) {
}
