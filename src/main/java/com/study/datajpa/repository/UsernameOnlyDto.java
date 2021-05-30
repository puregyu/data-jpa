package com.study.datajpa.repository;

import lombok.Getter;

@Getter
public class UsernameOnlyDto {

    private final String username;

    // 생성자 파라미터명 'username' 에 매칭
    public UsernameOnlyDto(String username) {
        this.username = username;
    }
}
