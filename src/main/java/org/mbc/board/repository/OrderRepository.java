package org.mbc.board.repository;


import org.mbc.board.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {


} //interface 종료
