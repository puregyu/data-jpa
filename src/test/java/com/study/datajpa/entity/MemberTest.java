package com.study.datajpa.entity;

import com.study.datajpa.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

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

    @Test
    @DisplayName("Auditing 테스트")
    public void auditing_test() {
        Member member = memberRepository.save(new Member("민규")); //@PrePersist

        Optional<Member> byId = memberRepository.findById(member.getId());
        if(byId.isPresent()) {
            System.out.println(byId.get().getCreatedDate());
            System.out.println(byId.get().getModifiedDate());
            System.out.println(byId.get().getCreatedBy());
            System.out.println(byId.get().getModifiedBy());
        }
    }
}