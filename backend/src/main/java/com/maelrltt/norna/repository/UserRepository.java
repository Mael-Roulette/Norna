package com.maelrltt.norna.repository;

import com.maelrltt.norna.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
