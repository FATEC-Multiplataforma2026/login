package br.gov.sp.cps.controller.request;

public record LoginRequest(
        String username,
        String password
) {
}
