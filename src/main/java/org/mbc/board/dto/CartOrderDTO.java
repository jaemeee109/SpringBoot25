package org.mbc.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDTO {

    private Long mid; // 카트아이템id
    private List<CartOrderDTO> cartOrderDTOList;

}//class 종료
