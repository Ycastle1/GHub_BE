package com.ce.back.controller;

import com.ce.back.entity.Team;
import com.ce.back.entity.User;
import com.ce.back.service.TeamService;
import com.ce.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    @Autowired
    public TeamController(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    // 팀 전체 목록 조회
    // http://localhost:8080/api/teams/
    @GetMapping("/")
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    // 팀 Id로 특정 팀 조회
    // http://localhost:8080/api/teams/{teamId}
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeamByTeamId(@PathVariable Long teamId) {
        try {
            Optional<Team> team = teamService.getTeamByTeamId(teamId);
            return ResponseEntity.ok(team);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 특정 팀 이름이 포함된 모든 팀 조회
    // http://localhost:8080/api/teams/name/{teamName}
    @GetMapping("/name/{teamName}")
    public ResponseEntity<List<Team>> getTeamsByTeamName(@PathVariable String teamName) {
        List<Team> teams = teamService.getTeamsByTeamName(teamName);
        return ResponseEntity.ok(teams);
    }

    // 특정 사용자 메일로, 사용자가 포함된 모든 팀 조회
    // http://localhost:8080/api/teams/name/{userMail}
    @GetMapping("/mail/{userMail}")
    public ResponseEntity<List<Team>> getTeamsByUserMail(@PathVariable String userMail) {
        List<Team> teams = teamService.getTeamsByUserMail(userMail);
        if (teams.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teams);
    }

    // 새로운 팀 추가
    // http://localhost:8080/api/teams/create-team
    @PostMapping("/create-team")
    public ResponseEntity<?> createTeam(@RequestPart("team") Team team, @RequestPart("logo") MultipartFile logo) {
        try {
            // 로고 파일을 함께 저장하고 팀 생성
            Team newTeam = teamService.createTeam(team, logo);
            return ResponseEntity.ok(newTeam);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("로고 업로드 실패: " + e.getMessage());
        }
    }

    // 팀 삭제
    // http://localhost:8080/api/teams/delete-team
    @DeleteMapping("/delete-team")
    public ResponseEntity<?> deleteTeam(@RequestBody Map<String, Long> body) {
        try {
            Long teamId = body.get("teamId");

            // 팀 ID로 팀을 찾고, 팀 삭제
            teamService.deleteTeam(teamId); // teamService에서 deleteTeam 메소드 호출

            // 성공적으로 삭제된 팀 정보 반환
            return ResponseEntity.ok("팀이 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            // 예외 발생 시 404 에러 반환 (팀 관련 예외 처리)
            return ResponseEntity.status(404).body("팀을 삭제할 수 없습니다: " + e.getMessage());
        }
    }

    // 팀 정보 수정
    // http://localhost:8080/api/teams/update-team
    @PostMapping("/update-team")
    public ResponseEntity<?> updateTeam(@RequestBody Team team) {
        try {
            Team updatedTeam = teamService.updateTeam(team);
            return ResponseEntity.ok(updatedTeam);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 로고 파일 수정
    @PostMapping("/{teamId}/upload-logo")
    public ResponseEntity<?> uploadTeamLogo(@PathVariable Long teamId, @RequestParam("file") MultipartFile file) {
        try {
            // 로고 파일 저장
            String logoFileName = teamService.saveLogoFile(file);

            // 로고 파일 경로를 팀 객체에 저장
            Team existingTeam = teamService.getTeamByTeamId(teamId).orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));
            existingTeam.setLogo(logoFileName);

            // 팀 업데이트
            teamService.updateTeam(existingTeam);

            return ResponseEntity.ok(existingTeam);
        } catch (IOException | RuntimeException e) {
            return ResponseEntity.status(500).body("로고 업로드 실패: " + e.getMessage());
        }
    }

    // 특정 팀에 속한 사용자들 조회
    // http://localhost:8080/api/teams/{teamId}/users-in-team
    @GetMapping("/{teamId}/users-in-team")
    public ResponseEntity<?> getUsers(@PathVariable Long teamId) {
        try {
            // 팀을 서비스에서 조회
            Optional<Team> teamOptional = teamService.getTeamByTeamId(teamId);

            // Optional에서 Team 객체를 꺼내고, 그 후 getUsers() 호출
            if (teamOptional.isPresent()) {
                Team team = teamOptional.get();  // team 객체 추출
                List<User> users = team.getUsers();  // 이제 team.getUsers() 호출 가능
                return ResponseEntity.ok(users);  // 사용자 목록 반환
            } else {
                return ResponseEntity.status(404).body("팀을 찾을 수 없습니다.");
            }
        } catch (RuntimeException e) {
            // 예외 발생 시 처리
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 특정 팀에 사용자 추가
    // http://localhost:8080/api/teams/{teamId}/add-user
    @PostMapping("/{teamId}/add-user")
    public ResponseEntity<?> addUserToTeam(@PathVariable Long teamId, @RequestBody Map<String, String> body) {
        try {
            String userMail = body.get("userMail");

            // 팀 조회
            Team team = teamService.getTeamByTeamId(teamId)
                    .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

            // 사용자 조회 (userMail로)
            User user = userService.getUserByUserMail(userMail)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 팀에 사용자 추가
            team.getUsers().add(user);

            teamService.updateTeam(team); // 팀 정보 업데이트

            return ResponseEntity.ok("사용자가 팀에 성공적으로 추가되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("사용자 추가 실패: " + e.getMessage());
        }
    }

    // 특정 팀에 속한 사용자 제거
    // http://localhost:8080/api/teams/{teamId}/remove-user
    @DeleteMapping("/{teamId}/remove-user")
    public ResponseEntity<?> removeUserFromTeam(@PathVariable Long teamId, @RequestBody Map<String, String> body) {
        try {
            String userMail = body.get("userMail");

            // 팀 조회
            Team team = teamService.getTeamByTeamId(teamId)
                    .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

            // 사용자 조회 (userMail로)
            User user = userService.getUserByUserMail(userMail)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            // 팀에서 사용자 제거
            team.getUsers().remove(user);

            teamService.updateTeam(team); // 팀 정보 업데이트

            return ResponseEntity.ok("사용자가 팀에서 성공적으로 제거되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("사용자 제거 실패: " + e.getMessage());
        }
    }

    // 팀 매니저 양도
    // http://localhost:8080/api/teams/{teamId}/transfer-manager
    @PostMapping("/{teamId}/transfer-manager")
    public ResponseEntity<?> transferTeamManager(@PathVariable Long teamId, @RequestBody Map<String, String> body) {
        String currentManagerMail = body.get("currentManagerMail");
        String newManagerMail = body.get("newManagerMail");

        try {
            Team updatedTeam = teamService.transferTeamManager(teamId, currentManagerMail, newManagerMail);
            return ResponseEntity.ok(updatedTeam);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("팀 매니저 직함 양도 실패: " + e.getMessage());
        }
    }
}