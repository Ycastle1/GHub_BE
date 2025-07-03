package com.ce.back.controller;

import com.ce.back.entity.Community;
import com.ce.back.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityService communityService;

    // 게시글 작성
    @PostMapping
    public ResponseEntity<Community> createPost(@RequestBody java.util.Map<String, Object> request) {
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        Long teamId = ((Number) request.get("teamId")).longValue();
        String userMail = (String) request.get("userMail");
        String category = (String) request.get("category");
        LocalDateTime matchDay = LocalDateTime.parse((String) request.get("matchDay"));
        Community created = communityService.createPost(title, content, teamId, userMail, category, matchDay);
        return ResponseEntity.ok(created);
    }

    // 게시글 단건 조회 (조회수 증가 포함)
    @GetMapping("/{id}")
    public ResponseEntity<Community> getPost(@PathVariable Long id) {
        Community post = communityService.getPostWithViews(id);
        return ResponseEntity.ok(post);
    }

    // 전체 게시글 목록
    @GetMapping
    public ResponseEntity<List<Community>> getAllPosts() {
        return ResponseEntity.ok(communityService.getAllPosts());
    }

    // 카테고리로 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Community>> getPostsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(communityService.getPostsByCategory(category));
    }

    // 매칭날짜로 조회
    @GetMapping("/matchDay/{matchDay}")
    public ResponseEntity<List<Community>> getPostsByMatchDay(@PathVariable LocalDateTime matchDay) {
        return ResponseEntity.ok(communityService.getPostsByMatchDay(matchDay));
    }

    // 제목 키워드로 검색
    @GetMapping("/search")
    public ResponseEntity<List<Community>> searchByTitle(@RequestParam String keyword) {
        return ResponseEntity.ok(communityService.searchByTitle(keyword));
    }

    // 팀 ID로 게시글 목록
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Community>> getByTeam(@PathVariable Long teamId) {
        return ResponseEntity.ok(communityService.getPostsByTeamId(teamId));
    }

    // 사용자 이메일로 게시글 목록
    @GetMapping("/user/{userMail}")
    public ResponseEntity<List<Community>> getByUser(@PathVariable String userMail) {
        return ResponseEntity.ok(communityService.getPostsByUserMail(userMail));
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        communityService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Community> updatePost(@PathVariable Long id, @RequestBody java.util.Map<String, Object> request) {
        String title = (String) request.get("title");
        String content = (String) request.get("content");
        String category = (String) request.get("category");
        LocalDateTime matchDay = (LocalDateTime) request.get("matchDay");

        Community updated = communityService.updatePost(id, title, content, category, matchDay);
        return ResponseEntity.ok(updated);
    }
}