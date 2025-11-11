package com.example.userservice.adapter.structure;

import lombok.Builder;

@Builder
public record UserAdapterResponse(
        Long id,
        String name,
        String email
) {
}
