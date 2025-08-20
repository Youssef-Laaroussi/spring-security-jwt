package com.youssef.spring.security.repository;


import com.youssef.spring.security.model.Tokn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ToknRepository extends JpaRepository<Tokn, Long> {





    // Query optimisée
    @Query("SELECT t FROM Tokn t WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false")
    List<Tokn> findAllValidTokensByUser(Long userId);

    Optional<Tokn> findByToken(String token);


}
