package com.study.datajpa.repository;

import com.study.datajpa.dto.MemberDto;
import com.study.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 스프링 데이터 JPA는 메소드 이름을 분석해서 JPQL을 생성하고 실행
    // Query Method 방식
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // NamedQuery 방식
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // Query repository 직접 정의 방식
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // 객체DTO에 조회값 바인딩 하기
    @Query("select new com.study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 파라미터 바인딩(리스트)
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    // 컬렉션 반환타입
    List<Member> findListByUsername(String username);
    // 단건 반환타입
    Member findMemberByUsername(String username);
    // 단건(optional) 반환타입
    Optional<Member> findOptionalByUsername(String username);

    // paging 처리(Page)
//    Page<Member> findByAge(int age, Pageable pageable);

    // paging 처리(Slice)
    Slice<Member> findByAge(int age, Pageable pageable);

    // bulk update
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}