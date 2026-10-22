package com.example.demo.repository;

import com.example.demo.dto.user.UserProfile;
import com.example.demo.dto.user.UserSummary;
import com.example.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    Users findByUsername(String username);
    Users findByEmail(String email);
    Users findByDisplayName(String DisplayName);
    @Query(value ="""
            select u.id,u.display_name,u.avatar_url
            from Users as u
            where u.id=:userId""",nativeQuery = true)
    UserSummary findUsersById(@Param("userId") Long id);
    @Query(value ="""
            select u.id,u.display_name,u.email,u.phone
            from Users as u
            where u.id=:userId""",nativeQuery = true)
    UserProfile findUserProfilesById(@Param("userId") Long id);
    List<Users> findByDisplayNameContaining(String displayName);
     @Query(value ="""
            select u.*
            from Users as u
            where u.id=:userId""",nativeQuery = true)
    Users findUserById(@Param("userId") Long id);
}
