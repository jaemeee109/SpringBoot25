package org.mbc.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Length;

@Entity // 데이터베이스 관련 객체임을 선언
@Table(name="tbl_memo") //데이터 베이스 테이블명 선언
@ToString
@Getter
@Builder // 빌더패턴 사용  member.setName(), member.getName() -> member.name() 으로 사용할 수 있게 하는 @
@AllArgsConstructor // 모든 필드 값을 이용해서 생성자 만듦
@NoArgsConstructor // 기본생성자
public class Memo {
    
    @Id // pk임을 선언
    @GeneratedValue (strategy = GenerationType.IDENTITY) 
    // GenerationType.IDENTITY->pk를 자동으로 생성 하고자 함 (키생성)
    // 만일 연결되는 데이터 베이스가 오라클이면 번호를 위한 별도의 테이블 생성 (시퀀스 객체)
    // mySQL이나 MariaDB이면 auto incremenet 를 기본으로 사용해서 새로운 레코드가 기록 될 때 다른 번호흘 줌
    // GenerationType.AUTO -> jpa가 알아서 생성 방식을 결정해라
    // GenerationType.SEQUENCE -> 데이터베이스의 시퀀스를 이용해서 키를 생성
    // GenerationType.TABLE -> 키 생성 전용 테이블을 생성해서 키를 생성
    private Long mno;
    
    @Column(length =200, nullable = false) // 200글자에 notnull효과
    private String memoText;
}
