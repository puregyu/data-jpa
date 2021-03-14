package com.study.datajpa.repository;

import com.study.datajpa.entity.Member;
import com.study.datajpa.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {

    // @PersistenceContext 애노테이션을 사용하면 스프링 컨테이너는 영속성 컨텍스트인 EntityManager를 가져온다.
    @PersistenceContext
    private EntityManager entityManager;

    public Team save(Team team) {
        entityManager.persist(team);
        return team;
    }

    public Team find(Long id) {
        return entityManager.find(Team.class, id);
    }

    public void delete(Team team) {
        entityManager.remove(team);
    }

    public List<Team> findAll() {
        // JPQL -> SQL
        return entityManager.createQuery("select t from Team t", Team.class).getResultList();
    }

    public long count() {
        // JPQL -> SQL
        return entityManager.createQuery("select count(t) from Team t", Long.class).getSingleResult();
    }

    public Optional<Team> findById(Long id) {
        Team team = entityManager.find(Team.class, id);
        return Optional.ofNullable(team);
    }

}
