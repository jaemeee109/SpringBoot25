package org.mbc.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.constant.ItemSellStatus;
import org.mbc.board.domain.Member;
import org.mbc.board.dto.OrderDTO;
import org.mbc.board.entity.Item;
import org.mbc.board.entity.Order;
import org.mbc.board.entity.OrderItem;
import org.mbc.board.repository.ItemRepository;
import org.mbc.board.repository.MemberRepository;
import org.mbc.board.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
public class OrderServiceTest {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    ItemRepository itemRepository;
    
    @Autowired
    MemberRepository memberRepository;
    
    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }//saveItem종료
    
    public Member saveMember(){
        Member member = Member.builder()
                .mid("testuser01")
                .email("test@test")
                .build();
        return memberRepository.save(member);
    } //saveMember() 종료

    @Test
    public void order(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCount(10);
        orderDTO.setMid(item.getMid());

        Long mid = orderService.order(orderDTO, member.getEmail());
        // 주문 로직 호출 mid에 저장

        Order order = orderRepository.findById(mid).orElseThrow(EntityNotFoundException::new);
        List<OrderItem> orderItems = order.getOrderItems();
        int totalPrice = orderDTO.getCount()*item.getPrice();
        assertEquals(totalPrice,order.getTotalPrice());
    }//order종료

    
} //class종료
