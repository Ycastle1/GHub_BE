package com.ce.back.service;


import com.ce.back.entity.User;
import com.ce.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    public boolean registerUser(User user) {
        if (userRepository.existsByUserMail(user.getUserMail())) {
            return false; // 이미 존재하는 사용자
        }
        userRepository.save(user);
        return true;
    }

    // 메일 존재 체크
    public boolean userMailCheck(@RequestParam String userMail) {
        return userRepository.existsByUserMail(userMail);
    }

    // 로그인
    public Optional<User> loginUser(String userMail, String password) {
        return userRepository.findUserByUserMailAndPassword(userMail, password);
    }

    // 사용자 정보 수정
    public boolean updateUser(User user) {
        Optional<User> userOptional = userRepository.findUserByUserMail(user.getUserMail());
        if (userOptional.isPresent()) {
            userRepository.save(user);
            return true;
        }
        return false; // 사용자 없음
    }

    // 사용자 선호 포지션 수정
    public boolean updateUserPositions(String userMail, List<String> positions) {
        if (positions.size() != 3) {
            return false; // 포지션이 3개가 아니면 불가
        }
        Optional<User> userOptional = userRepository.findUserByUserMail(userMail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstPosition(positions.get(0));
            user.setSecondPosition(positions.get(1));
            user.setThirdPosition(positions.get(2));
            userRepository.save(user);
            return true;
        }
        return false; // 사용자 없음
    }

    // 비밀번호 변경
    public boolean changePassword(String userMail, String newPassword) {
        Optional<User> userOptional = userRepository.findUserByUserMail(userMail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false; // 사용자 없음
    }

    public Optional<User> getUserByUserMail(String userMail) {
        return userRepository.findUserByUserMail(userMail);
    }
}