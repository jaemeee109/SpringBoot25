package org.mbc.board.repository;

import org.mbc.board.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {


} //interface 종료
