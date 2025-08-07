package org.mbc.board.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.constant.ItemSellStatus;
import org.mbc.board.constant.OrderStatus;
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

    @Autowired
    private EntityManager em;

    public Item saveItem() {
        Item item = new Item();
        item.setItemNm("테스트상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    } // saveItem 종료

    public Member saveMember() {
        Member member = Member.builder()
                .mid("testuser01")
                .email("test@test")
                .build();
        return memberRepository.save(member);
    } // saveMember 종료

    @Test
    public void order() {
        Item item = saveItem();
        Member member = saveMember();

        em.flush();  // DB에 저장 반영
        em.clear();  // 영속성 컨텍스트 초기화

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCount(10);
        orderDTO.setMid(item.getMid()); // 상품 ID(Long)

        Long orderId = orderService.order(orderDTO, member.getMid()); // 회원 ID(String)

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        int totalPrice = orderDTO.getCount() * item.getPrice();
        assertEquals(totalPrice, order.getTotalPrice());
    }// order 종료

    @Test
    public void cancelOrder() {
        Item item = saveItem();
        Member member = saveMember();

        em.flush();  // DB에 저장 반영
        em.clear();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCount(10);
        orderDTO.setMid(item.getMid());  // 상품 ID(Long)

        Long orderId = orderService.order(orderDTO, member.getMid());

        orderService.cancelOrder(orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(OrderStatus.CANCEL, order.getOrderstatus());
        assertEquals(100, item.getStockNumber());
    } // cancelOrder 종료

} // class 종료
