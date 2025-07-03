package com.ce.back.repository;

import com.ce.back.entity.Community;
import com.ce.back.entity.Team;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    // 제목 키워드 검색
    List<Community> findByTitleContaining(String keyword);

    // 팀 ID로 게시물 조회
    List<Community> findByTeam_TeamId(Long teamId);

    // 사용자 이메일로 게시물 조회
    List<Community> findByUser_UserMail(String userMail);

    // 카테고리로 게시물 조회
    List<Community> findByCategory(String category);

    // 매칭날짜로 게시물 조회
    List<Community> findByMatchDay(LocalDateTime matchDay);

    // 조회수 증가
    @Modifying
    @Query("UPDATE Community c SET c.views = c.views + 1 WHERE c.contentId = :id")
    void incrementViews(@Param("id") Long id);

    void deleteByTeam(Team team);
}