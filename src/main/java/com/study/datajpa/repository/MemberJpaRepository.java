package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    // @PersistenceContext 애노테이션을 사용하면 스프링 컨테이너는 영속성 컨텍스트인 EntityManager를 가져온다.
    @PersistenceContext
    private EntityManager entityManager;

    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }

    public void delete(Member member) {
        entityManager.remove(member);
    }

    public List<Member> findAll() {
        // JPQL -> SQL
        return entityManager.createQuery("select m from Member m", Member.class).getResultList();
    }

    public long count() {
        // JPQL -> SQL
        return entityManager.createQuery("select count(m) from Member m", Long.class).getSingleResult();
    }

    public Optional<Member> findById(Long id) {
        Member member = entityManager.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return entityManager.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

}
