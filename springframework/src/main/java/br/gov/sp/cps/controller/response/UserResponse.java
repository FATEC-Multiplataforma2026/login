package br.gov.sp.cps.controller.response;

import br.gov.sp.cps.entity.enumerable.UserRole;

import java.util.List;

public record UserResponse(
        String id,
        String username,
        String email,
        String cep,
        List<UserRole> roles
) {
}