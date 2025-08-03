package org.mbc.board.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.domain.Member;
import org.mbc.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Log4j2
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @WithMockUser(username="gildong", roles="USER")
    public void auditingTest(){

        Member newMember = Member.builder()
                .mid("Membertest")
                .mpw("1234")
                .email("membertest@auditingTest")
                .name("이멤테")
                .address("대한민국")
                .del(false)
                .social(false)
                .build();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getMid()).orElseThrow(EntityNotFoundException::new);
        log.info("register time :" + member.getRegDate());
        log.info("update time :" + member.getModDate());
        log.info("create member :" + member.getCreatedBy());
        log.info("modify member :" + member.getModifiedBy());

    } //  auditingTest() 종료

} // MemberTest 종료
