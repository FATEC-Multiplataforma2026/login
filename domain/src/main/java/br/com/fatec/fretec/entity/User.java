package br.com.fatec.fretec.entity;

import br.com.fatec.fretec.entity.enumerable.UserRole;

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