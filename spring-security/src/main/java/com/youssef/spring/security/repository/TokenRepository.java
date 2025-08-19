package com.youssef.spring.security.repository;


import ch.qos.logback.core.subst.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
