package org.mbc.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Member;
import org.mbc.board.dto.OrderDTO;
import org.mbc.board.dto.OrderHistDTO;
import org.mbc.board.dto.OrderItemDTO;
import org.mbc.board.entity.Item;
import org.mbc.board.entity.ItemImg;
import org.mbc.board.entity.Order;
import org.mbc.board.entity.OrderItem;
import org.mbc.board.repository.ItemImgRepository;
import org.mbc.board.repository.ItemRepository;
import org.mbc.board.repository.MemberRepository;
import org.mbc.board.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDTO orderDTO, String mid) {

        Item item = itemRepository.findById(orderDTO.getMid()).orElseThrow(EntityNotFoundException::new);
        // 주문할 상품 조회 (없으면 예외발생)
        Member member = memberRepository.findByMid(mid).orElseThrow(()->new EntityNotFoundException("회원이 존재하지 않습니다"));
        // 이메일로 정보 조회, 이메일 없으면 예외발생
        List<OrderItem> orderItemList = new ArrayList<>();
        // 주문 상품 리스트에 담기
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDTO.getCount());
        // 주문할 상품 객체 생성 (상품+주문수량)
        orderItemList.add(orderItem);
        // 리스트에 주문상품 추가
        Order order = Order.createOrder(member, orderItemList);
        // 회원 + 주문상품 리스트로 주문 객체 생성
        orderRepository.save(order);
        // 주문을 DB에 저장

        return order.getMid();
        //회원ID에 반환

    } //order()종료

    @Transactional(readOnly = true)
    public Page<OrderHistDTO> getOrderList(String mid, Pageable pageable) {
        // mid로 회원 조회
        Member member = memberRepository.findByMid(mid)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));

        // mid 중복 안 생기게 변수명 바꿈
        String memberMid = member.getMid();

        // 주문 조회
        Page<Order> orderPage = orderRepository.findOrders(memberMid, pageable);
        List<Order> orders = orderPage.getContent();
        long totalCount = orderPage.getTotalElements();

        // DTO 변환
        List<OrderHistDTO> orderHistDTOs = new ArrayList<>();

        for (Order order : orders) {
            OrderHistDTO orderHistDTO = new OrderHistDTO(order);

            for (OrderItem orderItem : order.getOrderItems()) {
                ItemImg itemImg = itemImgRepository.findByItem_MidAndRepimgYn(orderItem.getItem().getMid(), "Y");
                OrderItemDTO orderItemDTO = new OrderItemDTO(orderItem, itemImg.getImgUrl());
                orderHistDTO.addOrderItemDTO(orderItemDTO);
            }

            orderHistDTOs.add(orderHistDTO);
        }

        return new PageImpl<>(orderHistDTOs, pageable, totalCount);
    } //getOrderList() 종룐


    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String userMid) {
        // 로그인한 사용자가 주문한 사용자와 같은 사용자인지 검사

        Member curMember = memberRepository.findByMid(userMid).orElseThrow(()-> new EntityNotFoundException("회원이 존재하지 않습니다."));
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        Member saveMember = order.getMember();

        return saveMember != null && saveMember.getMid().equals(curMember.getMid());



}// validateOrder() 종료

    public void cancelOrder(Long orderId){

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }//cancelOrder()종료



}//class종료
