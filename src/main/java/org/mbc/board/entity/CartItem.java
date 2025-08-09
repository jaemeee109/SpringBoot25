package org.mbc.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mbc.board.domain.BaseEntity;

@Entity
@Getter
@Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long mid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private int count;

    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    } // createCartItem() 종료

    public void addCount(int count) {
        this.count += count;
    }//addCount()종료
    
    public void updateCount (int count){
        this.count += count;
    }//updateCount 종료

} // class 종료
