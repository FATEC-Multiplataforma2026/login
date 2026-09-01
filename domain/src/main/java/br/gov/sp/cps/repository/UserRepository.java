package br.gov.sp.cps.repository;

import br.gov.sp.cps.entity.User;

public interface UserRepository {
    User save(User user);

    User findByUsername(String username);
}
