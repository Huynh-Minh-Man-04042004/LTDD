package com.hcmute.thuexe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcmute.thuexe.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccount_Username(String username);

    @Query("SELECT DISTINCT u FROM User u " +
            "WHERE (LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.account.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND u.userId <> :currentUserId")
    List<User> searchUsersByKeyword(@Param("keyword") String keyword,
                                    @Param("currentUserId") Long currentUserId);
}