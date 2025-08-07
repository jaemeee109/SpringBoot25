package org.mbc.board.repository;

import org.mbc.board.domain.Member;
import org.mbc.board.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long mid);


    Cart findByMember(Member member);
} // interface 종료
