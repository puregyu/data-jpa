package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import com.study.datajpa.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 스프링 빈을 injection 받아서 쓰기 때문에 스프링 컨테이너가 필요
@Transactional // jpa의 모든 데이터변경은 트랙잭션안에서 이루어져야 한다. 기본적으로 Test에서는 Rollback 처리된다.
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    EntityManager entityManager; // 같은 transaction 이면 같은 앤티티매니저를 사용

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

    @Test
    public void findUsernameList() {
        Member m1 = new Member("devyu", 10);
        Member m2 = new Member("puregyu", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        assertThat(usernameList.size()).isEqualTo(2);

    }

    @Test
    public void findMemberDto() {

        Team t = new Team("개발부");
        Member m1 = new Member("devyu", 10);
        m1.setTeam(t);

        memberRepository.save(m1);
        teamRepository.save(t);

        List<MemberDto> memberDto = memberRepository.findMemberDto();

    }

    @Test
    public void returnType() {
        Member m1 = new Member("devyu", 10);
        Member m2 = new Member("puregyu", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // 반환타입이 List인데 조회결과가 없는경우, null이 아닌 empty collection이 반환됨
        List<Member> listByUsername = memberRepository.findListByUsername("ttttt");
        System.out.println(listByUsername.isEmpty()); // true
        System.out.println(listByUsername == null); // false

        // 반환타입이 단건인데 조회결과가 없는경우, null
        Member memberByUsername = memberRepository.findMemberByUsername("ttttt");
        System.out.println(memberByUsername == null); // true

        Optional<Member> optionalByUsername = memberRepository.findOptionalByUsername("devyu");

    }

    @Test
    void test() {
        Member m1 = new Member("devyu", 10);
        Member m2 = new Member("puregyu", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

//        Member byMemberDto = memberRepository.findByMemberDto();
//        System.out.println(byMemberDto);
    }

    @Test
    @DisplayName("스프링 데이터 JPA를 통한 페이징 처리 테스트(Page)")
    public void paging_Page() {

        memberRepository.save(new Member("강만주", 20));
        memberRepository.save(new Member("나현수", 20));
        memberRepository.save(new Member("도경만", 10));
        memberRepository.save(new Member("라형주", 20));
        memberRepository.save(new Member("마재석", 10));
        memberRepository.save(new Member("박한솔", 20));
        memberRepository.save(new Member("사현재", 20));
        memberRepository.save(new Member("차인권", 10));
        memberRepository.save(new Member("탁준희", 20));
        memberRepository.save(new Member("홍현", 20));

        int age = 20;
        int offset = 0;
        int limit = 5;

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.DESC, "username");

        Page<Member> members = memberRepository.findFromPageByAge(age, pageRequest);

        assertThat(members.getContent().size()).isEqualTo(5);
        assertThat(members.getTotalElements()).isEqualTo(7);
        assertThat(members.getNumber()).isEqualTo(0);
        assertThat(members.getTotalPages()).isEqualTo(2);
        assertThat(members.isFirst()).isTrue();
        assertThat(members.hasNext()).isTrue();

        members.stream().map(Member::getUsername).forEach(System.out::println);
    }

    @Test
    @DisplayName("스프링 데이터 JPA를 통한 페이징 처리 테스트(Slice)")
    public void paging_Slice() {

        memberRepository.save(new Member("강만주", 20));
        memberRepository.save(new Member("나현수", 20));
        memberRepository.save(new Member("도경만", 10));
        memberRepository.save(new Member("라형주", 20));
        memberRepository.save(new Member("마재석", 10));
        memberRepository.save(new Member("박한솔", 20));
        memberRepository.save(new Member("사현재", 20));
        memberRepository.save(new Member("차인권", 10));
        memberRepository.save(new Member("탁준희", 20));
        memberRepository.save(new Member("홍현", 20));

        int age = 20;
        int offset = 0;
        int limit = 5;

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.DESC, "username");

        // Slice : limit + 1, total count X
        Slice<Member> members = memberRepository.findFromSliceByAge(age, pageRequest);

        assertThat(members.getContent().size()).isEqualTo(5);
        assertThat(members.getNumber()).isEqualTo(0);
        assertThat(members.isFirst()).isTrue();
        assertThat(members.hasNext()).isTrue();

        members.stream().map(Member::getUsername).forEach(System.out::println);
    }

    @Test
    @DisplayName("스프링 데이터 JPA를 통한 bulk 업데이트")
    public void bulkUpdate() {

        memberRepository.save(new Member("강만주", 20));
        memberRepository.save(new Member("나현수", 20));
        memberRepository.save(new Member("도경만", 21));
        memberRepository.save(new Member("라형주", 23));
        memberRepository.save(new Member("마재석", 25));

        int resultCount = memberRepository.bulkAgePlus(21);

        // *주의사항* : JPA의 영속성 컨텍스트에는 도경만이 21살로 이미 1차 캐시에 캐싱화되어 남아있고 데이터베이스에만 bulk update되어 있는 상태
        List<Member> members = memberRepository.findByUsername("도경만");
        members.stream().map(Member::getAge).forEach(System.err::println);

        entityManager.flush(); // JPQL을 실행하면 그 직전에 자동으로 실행됨
        entityManager.clear(); // @Modifying(clearAutomatically = true) 대체 가능

        List<Member> members2 = memberRepository.findByUsername("도경만");
        members2.stream().map(Member::getAge).forEach(System.err::println);

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    @DisplayName("Lazy 테스트")
    public void Lazy() {

        Team teamA = new Team("영업부");
        Team teamB = new Team("개발부");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("강만주", 20, teamA));
        memberRepository.save(new Member("나현수", 20, teamB));

        entityManager.flush();
        entityManager.clear();

        // 이 시점에는 member만 조회를 해온다.
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member name : " + member.getUsername());

            // 프록시 객체 class com.study.datajpa.entity.Team$HibernateProxy$uVss4yEL
            System.out.println("member team class : " + member.getTeam().getClass());

            // 이 시점에서 team을 조회해온다. (Lazy)
            System.out.println("member team name : " + member.getTeam().getName());
        }

        // N + 1 문제 발생 :: (조회된 맴버의 개수만큼 지연로딩에 의하여 추가되는 쿼리 여러개) + (맴버조회 쿼리 하나)
    }

    @Test
    @DisplayName("fetch 테스트(쿼리)")
    public void fetchTestByQuery() {

        Team teamA = new Team("영업부");
        Team teamB = new Team("개발부");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("강만주", 20, teamA));
        memberRepository.save(new Member("나현수", 20, teamB));

        entityManager.flush();
        entityManager.clear();

        // 이 시점에서 member 뿐만 아니라 team 객체까지 조회한다.
        List<Member> members = memberRepository.findMemberFetchJoin();

        for (Member member : members) {
            System.out.println("member name : " + member.getUsername());

            // 진짜 객체 class com.study.datajpa.entity.Team
            System.out.println("member team class : " + member.getTeam().getClass());

            System.out.println("member team name : " + member.getTeam().getName());
        }

        // N + 1 문제 해결
    }

    @Test
    @DisplayName("fetch 테스트(애노테이션)")
    public void fetchTestByAnnotation() {

        Team teamA = new Team("영업부");
        Team teamB = new Team("개발부");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("강만주", 20, teamA));
        memberRepository.save(new Member("강만주", 20, teamB));

        entityManager.flush();
        entityManager.clear();

        // 이 시점에서 member 뿐만 아니라 team 객체까지 조회한다.
        List<Member> members = memberRepository.findEntityGraphByUsername("강만주");

        for (Member member : members) {
            System.out.println("member name : " + member.getUsername());

            // 진짜 객체 class com.study.datajpa.entity.Team
            System.out.println("member team class : " + member.getTeam().getClass());

            System.out.println("member team name : " + member.getTeam().getName());
        }

        // N + 1 문제 해결
    }

}