package org.mbc.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mbc.board.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long mid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; // 주문가격
    private int count; // 수량
    
    public static OrderItem createOrderItem(Item item, int count) {
       
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); // 주문상품
        orderItem.setCount(count); // 주문수량
        
        orderItem.setOrderPrice(item.getPrice());
        
        item.removeStock(count); // 재고수량 감소
        return orderItem;
        
    }//createOrderItem 종료
    
    public int getTotalPrice() {
        return orderPrice * count; // 총가격
    }//getTotalPrice 종료


    public void cancel(){
        
        this.getItem().addStock(count);
        // 주문취소시 주문 수량을 상품의 재고에+ 시킴
    }//cancel() 종료
}//class 종료
