package org.mbc.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/login")
    public void loginGET(String errorCode, String logout){
        // http://localhost:8000/member/login?error=???
        // http://localhost:8000/member/login?logout=???
        log.info("===== MemberController.loginGET 메서드 실행 =====");
        log.info("logout: " + logout); // db에서 활용
        log.info("error: " + errorCode); // db에서 활용

        if(logout != null){
            log.info("logout 처리됨 : " + logout);
        } // if종료

    }//loginGet종료
} //class종료
