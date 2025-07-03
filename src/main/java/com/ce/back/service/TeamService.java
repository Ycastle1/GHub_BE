package com.ce.back.service;

import com.ce.back.entity.Team;
import com.ce.back.entity.User;
import com.ce.back.repository.TeamRepository;
import com.ce.back.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final GameService gameService;
    private final CommunityService communityService;

    // 로고 파일을 저장할 경로 (application.properties에서 설정)
    @Value("${team.logo.directory}")
    private String logoDirectory;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository, GameService gameService,
            CommunityService communityService) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.gameService = gameService;
        this.communityService = communityService;
    }

    // 모든 팀 조회
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // 특정 팀 이름이 포함된 모든 팀 조회
    public List<Team> getTeamsByTeamName(String teamName) {
        return teamRepository.findTeamsByTeamName(teamName);
    }

    // 팀 검색 : 팀 식별자로 조회
    public Optional<Team> getTeamByTeamId(Long teamId) {
        Optional<Team> existingTeam = teamRepository.findTeamByTeamId(teamId);

        // isEmpty()를 사용하여 값이 없는 경우 처리
        if (existingTeam.isEmpty()) {
            throw new RuntimeException("경기를 찾을 수 없습니다.");
        }

        return existingTeam;
    }

    // 팀 검색 : 사용자 메일로 조회
    public List<Team> getTeamsByUserMail(String userMail) {
        return teamRepository.findByUsersUserMail(userMail);
    }

    // 팀 생성 메소드 수정 (로고 파일 처리 추가)
    public Team createTeam(Team team, MultipartFile logoFile) throws IOException {
        // 로고 파일 저장
        String logoFileName = saveLogoFile(logoFile); // 로고 파일을 저장하고 파일명을 반환
        team.setLogo(logoFileName); // 팀 객체에 로고 파일명 설정

        User manager = team.getTeamManager();
        if (!team.getUsers().contains(manager)) {
            team.getUsers().add(manager);
        }
        return teamRepository.save(team); // 새로운 팀 저장
    }

    // 팀 삭제
    @Transactional
    public void deleteTeam(Long teamId) {
        // 팀 존재 여부 확인
        Optional<Team> existingTeam = teamRepository.findTeamByTeamId(teamId);
        if (existingTeam.isEmpty()) {
            throw new RuntimeException("팀을 찾을 수 없습니다.");
        }

        Team team = existingTeam.get();

        // 팀의 로고 파일 경로 확인 및 삭제
        if (team.getLogo() != null && !team.getLogo().isEmpty()) {
            Path logoPath = Paths.get(logoDirectory, team.getLogo());
            try {
                Files.deleteIfExists(logoPath); // 로고 파일 삭제
            } catch (IOException e) {
                throw new RuntimeException("로고 파일을 삭제할 수 없습니다: " + e.getMessage());
            }
        }

        // 해당 팀이 작성한 게시글 삭제
        communityService.deleteByTeamId(teamId);

        // 해당 팀에 속한 모든 게임 삭제
        gameService.deleteGamesByTeamId(teamId);

        // 팀 삭제
        teamRepository.delete(team); // 팀 삭제
    }

    // 팀 정보 업데이트
    public Team updateTeam(Team team) {
        // 팀이 존재하는지 확인
        Team existingTeam = teamRepository.findTeamByTeamId(team.getTeamId())
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        // 기존 팀 정보 업데이트
        existingTeam.setTeamName(team.getTeamName());
        existingTeam.setLocation(team.getLocation());
        existingTeam.setFirstColor(team.getFirstColor());
        existingTeam.setSecondColor(team.getSecondColor());
        // 기타 필요한 필드 업데이트

        return teamRepository.save(existingTeam); // 업데이트된 팀 저장
    }

    // 로고 파일 저장 메소드
    public String saveLogoFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("로고 파일이 비어 있습니다.");
        }

        // 파일명 생성 (중복 방지)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // 파일을 지정된 경로에 저장
        Path path = Paths.get(logoDirectory, fileName);

        // 디렉토리가 없으면 생성
        Files.createDirectories(path.getParent());

        // 파일 복사
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return fileName; // 파일명 반환
    }

    // 팀 매니저 직함 양도
    @Transactional
    public Team transferTeamManager(Long teamId, String currentManagerMail, String newManagerMail) {
        // 팀 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        // 현재 매니저 확인
        if (!team.getTeamManager().getUserMail().equals(currentManagerMail)) {
            throw new RuntimeException("현재 매니저가 아닙니다.");
        }

        // 새로운 매니저 확인
        User newManager = userRepository.findUserByUserMail(newManagerMail)
                .orElseThrow(() -> new RuntimeException("새로운 매니저를 찾을 수 없습니다."));

        // 새로운 매니저가 해당 팀에 소속되어 있는지 확인
        if (!team.getUsers().contains(newManager)) {
            team.getUsers().add(newManager);
        }

        // 팀 매니저 직함 양도
        team.setTeamManager(newManager);

        // 변경된 팀 정보 저장
        return teamRepository.save(team);
    }
}