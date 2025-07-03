package com.ce.back.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String userMail; // 사용자 이메일

    private String password; // 비밀번호
    private String userName; // 사용자 이름
    private String tel; // 전화번호

    private String firstPosition; // 첫 번째 선호 포지션
    private String secondPosition; // 두 번째 선호 포지션
    private String thirdPosition; // 세 번째 선호 포지션
}