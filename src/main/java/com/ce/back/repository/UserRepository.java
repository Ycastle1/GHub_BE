package com.ce.back.repository;

import com.ce.back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    // 사용자 이메일이 존재하는지 확인
    boolean existsByUserMail(String userMail);

    // 사용자 이메일과 비밀번호로 사용자 반환
    Optional<User> findUserByUserMailAndPassword(String userMail, String password);

    // 사용자 이메일로 사용자 반환
    Optional<User> findUserByUserMail(String userMail);

    // 사용자 이름으로 사용자 반환
    Optional<User> findUserByUserName(String userName);
}