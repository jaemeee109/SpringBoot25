package org.mbc.board.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.mbc.board.constant.ItemSellStatus;
import org.mbc.board.domain.Member;
import org.mbc.board.domain.MemberRole;
import org.mbc.board.repository.ItemRepository;
import org.mbc.board.repository.MemberRepository;
import org.mbc.board.repository.OrderItemRepository;
import org.mbc.board.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @PersistenceContext
    EntityManager em;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;


    public Item createItem() {

        Item item = new Item();
        item.setItemNm("테스트상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;

    }//createItem() 종료

    @Test
    public void cascadeTest(){

        Order order = new Order();

        for(int i = 0;i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(10000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }//for종료
        orderRepository.saveAndFlush(order);
        em.clear();

        Order saveOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, saveOrder.getOrderItems().size());


    }//cascadeTest() 종료
    
    public Order createOrder(){

        Order order = new Order();
        for(int i = 0;i<3; i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(10000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }//for종료

        Member member = Member.builder()
                .mid("OrderTest.createOrder아이디")
                .mpw("1234")
                .email("id@email.com")
                .name("오더테스트")
                .address("오더시 오더구 오더동")
                .del(false)
                .social(false)
                .build();
        member.addRole(MemberRole.USER);
        memberRepository.save(member);
        memberRepository.flush();

        order.setMember(member);
        orderRepository.save(order);
        return order;

    }//createOrder()종료

    @Test
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    } //orphanRemovalTest() 종료


    @Test
    public void lazyLoadingTest(){
        //지연 로딩 테스트
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class: "+orderItem.getOrder().getClass());
        System.out.println("================");
        orderItem.getOrder().getOrderDate();
        System.out.println("================");

    }//lazyLoadingTest()종료
}//class 종료
