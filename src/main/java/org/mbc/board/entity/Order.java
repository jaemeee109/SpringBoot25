package org.mbc.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mbc.board.constant.OrderStatus;
import org.mbc.board.domain.BaseEntity;
import org.mbc.board.domain.Member;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="orders")
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long mid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_mid")
    private Member member;

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderstatus; // 주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem); // 주문 상품 넣기
        orderItem.setOrder(this);
    }//addOrderItem 종료

    public static Order createOrder(Member member, List<OrderItem> orderItemList){

        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem : orderItemList){
            order.addOrderItem(orderItem);
        } // for종료 // 장바구니 목록에 여러개 담을수 있게
        order.setOrderstatus(OrderStatus.ORDER); // 주문 세팅
        order.setOrderDate(LocalDateTime.now()); // 주문시간 세팅
        return order;
    } //createOrder 종료

    public int getTotalPrice(){
        // 총 주문 금액
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }//for종료
        return totalPrice;
    } // getTotalPrice()종료

} //class 종료
