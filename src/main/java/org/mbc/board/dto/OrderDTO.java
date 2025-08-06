package org.mbc.board.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public class OrderDTO {

    @NotNull(message = "상품 아이디는 필수 입력 값 입니다")
    private Long mid;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다")
    @Max(value = 999, message = "최대 주문 수량은 999개 입니다")
    private int count;


} //class 종료
