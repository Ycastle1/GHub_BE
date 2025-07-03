package com.ce.back.controller;

import com.ce.back.entity.User;
import com.ce.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    // http://localhost:8080/api/users/register
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        boolean isRegistered = userService.registerUser(user);
        if (isRegistered) {
            return ResponseEntity.ok("회원가입 성공!");
        } else {
            return ResponseEntity.badRequest().body("이미 존재하는 사용자입니다.");
        }
    }

    // 아이디 중복 확인
    // http://localhost:8080/api/users/userMail-check
    @GetMapping("/userMail-check")
    public ResponseEntity<?> checkUser(@RequestParam String userMail) {
        if (userService.userMailCheck(userMail)) {
            return ResponseEntity.ok("사용자가 존재합니다.");
        } else {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }
    }

    // 로그인
    // http://localhost:8080/api/users/login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body) {
        String userMail = body.get("userMail");
        String password = body.get("password");

        if (userMail == null || password == null) {
            return ResponseEntity.badRequest().body("아이디와 비밀번호를 입력하세요.");
        }

        // userService.loginUser()에서 로그인한 사용자 객체를 가져옵니다.
        Optional<User> user = userService.loginUser(userMail, password);

        // 사용자 객체가 존재하면 성공 응답을 반환
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get()); // 로그인한 사용자 객체를 반환
        } else {
            // 사용자 정보가 일치하지 않으면 401 상태 반환
            return ResponseEntity.status(401).body("잘못된 아이디 또는 비밀번호입니다.");
        }
    }

    // 사용자 정보 확인
    // http://localhost:8080/api/users/check/{userMail}
    @GetMapping("/check/{userMail}")
    public ResponseEntity<?> confirmUser(@PathVariable String userMail) {
        try {
            Optional<User> user = userService.getUserByUserMail(userMail);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 사용자 정보 수정
    // http://localhost:8080/api/users/update
    @PostMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        boolean isUpdated = userService.updateUser(user);
        if (isUpdated) {
            return ResponseEntity.ok("사용자 정보가 성공적으로 수정되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("사용자를 찾을 수 없습니다.");
        }
    }

    // 사용자 선호 포지션 업데이트
    // http://localhost:8080/api/users/updatePositions
    @PostMapping("/updatePositions")
    public ResponseEntity<?> updatePositions(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        List<String> positions = (List<String>) body.get("positions");

        if (username == null || positions == null || positions.size() != 3) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "올바른 데이터를 입력하세요."));
        }

        boolean isUpdated = userService.updateUserPositions(username, positions);
        if (isUpdated) {
            return ResponseEntity.ok(Map.of("success", true, "message", "포지션이 성공적으로 변경되었습니다."));
        } else {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "사용자를 찾을 수 없습니다."));
        }
    }

    // 비밀번호 변경
    // http://localhost:8080/api/users/changePassword
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("newPassword");
        String passwordCheck = body.get("passwordCheck");

        if (password == null || passwordCheck == null) {
            return ResponseEntity.badRequest().body("비밀번호를 모두 입력하세요.");
        }

        if (!password.equals(passwordCheck)) {
            return ResponseEntity.badRequest().body("비밀번호가 동일하지 않습니다.");
        }

        boolean isPasswordChanged = userService.changePassword(username, password);
        if (isPasswordChanged) {
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            return ResponseEntity.status(404).body("사용자를 찾을 수 없습니다.");
        }
    }
}