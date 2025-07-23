package org.mbc.board.service;


import org.mbc.board.dto.PageRequestDTO;
import org.mbc.board.dto.PageResponseDTO;
import org.mbc.board.dto.ReplyDTO;

public interface ReplyService {
    // 조장이 시그니처를 정하는 곳

    // 등록용 : 프론트에서 DTO객체가 넘어오면 DB에 .save()
    Long register(ReplyDTO replyDTO); // 리턴은 long이니까 rno가 나옴

    // read one : 프론트에서 rno가 넘어오면 DB에 .findById()
    ReplyDTO read(Long rno); // 객체를 리턴

    // read all : 게시물에 번호가 넘어오면 댓글의 리스트가 나오면서 페이징 기법 적용
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
    // 페이징 처리 응답용 객체에 댓글 객체가 담겨 나옴 (p.554)
    
    // 1개 수정 : 프론트에서 DTO객체가 넘어오면 .save() 메서드 처리
    void modify(ReplyDTO replyDTO);
    
    // 1개 삭제 : 프론트에서 rno(댓글번호)가 넘어오면 .deleteById()가 실행됨
    void remove(Long rno);





} //interface 종료
