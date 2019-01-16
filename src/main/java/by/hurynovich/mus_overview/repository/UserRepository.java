package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.impl.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(final String email);
    UserEntity findByEmailAndPassword(final String email, final String password);
}