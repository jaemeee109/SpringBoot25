package org.mbc.board.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Data
@Getter
@Setter
public class MemberJoinDTO {
    // 프론트에서 처리되는 객체를 구현한다.
    @NotBlank(message = "아이디 필수입력")
    private String mid;

    @NotEmpty(message="비밀번호 필수입력")
    @Length(min=8, max=16, message="비밀번호는 8~16 자리 숫자로 입력")
    private String mpw;

    @Email(message="이메일 형식으로 입력")
    private String email;

    private boolean del;
    private boolean social;

    @NotBlank(message="이름 필수 입력")
    private String name;
    @NotEmpty(message="주소는 필수 입력")
    private String address;
}
