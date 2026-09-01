package br.gov.sp.cps.repository.adapter;

import br.gov.sp.cps.entity.User;
import br.gov.sp.cps.repository.orm.UserOrm;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRepositoryImplAdapter {
    private UserRepositoryImplAdapter() {
    }

    public static User cast(UserOrm orm) {
        return new User(
                orm.id(),
                orm.username(),
                orm.password(),
                orm.email(),
                orm.cep(),
                orm.roles());
    }

    public static UserOrm cast(User user, PasswordEncoder passwordEncoder) {
        return new UserOrm(
                user.id(),
                user.username(),
                passwordEncoder.encode(user.password()),
                user.email(),
                user.cep(),
                user.roles());
    }

}
