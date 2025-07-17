package org.mbc.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity // DB테이블 관련 객체
@Getter
@Builder // 빌더패턴 (세터 대신 활용)
@AllArgsConstructor // 모든 필드값으로 생성자 만듦
@NoArgsConstructor // 기본생성자
@ToString // 테스트용 : 객체 주소가 아닌 값을 출력
public class Board extends BaseEntity{ // extends BaseEntity -> 날짜 관련된 jpa를 연결

    /*필드*/
    @Id // pk선언 (not null, unique, indexing)
    @GeneratedValue (strategy = GenerationType.IDENTITY) // 자동번호 생성용
    private Long bno; // 게시물 번호

    @Column(length = 500,nullable = false) // nullable = false : not Null
    private String title; // 제목

    @Column(length = 2000, nullable = false)
    private String content; // 내용

    @Column(length = 50, nullable = false)
    private String writer; // 작성자

 /* 서버를 돌리면 테이블이 생성 된 걸 볼 수 있음
    Hibernate:
    create table board (
    bno bigint not null auto_increment,
    content varchar(255),
    title varchar(255),
    writer varchar(255),
    primary key (bno)
    ) engine=InnoDB

    * baseEntity 상속 후 변경 코드

    Hibernate:
    alter table if exists board
       add column moddate datetime(6)
    Hibernate:
        alter table if exists board
           add column reg_date datetime(6)
    Hibernate:
        alter table if exists board
           modify column content varchar(2000) not null
    Hibernate:
        alter table if exists board
           modify column title varchar(500) not null
    Hibernate:
        alter table if exists board
           modify column writer varchar(50) not null



    */
    
    public void change(String title, String content){
        // 제목과 내용만 수정하는 메서드 (세터 대체용)
        this.title = title;
        this.content = content;
    }

} // class 종료
