package com.ce.back.controller;

import com.ce.back.entity.Game;
import com.ce.back.entity.PRGame;
import com.ce.back.entity.Team;
import com.ce.back.service.GameService;
import com.ce.back.service.PRGameService;
import com.ce.back.service.TeamService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final TeamService teamService;
    private final PRGameService prGameService;

    @Autowired
    public GameController(GameService gameService, TeamService teamService, PRGameService prGameService) {
        this.gameService = gameService;
        this.teamService = teamService;
        this.prGameService = prGameService;
    }

    // 특정 팀의 경기 일정을 조회하는 메서드
    // http://localhost:8080/api/games/team/{teamId}
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Game>> getGameByTeamId(@PathVariable Long teamId) {
        List<Game> games = gameService.getGamesByTeamId(teamId);
        if (games.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(games);
    }

    // 특정 사용자가 참가하는 경기 목록 출력
    // http://localhost:8080/api/games/team/{teamId}
    @GetMapping("/user/{userMail}")
    public ResponseEntity<List<Game>> getGameByUser(@PathVariable String userMail) {
        List<Game> games = gameService.getGamesByUserMail(userMail);
        if (games.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(games);
    }

    // 경기 생성
    // http://localhost:8080/api/games/create-game
    @PostMapping("/create-game")
    public ResponseEntity<?> createGame(@RequestParam("gameName") String gameName,
                                        @RequestParam("startDate") String startDate,
                                        @RequestParam("versus") String versus,
                                        @RequestParam("teamId") Long teamId,
                                        @RequestParam("oppoLogo") MultipartFile oppoLogo) {
        try {
            // 상대 팀 로고 파일 저장
            String logoFileName = gameService.saveLogoFile(oppoLogo);

            // Team 객체를 팀 ID로 조회
            Team team = teamService.getTeamByTeamId(teamId).orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

            // Game 객체 생성
            Game game = Game.builder()
                    .gameName(gameName)
                    .date(LocalDateTime.parse(startDate)) // startDate를 LocalDateTime으로 변환
                    .versus(versus)
                    .team(team)
                    .oppoLogo(logoFileName) // 상대팀 로고 파일명
                    .build();

            // 게임 생성 서비스 호출
            Game savedGame = gameService.createGame(game);

            return ResponseEntity.ok(savedGame);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 경기 삭제
    // http://localhost:8080/api/games/delete-game
    @Transactional
    @DeleteMapping("/delete-game")
    public ResponseEntity<?> deleteGame(@RequestBody Map<String, Long> body) {
        try {
            Long gameId = body.get("gameId");

            // 먼저 관련된 PRGame들 삭제
            Game game = gameService.getGameByGameId(gameId)
                    .orElseThrow(() -> new RuntimeException("게임을 찾을 수 없습니다."));
            prGameService.deletePRGamesByGame(game);
            // 경기 ID로 게임을 찾고, 게임 삭제
            gameService.deleteGame(gameId); // gameService에서 deleteGame 메소드 호출

            // 성공적으로 삭제된 게임 정보 반환
            return ResponseEntity.ok("경기가 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            // 예외 발생 시 404 에러 반환 (경기 관련 예외 처리)
            return ResponseEntity.status(404).body("경기를 삭제할 수 없습니다: " + e.getMessage());
        }
    }

    // 경기에서의 포지션을 변경
    // http://localhost:8080/api/games/update-game
    @PostMapping("/update-game")
    public ResponseEntity<?> changePosition(@RequestBody Game game) {
        try {
            Game savedGame = gameService.updateGame(game);
            return ResponseEntity.ok(savedGame);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 경기 Id로 특정 경기 조회
    // http://localhost:8080/api/games/saved-formation
    @GetMapping("/saved-formation/{gameId}")
    public ResponseEntity<?> getSavedFormation(@PathVariable Long gameId) {
        try {
            Optional<Game> game = gameService.getGameByGameId(gameId);
            return ResponseEntity.ok(game);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 경기에 사용자 추가
    // http://localhost:8080/api/games/{gameId}/insert-to-game
    @PostMapping("/{gameId}/insert-to-game")
    public ResponseEntity<?> insertUserToGame(@PathVariable Long gameId, @RequestBody Map<String, String> body) {
        try {
            String userMail = body.get("userMail");
            // 게임 ID로 게임을 찾고, 해당 게임에 유저 추가
            gameService.insertUserToGame(gameId, userMail); // gameId로 게임을 찾고 userMail로 사용자 추가

            // 성공적으로 추가된 게임 정보 반환
            return ResponseEntity.ok("게임에 사용자가 성공적으로 추가되었습니다.");
        } catch (RuntimeException e) {
            // 예외 발생 시 404 에러 반환 (사용자 또는 경기 관련 예외 처리)
            return ResponseEntity.status(404).body("게임에 사용자를 추가할 수 없습니다: " + e.getMessage());
        }
    }

    // 경기에 사용자 삭제
    // http://localhost:8080/api/games/{gameId}/remove-from-game
    @DeleteMapping("/{gameId}/remove-from-game")
    public ResponseEntity<?> removeUserFromGame(@PathVariable Long gameId, @RequestBody Map<String, String> body) {
        try {
            String userMail = body.get("userMail");

            // 게임 ID로 게임을 찾고, 해당 게임에 유저 삭제
            gameService.removeUserFromGame(gameId, userMail); // gameId로 게임을 찾고 userMail로 사용자 삭제

            // 성공적으로 삭제된 게임 정보 반환
            return ResponseEntity.ok("게임에서 사용자가 성공적으로 삭제되었습니다.");
        } catch (RuntimeException e) {
            // 예외 발생 시 404 에러 반환 (사용자 또는 경기 관련 예외 처리)
            return ResponseEntity.status(404).body("게임에서 사용자를 삭제할 수 없습니다: " + e.getMessage());
        }
    }

    // PRGame → Game 포지션 업데이트
    // http://localhost:8080/api/games/change-from-pr-to-game
    @PostMapping("/change-from-pr-to-game")
    public ResponseEntity<?> changeToPr(@RequestBody Map<String, String> body) {
        try {
            Long prGameId = Long.parseLong(body.get("prGameId"));

            Optional<PRGame> prGameOpt = prGameService.getPRGameById(prGameId);
            if (prGameOpt.isEmpty()) {
                return ResponseEntity.status(404).body("해당 PRGame ID는 존재하지 않습니다.");
            }
            PRGame prGame = prGameOpt.get();
            Game game = prGame.getGame();

            game.setGk(prGame.getGk());
            game.setLb(prGame.getLb());
            game.setLcb(prGame.getLcb());
            game.setSw(prGame.getSw());
            game.setRcb(prGame.getRcb());
            game.setRb(prGame.getRb());
            game.setLwb(prGame.getLwb());
            game.setLdm(prGame.getLdm());
            game.setCdm(prGame.getCdm());
            game.setRdm(prGame.getRdm());
            game.setRwb(prGame.getRwb());
            game.setLm(prGame.getLm());
            game.setLcm(prGame.getLcm());
            game.setCm(prGame.getCm());
            game.setRcm(prGame.getRcm());
            game.setRm(prGame.getRm());
            game.setLam(prGame.getLam());
            game.setCam(prGame.getCam());
            game.setRam(prGame.getRam());
            game.setLw(prGame.getLw());
            game.setCf(prGame.getCf());
            game.setRw(prGame.getRw());
            game.setLs(prGame.getLs());
            game.setRs(prGame.getRs());
            game.setSt(prGame.getSt());

            Game updated = gameService.updateGame(game);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("PRGame → Game 변환 실패: " + e.getMessage());
        }
    }
}