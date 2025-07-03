package com.ce.back.service;

import com.ce.back.entity.Community;
import com.ce.back.entity.Team;
import com.ce.back.entity.User;
import com.ce.back.repository.CommunityRepository;
import com.ce.back.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final TeamRepository teamRepository;

    // 게시글 작성
    public Community createPost(String title, String content, Long teamId, String userMail, String category,
            LocalDateTime matchDay) {
        Community post = Community.builder()
                .title(title)
                .content(content)
                .createTime(LocalDateTime.now())
                .views(0)
                .team(Team.builder().teamId(teamId).build())
                .user(User.builder().userMail(userMail).build())
                .category(category)
                .matchDay(matchDay)
                .build();
        return communityRepository.save(post);
    }

    // 게시글 단건 조회 + 조회수 증가
    @Transactional
    public Community getPostWithViews(Long contentId) {
        communityRepository.incrementViews(contentId);
        return communityRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("해당 게시물이 없습니다."));
    }

    // 전체 게시글 조회
    public List<Community> getAllPosts() {
        return communityRepository.findAll();
    }

    // 카테고리로 조회
    public List<Community> getPostsByCategory(String category) {
        return communityRepository.findByCategory(category);
    }

    // 제목 키워드 검색
    public List<Community> searchByTitle(String keyword) {
        return communityRepository.findByTitleContaining(keyword);
    }

    // 팀 ID 기반 조회
    public List<Community> getPostsByTeamId(Long teamId) {
        return communityRepository.findByTeam_TeamId(teamId);
    }

    // 사용자 이메일 기반 조회
    public List<Community> getPostsByUserMail(String userMail) {
        return communityRepository.findByUser_UserMail(userMail);
    }

    // 매칭날짜 기반 조회
    public List<Community> getPostsByMatchDay(LocalDateTime matchDay) {
        return communityRepository.findByMatchDay(matchDay);
    }

    // 게시글 삭제
    public void deletePost(Long contentId) {
        communityRepository.deleteById(contentId);
    }

    // 게시글 수정
    public Community updatePost(Long contentId, String title, String content, String category, LocalDateTime matchDay) {
        Community post = communityRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("해당 게시물이 없습니다."));

        if (title != null)
            post.setTitle(title);
        if (content != null)
            post.setContent(content);

        post.setCategory(category);
        post.setMatchDay(matchDay);

        return communityRepository.save(post);
    }

    // 팀이 작성한 게시물들 삭제
    public void deleteByTeamId(Long teamId) {
        Team team = teamRepository.findTeamByTeamId(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));
        communityRepository.deleteByTeam(team);
    }
}