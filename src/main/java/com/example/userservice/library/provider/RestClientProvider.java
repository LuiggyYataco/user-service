package com.example.userservice.library.provider;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RestClientProvider {
    private final WebClient webClient;

    public RestClientProvider(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    public <T> Mono<T> get(String url, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T, R> Mono<R> post(String url, T request, Class<R> responseType) {
        return webClient.post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T> Mono<T> delete(String url, Class<T> responseType) {
        return webClient.delete()
                .uri(url)
                .retrieve()
                .bodyToMono(responseType);
    }
}
