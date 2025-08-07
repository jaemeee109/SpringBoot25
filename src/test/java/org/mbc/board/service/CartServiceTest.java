package org.mbc.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.constant.ItemSellStatus;
import org.mbc.board.domain.Member;
import org.mbc.board.dto.CartItemDTO;
import org.mbc.board.entity.CartItem;
import org.mbc.board.entity.Item;
import org.mbc.board.repository.CartItemRepository;
import org.mbc.board.repository.ItemRepository;
import org.mbc.board.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
public class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepository cartItemRepository;
    
    public Item savedItem(){
        Item item = new Item();
        item.setItemNm("테스트상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }//savedItem() 종료

    public Member savedMember(){
        Member member = Member.builder()
                .mid("testuser01")
                .email("test@test")
                .build();
        return memberRepository.save(member);
    }//savedMember()종료

    @Test
    public void addCart(){
        Item item = savedItem();
        Member member = savedMember();
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setCount(5);
        cartItemDTO.setMid(item.getMid());

        Long cartItemId = cartService.addCart(cartItemDTO, member.getMid());

        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getMid(), cartItem.getItem().getMid());
        assertEquals(cartItemDTO.getCount(), cartItem.getCount());
    } //addCart() 종료
    



}// class 종료
