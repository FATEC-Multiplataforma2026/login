package br.com.fatec.fretec.controller.request;

import br.com.fatec.fretec.entity.enumerable.UserRole;

import java.util.List;

public record UserRequest(
        String username,
        String password,
        String email,
        String cep,
        List<UserRole> roles
) {
}
