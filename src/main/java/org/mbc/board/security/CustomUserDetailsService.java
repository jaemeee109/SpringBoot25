package org.mbc.board.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
// @RequiredArgsConstructor // p.689 에서 테스트할때만 제외
public class CustomUserDetailsService implements UserDetailsService {
    // UserDetailsService 인터페이스를 구현하는 클래스
    // UserDetailsService 단 하나의 메서드를 가지고 동작함
    // 구현클래스로 loadUserByUsername을 재정의해서 사용함

    // 패스워드를 암호화 처리하도록 CustomSecurityConfig에 구현
    private PasswordEncoder passwordEncoder; //new BCryptPasswordEncoder();

    // 기본생성자
    public CustomUserDetailsService(){
        // CustomUserDetailsService() 호출되면 자동으로 패스워드를 암호화 처리 가능
        this.passwordEncoder = new BCryptPasswordEncoder();
    }// 기본생성자 종료

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //실제 인증처리 할 때 호출되는 메서드
        // 프론트에서 id로 넘어오는 username 값을 처리한다
        
        log.info("===== UserDetails.loadUserByUsername 메서드 호출 =====");
        log.info("loadUserByUsername.로그온 사용자의 이름: " + username);
        // 이 메서드가 호출되면 사용자명이 넘어와 처리함 (e-mail, id, 학번 등)
        
        // 이 메서드의 리턴타입은 UserDetails라는 인터페이스 타입이다
        // UserDetails는 사용자 인증(Authentication)과 관련된 정보들을 저장하는 역할
        // Spring Security는 내부적으로 UserDetails 타입 객체를 이용해서 패스워드를 검사하고
        // 사용자 권한을 확인하는 방식으로 동작
        // http://raccon.tistory.com/45

        // getAuthorites() 메서드는 사용자가 가진 인가(Authority) 정보를 반환 해야 함

       

        UserDetails userDetails = User.builder() // User객체는 사용자 객체
                .username("USER1") // User객체의 id
                .password(passwordEncoder.encode("1111")) // User 객체이 pw(암호화처리됨)
                .authorities("ROLE_USER") // ROLE_USER : 일반권한
                .build(); // 빌더 패턴으로 완성
        //Retrieved SecurityContextImpl [Authentication=UsernamePasswordAuthenticationToken
        // [Principal=org.springframework.security.core.userdetails.User
        // [Username=USER1, Password=[PROTECTED], Enabled=true, AccountNonExpired=true,
        // CredentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]],
        // Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails
        // [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=1260CE6208CE90BCC479BBC0A1CCCFC5],
        // Granted Authorities=[ROLE_USER]]] from SPRING_SECURITY_CONTEXT

        return userDetails;

    } // loadUserByUsername종료
}//class종료
