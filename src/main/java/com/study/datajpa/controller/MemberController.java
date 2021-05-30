package com.study.datajpa.controller;

import com.study.datajpa.entity.Member;
import com.study.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        // @PageableDefault 애노테이션 사용 가능
        // members?page=0&size=3&sort=id,desc
        return memberRepository.findAll(pageable);
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("devyu"));

        for (int i = 0; i < 200; i++) {
            memberRepository.save(new Member("user" + i, i));
        }

    }
}
