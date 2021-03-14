package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 빈을 injection 받아서 쓰기 때문에 스프링 컨테이너가 필요
@Transactional // jpa의 모든 데이터변경은 트랙잭션안에서 이루어져야 한다. 기본적으로 Test에서는 Rollback 처리된다.
// @Rollback(false) Test에서 Rollback 처리안함
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    void save() {
        Member member = new Member("devyu");

        // 회원 등록
        Member savedMember = memberJpaRepository.save(member);

        // 회원 조회
        Member findMember = memberJpaRepository.find(savedMember.getId());

        // true
        assertThat(findMember.getId()).isEqualTo(member.getId());

        // true
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        // true : jpa 특성상 같은 트랙잰션안에서는 영속성 컨텍스트(1차 캐시)의 동일성을 보장한다.(같은 인스턴스 보장)
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void find() {

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        //리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        //카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);
        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }
}