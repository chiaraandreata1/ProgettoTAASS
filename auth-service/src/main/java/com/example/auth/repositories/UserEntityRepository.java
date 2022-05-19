package com.example.auth.repositories;

import com.example.auth.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    List<UserEntity> findUserEntitiesByEmailContainingIgnoreCase(String query);

    List<UserEntity> findUserEntitiesByEmailContainingIgnoreCaseAndEmailNotIn(String query, List<String> excluded);

}