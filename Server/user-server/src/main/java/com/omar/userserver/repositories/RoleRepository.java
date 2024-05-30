package com.omar.userserver.repositories;

import com.omar.userserver.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role ,Integer> {

    Optional<Role> findByName(String role);
}
