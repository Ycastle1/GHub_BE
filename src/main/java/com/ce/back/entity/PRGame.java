package com.ce.back.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PRGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prGameId; // 경기 고유 ID

    private String prGameName;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_mail")
    private User user;

    @ManyToOne
    @JoinColumn(name = "gk_id")
    @JsonProperty("gkId") // 골키퍼 ID만 직렬화
    private User gk; // 골키퍼

    @ManyToOne
    @JoinColumn(name = "lb_id")
    @JsonProperty("lbId") // 왼쪽 풀백 ID만 직렬화
    private User lb; // 왼쪽 풀백

    @ManyToOne
    @JoinColumn(name = "lcb_id")
    @JsonProperty("lcbId") // 왼쪽 센터백 ID만 직렬화
    private User lcb; // 왼쪽 센터백

    @ManyToOne
    @JoinColumn(name = "sw_id")
    @JsonProperty("swId") // 스위퍼 ID만 직렬화
    private User sw; // 스위퍼

    @ManyToOne
    @JoinColumn(name = "rcb_id")
    @JsonProperty("rcbId") // 오른쪽 센터백 ID만 직렬화
    private User rcb; // 오른쪽 센터백

    @ManyToOne
    @JoinColumn(name = "rb_id")
    @JsonProperty("rbId") // 오른쪽 풀백 ID만 직렬화
    private User rb; // 오른쪽 풀백

    @ManyToOne
    @JoinColumn(name = "lwb_id")
    @JsonProperty("lwbId") // 왼쪽 윙백 ID만 직렬화
    private User lwb; // 왼쪽 윙백

    @ManyToOne
    @JoinColumn(name = "ldm_id")
    @JsonProperty("ldmId") // 왼쪽 수비형 미드필더 ID만 직렬화
    private User ldm; // 왼쪽 수비형 미드필더

    @ManyToOne
    @JoinColumn(name = "cdm_id")
    @JsonProperty("cdmId") // 중앙 수비형 미드필더 ID만 직렬화
    private User cdm; // 중앙 수비형 미드필더

    @ManyToOne
    @JoinColumn(name = "rdm_id")
    @JsonProperty("rdmId") // 오른쪽 수비형 미드필더 ID만 직렬화
    private User rdm; // 오른쪽 수비형 미드필더

    @ManyToOne
    @JoinColumn(name = "rwb_id")
    @JsonProperty("rwbId") // 오른쪽 윙백 ID만 직렬화
    private User rwb; // 오른쪽 윙백

    @ManyToOne
    @JoinColumn(name = "lm_id")
    @JsonProperty("lmId") // 왼쪽 미드필더 ID만 직렬화
    private User lm; // 왼쪽 미드필더

    @ManyToOne
    @JoinColumn(name = "lcm_id")
    @JsonProperty("lcmId") // 왼쪽 중앙 미드필더 ID만 직렬화
    private User lcm; // 왼쪽 중앙 미드필더

    @ManyToOne
    @JoinColumn(name = "cm_id")
    @JsonProperty("cmId") // 중앙 미드필더 ID만 직렬화
    private User cm; // 중앙 미드필더

    @ManyToOne
    @JoinColumn(name = "rcm_id")
    @JsonProperty("rcmId") // 오른쪽 중앙 미드필더 ID만 직렬화
    private User rcm; // 오른쪽 중앙 미드필더

    @ManyToOne
    @JoinColumn(name = "rm_id")
    @JsonProperty("rmId") // 오른쪽 미드필더 ID만 직렬화
    private User rm; // 오른쪽 미드필더

    @ManyToOne
    @JoinColumn(name = "lam_id")
    @JsonProperty("lamId") // 왼쪽 공격형 미드필더 ID만 직렬화
    private User lam; // 왼쪽 공격형 미드필더

    @ManyToOne
    @JoinColumn(name = "cam_id")
    @JsonProperty("camId") // 중앙 공격형 미드필더 ID만 직렬화
    private User cam; // 중앙 공격형 미드필더

    @ManyToOne
    @JoinColumn(name = "ram_id")
    @JsonProperty("ramId") // 오른쪽 공격형 미드필더 ID만 직렬화
    private User ram; // 오른쪽 공격형 미드필더

    @ManyToOne
    @JoinColumn(name = "lw_id")
    @JsonProperty("lwId") // 왼쪽 윙어 ID만 직렬화
    private User lw; // 왼쪽 윙어

    @ManyToOne
    @JoinColumn(name = "cf_id")
    @JsonProperty("cfId") // 중앙 포워드 ID만 직렬화
    private User cf; // 중앙 포워드

    @ManyToOne
    @JoinColumn(name = "rw_id")
    @JsonProperty("rwId") // 오른쪽 윙어 ID만 직렬화
    private User rw; // 오른쪽 윙어

    @ManyToOne
    @JoinColumn(name = "ls_id")
    @JsonProperty("lsId") // 왼쪽 스트라이커 ID만 직렬화
    private User ls; // 왼쪽 스트라이커

    @ManyToOne
    @JoinColumn(name = "rs_id")
    @JsonProperty("rsId") // 오른쪽 스트라이커 ID만 직렬화
    private User rs; // 오른쪽 스트라이커

    @ManyToOne
    @JoinColumn(name = "st_id")
    @JsonProperty("stId") // 스트라이커 ID만 직렬화
    private User st; // 스트라이커
}