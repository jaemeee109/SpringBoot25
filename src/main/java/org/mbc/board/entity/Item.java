package org.mbc.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mbc.board.constant.ItemSellStatus;

import java.time.LocalDateTime;

@Entity
@Table(name="Item")
@Getter
@Setter
@ToString
public class Item {

    @Id
    @Column(name="Item_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id; // 상품코드

    @Column(nullable=false,length=50)
    private String itemNm; // 상품명

    @Column(name="price", nullable=false)
    private int price; // 가격

    @Column(nullable=false)
    private int stockNumber; // 재고수량

    @Lob
    @Column(nullable=false)
    private String itemDetail; // 상품 상세설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매상태

    private LocalDateTime regTime; // 등록시간
    private LocalDateTime updateTime; // 수정시간


} // class종료
