package com.jvavateam.carsharingapp.repository.user;

import com.jvavateam.carsharingapp.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph("User.roles")
    Optional<User> findByEmail(String email);

    @Query("FROM User u WHERE u.id = ?#{ principal?.id }")
    User getCurrentUser();
}
