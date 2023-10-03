package com.jvavateam.carsharingapp.repository.user;

import com.jvavateam.carsharingapp.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
