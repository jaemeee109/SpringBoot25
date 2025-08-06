package org.mbc.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.constant.OrderStatus;
import org.mbc.board.entity.Order;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Log4j2
public class OrderHistDTO {
    
    
    
    private Long mid; // 주문id
    private String orderDate; // 주문날짜
    private OrderStatus orderStatus; //주문상태
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();




    public void addOrderItemDTO(OrderItemDTO orderItemDTO) {
        orderItemDTOList.add(orderItemDTO);
    }//addOrderItemDTO()종료


    
    public OrderHistDTO(Order order) {
        this.mid = order.getMid();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderstatus();
     } //OrderHistDTO() 종료

    
    
    
} //class종료
