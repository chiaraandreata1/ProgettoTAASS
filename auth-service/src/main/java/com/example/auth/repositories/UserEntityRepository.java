package com.example.auth.repositories;

import com.example.auth.models.UserEntity;
import com.example.shared.models.users.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    List<UserEntity> findUserEntitiesByEmailContainingIgnoreCase(String query);

    List<UserEntity> findUserEntitiesByEmailContainingIgnoreCaseAndTypeIs(String query, UserType type);

    List<UserEntity> findUserEntitiesByEmailContainingIgnoreCaseAndEmailNotIn(String query, List<String> excluded);

    List<UserEntity> findUserEntitiesByEmailContainingIgnoreCaseAndIdNotIn(String partEmail, List<Long> excluded);

    List<UserEntity> findUserEntitiesByEmailContainingIgnoreCaseAndTypeIsAndIdNotIn(String query, UserType type, List<Long> excluded);
}