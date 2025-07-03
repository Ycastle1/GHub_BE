package com.ce.back.service;

import com.ce.back.entity.PRGame;
import com.ce.back.entity.Game;
import com.ce.back.repository.PRGameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PRGameService {

    private final PRGameRepository prGameRepository;

    @Autowired
    public PRGameService(PRGameRepository prGameRepository) {
        this.prGameRepository = prGameRepository;
    }

    // PRGame 생성
    public PRGame save(PRGame prGame) {
        return prGameRepository.save(prGame);
    }

    // prGameId로 PRGame 찾기
    public Optional<PRGame> findByPrGameId(Long prGameId) {
        return prGameRepository.findByPrGameId(prGameId);
    }

    // userMail로 PRGame 찾기
    public List<PRGame> findByUserMail(String userMail) {
        return prGameRepository.findByUser_UserMail(userMail);
    }

    // gameId로 PRGame 찾기
    public List<PRGame> findByGameId(Long gameId) {
        return prGameRepository.findByGame_GameId(gameId);
    }

    // PRGame 수정
    public PRGame update(PRGame prGame) {
        return prGameRepository.save(prGame);
    }

    // prGameId로 PRGame 찾기
    public Optional<PRGame> getPRGameById(Long prGameId) {
        return prGameRepository.findById(prGameId);
    }

    // PRGame 삭제
    public void delete(PRGame prGame) {
        prGameRepository.delete(prGame);
    }

    // Game과 관련된 PRGame들 모두 삭제
    @Transactional
    public void deletePRGamesByGame(Game game) {
        prGameRepository.deleteByGame(game);
    }
}