package org.mbc.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListAllDTO {
    // 게시물 list 페이지의 게시물, 이미지(썸네일),댓글 개수

    private Long bno;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime regDate;
    private Long replyCount;
    private List<BoardImageDTO> boardImages;
}
