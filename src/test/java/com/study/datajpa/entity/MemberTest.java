package com.study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void testEntity() {
        Team team1 = new Team("영업부");
        Team team2 = new Team("개발부");

        entityManager.persist(team1);
        entityManager.persist(team2);

        Member memberA = new Member("dev", 20, team2);
        Member memberB = new Member("qa", 22, team2);
        Member memberC = new Member("release", 23, team2);
        Member memberD = new Member("sale", 34, team1);

        entityManager.persist(memberA);
        entityManager.persist(memberB);
        entityManager.persist(memberC);
        entityManager.persist(memberD);

        // DB commit & 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();

        List<Member> resultList = entityManager.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
            System.out.println("=> member.team = " + member.getTeam());
        }

    }
}