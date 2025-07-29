package org.mbc.board.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.security.CustomUserDetailsService;
import org.mbc.board.security.handler.Custom403Handler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2 // 로그출력용
@Configuration // 환경설정임을 명시
@RequiredArgsConstructor // fianl 필드에 대한 생성자
//@EnableGlobalMethodSecurity(prePostEnabled = true) // @으로 권한 부여-> 시큐리티6에서 차단되어있음
@EnableMethodSecurity(prePostEnabled = true) //시큐리티6에서는 @EnableMethodSecurity 으로 처리
//@PreAuthorize : 메서드가 호출되기 전에 접근을 허용할지 결정 (사전검사)
//@PostAuthorize : 메서드가 호출된 후에 접근을 허용할지 결정 (사후검사)
//@Secured : 특정 롤(role)로 접근을 제한
//@RolesAllowed : 특정 롤(role)로 접근을 제한한다(JSR-250)
// 게시물의 목록은 로그인 여부에 관계없이 볼 수 있지만 글쓰기는 권한이 있어야 가능
// 해당 컨트롤러에 적용하면 된다 -> BoardController
// registerGet() -> @PreAuthorize("hasRole('USER')") -> 게시글 등록 페이지로 가기전에 USER권한인지 봄
// read() -> @PreAutorize("isAuthenticated()") -> 로그인한 사용자인지 확인
// modify() -> @PreAuthorize("principal.username == #boardDTO.writer") -> 작성자인지
// remove() -> @PreAuthorize("principal.username == #boardDTO.writer") -> 작성자인지
// 표현식 https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html
public class CustomSecurityConfig {
    // 스프링시큐리티 환경설정 하는 부분
    // board/list 접속시 /login 페이지로 자동 이동(시큐리티 내장된 로그인 페이지: id : user)
    // Using generated security password: a71521ce-b412-4da1-8fc1-b49a1b115629 (1회용)
    
    // p.704추가 자동로그인용 데이터베이스 연동
    private final DataSource dataSource;
    //  p.704추가 User 객체 처리용
    private final CustomUserDetailsService userDetailsService;
    
    @Bean // Spring 레거시에는 root-context.xml에서 설정한 부분
    public SecurityFilterChain FilterChain(HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {
        // 리턴 값                             파라미터                예외처리
        // 강제 로그인 안하는 메서드 용
        
        log.info("=====SecurityFilterChain. FilterChain() 메서드 실행=====");
        log.info("===== 강제로 로그인 하지 않음 =====");
        log.info("===== 모든 사용자가 모든 경로에 접근 할 수 있음 =====");
        log.info("===== application.properties파일에 로그 출력 레벨 추가 =====");
        // logging.level.org.springframework.security.web= debug
        // logging.level.org.mbc.security.web= debug
        // logging.level.org.springframework.security= trace

        // 이 메서드 안쪽에 커스텀한 실행문을 넣으면 동작하게 설정

        // 실제로 인증을 처리하는 UserDetailsService 인터페이스를 사용해서 실제 인증을 커스텀
        // UserDetailsService.loadUserByUserName() 실제인증을 처리할 때 호출 되는 부분 (단 1개의 메서드를 가짐)
        // username이라고 부르는 사용자의 아이디를 인증 코드로 구현

        // formLogin()' is deprecated since version 6.1 and marked for removal
        // http.formLogin(); 시큐리티 버전 6.1이상 급에서는 사용하지 말것
        // 람다식으로 변환해서 사용 -> 시큐리티 5버전에서는 매개변수가 없는 메서드를 사용가능
        //                      -> 6버전 이상해서는 deprecated
        http.formLogin(form -> {
            // 시큐리티 6버전부터는 람다식으로 변환하여 사용 됨
            log.info("=== 커스텀한 로그인 페이지 호출 ===");
            form.loginPage("/member/login"); // 로그인페이지 커스텀(p.694)
            // http://localhost/member/login.html
        });

        // http.csrf().disable()
        // 6.1버전에서 제외 됨 (스프링 3.0 이후 버전에서는 사용 안됨)
        // 람다식으로 사용할 것을 권고 함 . 아래로 변경
        http.csrf(httpSecurityCsrfConfigurer -> {
            // csrf 토큰에 대한 비활성화
            // 실무에서는 사용하면 안됨
            // 프론트에 아래코드 필수
            // <input type ="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token};>
            log.info("==== CSRF 비활성화 호출 ==== ");
            httpSecurityCsrfConfigurer.disable();
        });
        // p.704 자동 로그인 기능 구현 추가
        http.rememberMe(httpSecurityRememberMeConfigurer -> {
           httpSecurityRememberMeConfigurer.key("12345678") // key는 개발자 마음대로 (쿠키값을 인코딩시 활용)
                   .tokenRepository(persistentTokenRepository()) // 필요한 정보를 저장
                   .userDetailsService(customUserDetailsService) // User객체를 이용
                   .tokenValiditySeconds(60*60*24*30); // 30일 보관
            //                           초  분  시  일 쿠키의 maxAge()
        });

        //p.718 403예외처리 핸들러 사용
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
        });

        return http.build();

    }//FilterChain 종료
    @Bean // 내장된 403을 사용하는 것이 아니라 내가 만든 재정의용 핸들러를 활용한다
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    } // accessDeniedHandler 메서드 종료

    @Bean // 자동로그인용 데이터베이스 처리
    public PersistentTokenRepository persistentTokenRepository() {

        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();

        jdbcTokenRepository.setDataSource(dataSource);
        log.info("===== persistentTokenRepositoty 토큰생성기법 호출=====");
        return jdbcTokenRepository;
        // https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/web/authentication/rememberme/PersistentTokenRepository.html

    } // persistentTokenRepository 종료

    // p.683 정적페이지의 시큐리티 제외 처리
    
    @Bean
    public WebSecurityCustomizer WebSecurityCustomizer() {
        // css와 같이 정적 자원들에 대한 시큐리티 적용 제외
        log.info("=====WebSecurityCustomizer.WebSecurityCustomizer() 메서드 실행 =====");
        log.info("=====toStaticResources에 ignoring 처리 됨=====");
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        // No security for GET/css/styles.css
        
   } //WebSecurityCustomizer 종료
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // p.689 패스워드를 암호화 처리하는 용도
        log.info("===== passwordEncoder 패스워드 암호화 기법 처리 메서드 실행 =====");
        return new BCryptPasswordEncoder(); // 해시코드로 암호화 기법을 적용
    } //passwordEncoder 종료

} // class 종료
