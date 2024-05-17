package com.example.DayBridge.repository;

import com.example.DayBridge.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findById(Long id);
    boolean existsByUserID(String userID);
    boolean existsByNickName(String nickName);
    Optional<Users> findByUserID(String userID);
}
