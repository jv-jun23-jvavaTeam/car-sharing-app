package com.jvavateam.carsharingapp.repository.user;

import com.jvavateam.carsharingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
