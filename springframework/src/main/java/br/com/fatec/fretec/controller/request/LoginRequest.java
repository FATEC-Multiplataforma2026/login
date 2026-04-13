package br.com.fatec.fretec.controller.request;

public record LoginRequest(
        String username,
        String password
) {
}
