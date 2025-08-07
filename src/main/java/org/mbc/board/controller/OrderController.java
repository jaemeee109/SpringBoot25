package org.mbc.board.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.dto.OrderDTO;
import org.mbc.board.dto.OrderHistDTO;
import org.mbc.board.security.dto.MemberSecurityDTO;
import org.mbc.board.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping("/order")
    public @ResponseBody ResponseEntity order (@RequestBody @Valid OrderDTO orderDTO, BindingResult bindingResult, Principal principal) {
        
        if(bindingResult.hasErrors()) {
            // 주문정보 바인딩시 에러 검사
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }//for종료
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
            // 에러정보 반환
        }//if종료
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MemberSecurityDTO memberDTO = (MemberSecurityDTO) auth.getPrincipal();
        String email = memberDTO.getEmail();
        Long mid;

        try{
            mid = orderService.order(orderDTO, email);
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }//try-catch 종료
        return new ResponseEntity<Long>(mid, HttpStatus.OK);
    } //order() 종료

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value ={"orders","/orders/{page}"})
    public String orderHist(@PathVariable("page")Optional<Integer> page, Authentication authentication, Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 4);

        MemberSecurityDTO member = (MemberSecurityDTO) authentication.getPrincipal();
        String mid = member.getMid();  // 이메일이 아닌 mid를 기준으로 조회!

        Page<OrderHistDTO> orderHistDTOList = orderService.getOrderList(mid, pageable);
        model.addAttribute("orders", orderHistDTOList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderHist";
    }
    // orderHist()종료


    @PostMapping("/order/{mid}/cancel")
    public @ResponseBody ResponseEntity orderCancel(@PathVariable("mid") Long mid, Principal principal, Model model) {
        
        //다른사람 주문을 취소하지 못하게 방지
        if(!orderService.validateOrder(mid, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다", HttpStatus.FORBIDDEN);
        }//if종료
        orderService.cancelOrder(mid);
        // 주문 취소 로직 호출
        return new ResponseEntity<Long>(mid, HttpStatus.OK);
        
    } // orderCancel() 종료
    
    
} // class 종료
