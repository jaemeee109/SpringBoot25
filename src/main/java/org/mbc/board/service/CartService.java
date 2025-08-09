package org.mbc.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Member;
import org.mbc.board.dto.CartDetailDTO;
import org.mbc.board.dto.CartItemDTO;
import org.mbc.board.dto.CartOrderDTO;
import org.mbc.board.dto.OrderDTO;
import org.mbc.board.entity.Cart;
import org.mbc.board.entity.CartItem;
import org.mbc.board.entity.Item;
import org.mbc.board.repository.CartItemRepository;
import org.mbc.board.repository.CartRepository;
import org.mbc.board.repository.ItemRepository;
import org.mbc.board.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CartService {
    
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDTO cartItemDTO, String mid) {

        Item item = itemRepository.findById(cartItemDTO.getMid()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findById(mid).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByMember(member);
        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }//if종료

        CartItem savedCartItem = cartItemRepository.findByCartAndItem(cart, item);

        if(savedCartItem != null) {
            savedCartItem.addCount(cartItemDTO.getCount());
            return savedCartItem.getMid();
            
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDTO.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getMid();
        } //if종료


    }//addCart()종료

  @Transactional(readOnly = true)
    public List<CartDetailDTO> getCartList(String mid) {

       /* List<CartDetailDTO> cartDetailDTOList = new ArrayList<>();*/
        Member member = memberRepository.findById(mid).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByMember(member);
        if(cart == null) {
            return new ArrayList<>();
        }//if종료
         return cartItemRepository.findCartDetailDTOList(cart.getMid());

    }//getCartList() 종료

    @Transactional(readOnly = true)
    public boolean validateCartItem(CartItemDTO cartItemDTO, String mid){
        Member member = memberRepository.findById(mid).orElseThrow(EntityNotFoundException::new);
        CartItem cartitem = cartItemRepository.findById(cartItemDTO.getMid()).orElseThrow(EntityNotFoundException::new);
        Member savedMember =cartitem.getCart().getMember();

        if(!StringUtils.equals(member.getMid(),savedMember.getMid())){
            return false;
        }
        return true;

    }//validateCartItem()종료
    
    
    public void updateaCartItemCount(Long mid, int count) {
        CartItem cartItem = cartItemRepository.findById(mid).orElseThrow(EntityNotFoundException::new);
        cartItem.setCount(count);
    }//updateaCartItemCount()종료

    public void deleteCartItem(Long mid) {
        CartItem cartItem = cartItemRepository.findById(mid).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }//deleteCartItem() 종료

    public Long orderCartItem(List<CartOrderDTO> cartOrderDTOList, String mid) {

        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (CartOrderDTO cartOrderDTO : cartOrderDTOList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDTO.getMid()).orElseThrow(EntityNotFoundException::new);

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setMid(cartItem.getItem().getMid());
            orderDTO.setCount(cartItem.getCount());
            orderDTOList.add(orderDTO);
        }//for종료

        Long orderId = orderService.orders(orderDTOList, mid);

        for (CartOrderDTO cartOrderDTO : cartOrderDTOList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDTO.getMid()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);

        }//for종료
        return orderId;
    }//orderCartItem()종료
    
} // class종료
