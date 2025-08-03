package org.mbc.board.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 인증된 사용자 정보 가져오기

        String userId = "";
        // 변수 초기화

        if (authentication != null) {
            // 인증 객체가 null이 아닐경우 실행
            userId = authentication.getName();
            // 인증 객체에서 사용자 이름을 가져와 userId  에 저장
        } //if종료
        return Optional.of(userId);

    } //getCurrentAuditor() 종료

    
} //class 종료
