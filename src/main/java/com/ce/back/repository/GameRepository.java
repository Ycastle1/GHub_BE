package com.ce.back.repository;

import com.ce.back.entity.Game;
import com.ce.back.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    // 팀 이름으로 경기 찾기
    List<Game> findGamesByTeam_TeamName(String teamName);

    // 팀 ID로 경기 찾기
    List<Game> findGamesByTeam_TeamId(Long teamId);

    // 사용자 이메일로 경기 찾기
    List<Game> findGamesByPlayers_UserMail(String userMail);

    // 게임 ID로 경기 찾기
    Optional<Game> findGameByGameId(Long gameId);

    // 게임 이름으로 경기 찾기
    List<Game> findGamesByGameName(String gameName);

    // 팀에 속한 경기 삭제
    void deleteByTeam(Team team);

    List<Game> findAllByTeam(Team team);
}