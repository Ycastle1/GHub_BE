package com.ce.back.repository;

import com.ce.back.entity.Team;
import com.ce.back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    // 팀 이름을 포함한 모든 팀을 리스트로 반환
    List<Team> findTeamsByTeamName(String teamName);

    // 팀 ID로 팀을 찾음
    Optional<Team> findTeamByTeamId(Long teamId);

    // 유저 이메일을 기준으로 팀 목록을 조회하는 쿼리 메서드
    List<Team> findByUsersUserMail(String userMail); // 유저의 이메일을 기준으로 팀 찾기
}