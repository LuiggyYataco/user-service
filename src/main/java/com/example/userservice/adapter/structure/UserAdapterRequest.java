package com.example.userservice.adapter.structure;

import lombok.Builder;

@Builder
public record UserAdapterRequest(
        String name,
        String email
) {
}
