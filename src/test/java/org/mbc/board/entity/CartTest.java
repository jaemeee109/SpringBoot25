package org.mbc.board.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.mbc.board.domain.Member;
import org.mbc.board.repository.CartRepository;
import org.mbc.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CartTest {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PersistenceContext
    EntityManager em;

   @Test
    public void findCartAndMemberTest(){
       Member member = memberRepository.findAll().stream().findFirst().orElseThrow(() -> new EntityNotFoundException("멤버없음"));

       Cart cart = new Cart();
       cart.setMember(member);
       cartRepository.save(cart);

       em.flush();
       em.clear();

       Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
       assertEquals(savedCart.getMember().getMid(),member.getMid());
   }
}//class 종료
