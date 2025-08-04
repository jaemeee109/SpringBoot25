package org.mbc.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.mbc.board.entity.ItemImg;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDTO {

    private Long mid;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDTO of(ItemImg itemImg) {
        // of() 는 Entity -> DTO로 변환해줌
        return modelMapper.map(itemImg, ItemImgDTO.class);
    }

} // class 종료