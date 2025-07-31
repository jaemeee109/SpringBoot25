package org.mbc.board.service;

import org.mbc.board.domain.Member;
import org.mbc.board.dto.MemberJoinDTO;
import org.mbc.board.security.dto.MemberSecurityDTO;

public interface MemberService {
    // 회원가입시 해당아이디가 존재하는 경우 처리

    // jpa 기능 .save()
    // 이미 해당하는 mid가 있는 경우에는 insert가 되어야 하는데 update처리
    static class MidExistException extends Exception {
        // 시큐리티 기능
        // 만일 같은 아이디가 존재하면 예외를 발생

    }
    String register(MemberSecurityDTO memberSecurityDTO); // 프론트에서 폼에 있는 내용이 dto로
    MemberSecurityDTO readOne(String mid); // 프론트에서 들어온 아이디를 리턴

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;

    void modify(MemberSecurityDTO memberSecurityDTO) ;


}

