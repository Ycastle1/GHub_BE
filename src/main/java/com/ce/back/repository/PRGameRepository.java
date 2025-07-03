package com.ce.back.repository;

import com.ce.back.entity.Game;
import com.ce.back.entity.PRGame;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PRGameRepository extends JpaRepository<PRGame, Long> {

    // PRGameId로 조회
    Optional<PRGame> findByPrGameId(Long prGameId);

    // Game 객체의 ID로 조회
    List<PRGame> findByGame_GameId(Long gameId); // ✅ 수정

    // User 객체의 이메일로 조회
    List<PRGame> findByUser_UserMail(String userMail); // ✅ 수정

    // 게임 이름으로 경기 찾기
    List<PRGame> findByPrGameName(String prGameName);

    // 특정 Game에 해당하는 PRGame 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM PRGame p WHERE p.game = :game")
    void deleteByGame(@Param("game") Game game);
}