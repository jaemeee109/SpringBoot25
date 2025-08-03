package org.mbc.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mbc.board.constant.ItemSellStatus;
import org.mbc.board.domain.BaseEntity;
import org.mbc.board.dto.ItemFormDTO;

import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long mid; // 상품코드

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
    
   public void updateItem(ItemFormDTO itemFormDTO) {
       this.itemNm = itemFormDTO.getItemNm();
       this.price = itemFormDTO.getPrice();
       this.stockNumber = itemFormDTO.getStockNumber();
       this.itemDetail = itemFormDTO.getItemDetail();
       this.itemSellStatus = itemFormDTO.getItemSellStatus();
   }//updateItem 종료


} // class종료
