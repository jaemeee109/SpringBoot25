package org.mbc.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDTO {

    private Long mid;
    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

    @QueryProjection
    public MainItemDTO(Long mid, String itemNm, String itemDetail, String imgUrl, Integer price) {

        this.mid = mid;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;

    }//MainItemDTO()종료


}//class 종료
