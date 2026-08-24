package br.com.fatec.fretec.controller.request;

public record UserRequest(
        String username,
        String password,
        String email,
        String cep
) {
}
