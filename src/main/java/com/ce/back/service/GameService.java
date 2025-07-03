package com.ce.back.service;

import com.ce.back.entity.Game;
import com.ce.back.entity.Team;
import com.ce.back.entity.User;
import com.ce.back.repository.GameRepository;
import com.ce.back.repository.TeamRepository;
import com.ce.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PRGameService prGameService;

    // 로고 파일을 저장할 경로 (application.properties에서 설정)
    @Value("${team.logo.directory}")
    private String logoDirectory;

    @Autowired
    public GameService(GameRepository gameRepository, UserRepository userRepository, TeamRepository teamRepository,
            PRGameService prGameService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.prGameService = prGameService;
    }

    // 팀 이름으로 경기 찾기
    public List<Game> getGamesByTeamName(String teamName) {
        return gameRepository.findGamesByTeam_TeamName(teamName);
    }

    // 팀 ID로 경기 찾기
    public List<Game> getGamesByTeamId(Long teamId) {
        return gameRepository.findGamesByTeam_TeamId(teamId);
    }

    // 사용자 이메일로 포지션 조회
    public List<Game> getGamesByUserMail(String userMail) {
        return gameRepository.findGamesByPlayers_UserMail(userMail);
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

    // 경기 생성
    public Game createGame(Game game) {
        // 경기 중복 체크(게임 이름과 팀을 기준으로)
        Optional<Game> existingGame = gameRepository.findGamesByGameName(game.getGameName())
                .stream()
                .filter(g -> g.getTeam().equals(game.getTeam())) // 같은 팀의 동일한 경기라면 중복 처리
                .findFirst();

        if (existingGame.isPresent()) {
            throw new RuntimeException("이미 존재하는 경기입니다.");
        }

        return gameRepository.save(game);
    }

    // 경기 삭제
    public void deleteGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        prGameService.deletePRGamesByGame(game); // 🔥 영속된 Game 객체 전달
        gameRepository.delete(game); // 이제 안전하게 삭제
    }

    // 특정 팀에 속한 모든 게임 삭제
    public void deleteGamesByTeamId(Long teamId) {
        Team team = teamRepository.findTeamByTeamId(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        // 1. 팀에 소속된 모든 게임 조회
        List<Game> games = gameRepository.findAllByTeam(team);

        // 2. 각 게임에 연결된 PRGame 삭제
        for (Game game : games) {
            prGameService.deletePRGamesByGame(game);
        }

        // 3. 게임 삭제
        gameRepository.deleteByTeam(team);
    }

    // 경기 업데이트
    public Game updateGame(Game game) {
        Optional<Game> existingGame = gameRepository.findGameByGameId(game.getGameId());

        // isEmpty()를 사용하여 값이 없는 경우 처리
        if (existingGame.isEmpty()) {
            throw new RuntimeException("경기를 찾을 수 없습니다.");
        }

        return gameRepository.save(game);
    }

    // 경기 조회
    public Optional<Game> getGameByGameId(Long gameId) {
        Optional<Game> existingGame = gameRepository.findGameByGameId(gameId);

        // isEmpty()를 사용하여 값이 없는 경우 처리
        if (existingGame.isEmpty()) {
            throw new RuntimeException("경기를 찾을 수 없습니다.");
        }

        return existingGame;
    }

    // 경기에 참여하는 인원 추가
    public void insertUserToGame(Long gameId, String userMail) {

        // 게임이 존재하는지 확인
        Optional<Game> existingGame = gameRepository.findGameByGameId(gameId);
        Optional<User> existingUser = userRepository.findUserByUserMail(userMail);

        if (existingGame.isEmpty()) {
            throw new RuntimeException("경기가 존재하지 않습니다."); // 경기가 없으면 예외 처리
        }
        if (existingUser.isEmpty()) {
            throw new RuntimeException("사용자가 존재하지 않습니다.");
        }

        Game game = existingGame.get(); // 존재하는 경기를 가져옴
        User user = existingUser.get();

        // 경기 참가 선수 목록에 해당 사용자가 이미 존재하는지 확인
        if (game.getPlayers().contains(user)) {
            throw new RuntimeException("이 사용자는 이미 경기에 참여하고 있습니다."); // 이미 참여한 경우 예외 처리
        }

        // 사용자 추가
        game.getPlayers().add(user); // 경기 참가 선수 목록에 사용자를 추가

        // 게임 정보 업데이트 (저장)
        gameRepository.save(game); // 경기 정보 업데이트
    }

    // 경기에 참여하는 선수 삭제
    public void removeUserFromGame(Long gameId, String userMail) {

        // 게임이 존재하는지 확인
        Optional<Game> existingGame = gameRepository.findGameByGameId(gameId);
        Optional<User> existingUser = userRepository.findUserByUserMail(userMail);

        if (existingGame.isEmpty()) {
            throw new RuntimeException("경기가 존재하지 않습니다.");
        }
        if (existingUser.isEmpty()) {
            throw new RuntimeException("사용자가 존재하지 않습니다.");
        }

        Game game = existingGame.get();
        User user = existingUser.get();

        // 경기 참가 선수 목록에서 해당 사용자가 있는지 확인
        if (!game.getPlayers().contains(user)) {
            throw new RuntimeException("이 사용자는 해당 경기에서 찾을 수 없습니다.");
        }

        // 사용자 삭제
        game.getPlayers().remove(user); // 경기 참가 선수 목록에서 해당 사용자 제거

        // 게임 정보 업데이트 (저장)
        gameRepository.save(game); // 경기 정보 업데이트
    }
}