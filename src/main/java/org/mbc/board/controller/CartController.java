package org.mbc.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.dto.CartDetailDTO;
import org.mbc.board.dto.CartItemDTO;
import org.mbc.board.dto.CartOrderDTO;
import org.mbc.board.dto.OrderDTO;
import org.mbc.board.entity.Cart;
import org.mbc.board.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Log4j2
public class CartController {

    private final CartService cartService;

    @PostMapping("/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDTO cartItemDTO, BindingResult bindingResult, Principal principal) {
        
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }//for종료
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }//if종료

        String mid = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDTO, mid);
        }catch(Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }//try-catch 종료
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);


    }//order()종료

    @GetMapping("/cart")
    public String orderHist(Principal principal, Model model) {
        List<CartDetailDTO> cartDetailList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItem", cartDetailList);
        return "cart/cartList";

    }//orderHist()종료

    @PatchMapping("/cartItem/{mid}")
    public @ResponseBody ResponseEntity updateCartItem (@PathVariable("mid") Long mid, @RequestParam int count, Principal principal) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setMid(mid);
        if(count <=0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        }else if(!cartService.validateCartItem(cartItemDTO, principal.getName())){
          return new ResponseEntity<String>("수정권한이 없습니다", HttpStatus.FORBIDDEN);
       } //if종료
         cartService.updateaCartItemCount(mid,count);
        return new ResponseEntity<Long>(mid, HttpStatus.OK);


    }//updateCartItem()종료

    @DeleteMapping("/cartItem/{mid}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("mid") Long mid, Principal principal) {

        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setMid(mid);
         if(!cartService.validateCartItem(cartItemDTO, principal.getName())){
            return new ResponseEntity<String>("수정권한이 없습니다", HttpStatus.FORBIDDEN);
        } //if종료
        cartService.deleteCartItem(mid);
        return new ResponseEntity<Long>(mid, HttpStatus.OK);

    }//deleteCartItem()종료
    
    
    @PostMapping("/cart/orders")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDTO cartOrderDTO ,Principal principal) {

        List<CartOrderDTO> cartOrderDTOList = cartOrderDTO.getCartOrderDTOList();



        if(cartOrderDTOList == null || cartOrderDTOList.size() == 0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요",HttpStatus.FORBIDDEN);
        }//if 종료
        for (CartOrderDTO cartOrder : cartOrderDTOList) {
            CartItemDTO cartItemDTO = new CartItemDTO(); // 새 DTO 생성
            cartItemDTO.setMid(cartOrder.getMid());      // 주문 DTO에서 mid 복사

            if (!cartService.validateCartItem(cartItemDTO, principal.getName())) {
                return new ResponseEntity<String>("주문 권한이 없습니다", HttpStatus.FORBIDDEN);
            }//if 종료
        } //for종료



        Long orderId = cartService.orderCartItem(cartOrderDTOList, principal.getName());
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    }//orderCartItem() 종료

}//class종료
