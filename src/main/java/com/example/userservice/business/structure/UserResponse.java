package com.example.userservice.business.structure;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email
) {
}
