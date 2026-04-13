package br.com.fatec.fretec.repository;

import br.com.fatec.fretec.entity.User;

public interface UserRepository {
    User save(User user);

    User findByUsername(String username);
}
