package com.study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // jpa는 proxy객체가 존재하기 때문에 private가 아닌 protected로 설정한다.
@ToString(of = {"id", "name"})
public class Team {

    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    public Team(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "team") // mappedBy는 Foreign Key가 없는 쪽으로 연결
    private List<Member> members = new ArrayList<>();

}
