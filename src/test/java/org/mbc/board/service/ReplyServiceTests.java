package org.mbc.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mbc.board.dto.ReplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister(){
        // 프론트에서 dto가 넘어오면 댓글 DB에 등록 insert

        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("서비스에서 댓글등록")
                .replyer("서비스테스트")
                .bno(98L)
                .build();
        log.info("=====testRegister()메서드 실행=====");
        log.info(replyService.register(replyDTO));

        // Hibernate:
        //    insert
        //    into
        //        reply
        //        (board_bno, moddate, regdate, reply_text, replyer)
        //    values
        //        (?, ?, ?, ?, ?)
        //2025-07-23T10:38:31.728+09:00  INFO 248 --- [board] [    Test worker] o.mbc.board.service.ReplyServiceTests    : 2



    } // testRegister 종료
} // class종료
