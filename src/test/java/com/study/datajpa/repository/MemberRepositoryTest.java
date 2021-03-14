package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import com.sun.el.parser.AstFalse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 빈을 injection 받아서 쓰기 때문에 스프링 컨테이너가 필요
@Transactional // jpa의 모든 데이터변경은 트랙잭션안에서 이루어져야 한다. 기본적으로 Test에서는 Rollback 처리된다.
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    void save() {
        Member member = new Member("devyu");

        // 회원 등록
        Member savedMember = memberRepository.save(member);

        // 회원 조회
        Optional<Member> byId = memberRepository.findById(savedMember.getId());

        if(byId.isPresent()) {
            Member findMember = byId.get();

            // true
            assertThat(findMember.getId()).isEqualTo(member.getId());

            // true
            assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

            // true : jpa 특성상 같은 트랙잰션안에서는 영속성 컨텍스트(1차 캐시)의 동일성을 보장한다.(같은 인스턴스 보장)
            assertThat(findMember).isEqualTo(member);
        }

    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }
}