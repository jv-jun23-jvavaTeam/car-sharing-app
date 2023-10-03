package com.jvavateam.carsharingapp.repository;

import com.jvavateam.carsharingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
