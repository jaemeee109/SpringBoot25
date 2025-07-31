package org.mbc.board.security.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User { //extends User 시큐리티 DUserDetails 용

    // 필드
    private String mid;
    private String mpw;
    private String email;
    private boolean del;
    private boolean social;
    
    // 소셜로그인 정보 p.754 추가
    private Map<String, Object> props; //properties약자

    // 생성자
    public MemberSecurityDTO(String username, String password, String email,
                             boolean del, boolean social,
                             Collection<? extends GrantedAuthority> authorities) {
        //                   프론트에서 넘어오는 값은 다를수 있다.

        super(username, password, authorities);  // User 객체

        this.mid = username;
        this.mpw = password;
        this.email = email;
        this.del = del;
        this.social = social;
    }


    public MemberSecurityDTO() {
        super("username","password", List.of());
    }



    @Override // p.754 추가 (카카오 로그인 정보 가져오기)
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override // p.754 추가 (db의 id 가져오기)
    public String getName() {
        return this.mid;
    }
}
