package com.study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // jpa는 proxy객체가 존재하기 때문에 protected로 설정한다.
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY) // jpa 성능 최적화를 위한 지연로딩 설정
    @JoinColumn(name = "team_id") // 외래키를 가진 쪽이 연관관계의 주인
    private Team team;

    public Member(String username) {
        this(username, 0);
    }

    public Member(String username, int age) {
        this(username, age, null);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    // member가 team을 변경하면 연관관계 객체의 데이터에도 세팅을 걸어줘야 한다.
    public void changeTeam(Team t) {
        this.team = t;
        team.getMembers().add(this);
    }

}
