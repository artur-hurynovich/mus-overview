package by.hurynovich.mus_overview.repository;

import by.hurynovich.mus_overview.entity.impl.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(final String email);
    List<UserEntity> findByEmailContaining(final String email);
    long countByEmailContaining(final String email);
}