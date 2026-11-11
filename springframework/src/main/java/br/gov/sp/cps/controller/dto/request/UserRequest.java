package br.gov.sp.cps.controller.dto.request;

public record UserRequest(
        String username,
        String password,
        String email,
        String cep
) {
}
