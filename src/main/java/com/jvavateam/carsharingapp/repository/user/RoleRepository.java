package com.jvavateam.carsharingapp.repository.user;

import com.jvavateam.carsharingapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getRoleByName(Role.RoleName roleName);
}
