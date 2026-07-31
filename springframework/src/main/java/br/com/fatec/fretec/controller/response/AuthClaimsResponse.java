package br.com.fatec.fretec.controller.response;

import java.util.List;

public record AuthClaimsResponse(
        String userId,
        String username,
        List<String> roles
) {
}
