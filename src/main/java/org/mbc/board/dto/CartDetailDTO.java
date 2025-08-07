package org.mbc.board.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Data
public class CartDetailDTO {
    
    private Long mid;// 장바구니상품아이디
    private String itemName; //상품명
    private int price;//상품금액
    private int count; //수량
    private  String imgUrl; // 상품 이미지 경로
 
    public CartDetailDTO(Long mid, String itemName, int price, int count, String imgUrl) {
        this.mid = mid;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }// CartDetailDTO() 종료
    
}//class 종료
