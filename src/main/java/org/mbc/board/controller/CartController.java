package org.mbc.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.dto.CartDetailDTO;
import org.mbc.board.dto.CartItemDTO;
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



}//class종료
