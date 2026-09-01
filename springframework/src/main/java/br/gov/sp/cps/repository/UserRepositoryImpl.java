package br.gov.sp.cps.repository;

import br.gov.sp.cps.entity.User;
import br.gov.sp.cps.repository.adapter.UserRepositoryImplAdapter;
import br.gov.sp.cps.repository.client.UserRepositoryWithMongodb;
import br.gov.sp.cps.repository.orm.UserOrm;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final PasswordEncoder encoder;
    private final UserRepositoryWithMongodb repository;

    public UserRepositoryImpl(
            PasswordEncoder encoder,
            UserRepositoryWithMongodb repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        try {
            Optional<UserOrm> optional = repository.findByUsername(user.username());
            if(optional.isPresent()) {
                throw new RuntimeException("Usuario já existe");
            }
            UserOrm orm = repository.save(UserRepositoryImplAdapter.cast(user, encoder));
            return UserRepositoryImplAdapter.cast(orm);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            Optional<UserOrm> optional = repository.findByUsername(username);
            if (optional.isEmpty()) {
                throw new UsernameNotFoundException("Usuario não encontrado");
            }
            return UserRepositoryImplAdapter.cast(optional.get());
        } catch (UsernameNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
