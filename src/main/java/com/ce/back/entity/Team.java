package com.ce.back.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("teamId")
    private Long teamId; // 팀 고유 ID (순서대로 자동생성)

    private String teamName; // 팀 이름
    private String location; // 팀 활동 지역
    private String logo; // 팀 로고 파일명

    private String firstColor; // 팀 홈 컬러
    private String secondColor; // 팀 원정 컬러

    @ManyToOne // 각 팀에 하나의 관리자만
    @JoinColumn(name = "team_manager_mail")
    private User teamManager; // 팀 관리자 이름

    // 다대다 관계를 위한 매핑
    @ManyToMany
    @JoinTable(name = "team_user", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>(); // 팀에 속한 사용자 목록
}