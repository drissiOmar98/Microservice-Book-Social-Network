package com.omar.userserver.repositories;

import com.omar.userserver.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Integer> {

    Optional<Token> findByToken(String token);
}
