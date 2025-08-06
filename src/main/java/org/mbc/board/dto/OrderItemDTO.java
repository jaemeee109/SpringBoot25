package org.mbc.board.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.entity.OrderItem;

@Getter
@Setter
@Log4j2
public class OrderItemDTO {

    private String itemNm; // 상품명
    private int count; // 주문수량
    private int orderPrice; // 주문금액
    private String imgUrl; // 상품 이미지 경로

    public OrderItemDTO(OrderItem orderItem, String imgUrl) {

        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;

    }//OrderItemDTO() 종료

} //class종료
