package org.mbc.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.domain.Member;
import org.mbc.board.dto.CartItemDTO;
import org.mbc.board.entity.Cart;
import org.mbc.board.entity.CartItem;
import org.mbc.board.entity.Item;
import org.mbc.board.repository.CartItemRepository;
import org.mbc.board.repository.CartRepository;
import org.mbc.board.repository.ItemRepository;
import org.mbc.board.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CartService {
    
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    
    public Long addCart(CartItemDTO cartItemDTO, String mid) {

        Item item = itemRepository.findById(cartItemDTO.getMid()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findById(mid).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByMember(member);
        if(cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }//if종료

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getMid(), item.getMid());
        
        if(savedCartItem != null) {
            savedCartItem.addCount(cartItemDTO.getCount());
            return savedCartItem.getMid();
            
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDTO.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getMid();
        } //if종료


    }//addCart()종료
    
    
} // class종료
