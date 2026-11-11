package br.gov.sp.cps.controller.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
