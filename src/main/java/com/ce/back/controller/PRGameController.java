package com.ce.back.controller;

import com.ce.back.entity.PRGame;
import com.ce.back.service.PRGameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pr-games")
public class PRGameController {

    private final PRGameService prGameService;

    @Autowired
    public PRGameController(PRGameService prGameService) {
        this.prGameService = prGameService;
    }

    // PRGame 생성
    // POST /api/pr-games/create
    @PostMapping("/create")
    public ResponseEntity<PRGame> createPRGame(@RequestBody PRGame prGame) {
        return new ResponseEntity<>(prGameService.save(prGame), HttpStatus.CREATED);
    }


    // PRGameId로 PRGame 조회
    // GET /api/pr-games/findByPRGameId/{prGameId}
    @GetMapping("/findByPRGameId/{prGameId}")
    public ResponseEntity<Optional<PRGame>> findByPrGameId(@PathVariable Long prGameId) {
        return new ResponseEntity<>(prGameService.findByPrGameId(prGameId), HttpStatus.OK);
    }

    // 사용자 이메일로 PRGame 목록 조회
    // GET /api/pr-games/findByUserMail/{userMail}
    @GetMapping("/findByUserMail/{userMail}")
    public ResponseEntity<List<PRGame>> findByUserMail(@PathVariable String userMail) {
        return new ResponseEntity<>(prGameService.findByUserMail(userMail), HttpStatus.OK);
    }

    // 게임 ID로 PRGame 목록 조회
    // GET /api/pr-games/findByGameId/{gameId}
    @GetMapping("/findByGameId/{gameId}")
    public ResponseEntity<List<PRGame>> findByGameId(@PathVariable Long gameId) {
        return new ResponseEntity<>(prGameService.findByGameId(gameId), HttpStatus.OK);
    }

    // PRGame 정보 수정
    // PUT /api/pr-games/update
    @PutMapping("/update")
    public ResponseEntity<PRGame> updatePRGame(@RequestBody PRGame prGame) {
        return new ResponseEntity<>(prGameService.update(prGame), HttpStatus.OK);
    }

    // PRGame 삭제
    // DELETE /api/pr-games/remove/{prGameId}
    @DeleteMapping("/remove/{prGameId}")
    public ResponseEntity<Void> deletePRGame(@PathVariable Long prGameId) {
        prGameService.getPRGameById(prGameId)
            .ifPresentOrElse(
                prGameService::delete,
                () -> { throw new RuntimeException("삭제할 PRGame이 존재하지 않습니다."); }
            );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}