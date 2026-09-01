package br.gov.sp.cps.entity;

import br.gov.sp.cps.entity.enumerable.UserRole;

import java.util.List;

public record User(
        String id,
        String username,
        String password,
        String email,
        String cep,
        List<UserRole> roles
) {
}