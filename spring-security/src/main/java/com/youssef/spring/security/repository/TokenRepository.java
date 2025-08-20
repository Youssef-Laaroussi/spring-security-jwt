package com.youssef.spring.security.repository;


import com.youssef.spring.security.model.Tokn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Tokn, Long> {


    @Query("""
        select t from Tokn t inner join User u on t.user.id = u.id
        where u.id = :userId and t.expired = false and t.revoked = false
        """)
    List<Tokn> findAllValidTokensByUser(Long userId );

     Optional<Tokn> findByToken(String token);



}
