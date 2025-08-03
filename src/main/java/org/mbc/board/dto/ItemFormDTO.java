package org.mbc.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.mbc.board.constant.ItemSellStatus;
import org.mbc.board.entity.Item;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDTO {

    private Long mid;

    @NotBlank(message = "상품명 필수 입력")
    private String itemNm;

    @NotNull(message="가격 필수 입력")
    private Integer price;

    @NotBlank(message="이름 필수 입력")
    private String itemDetail;

    @NotNull(message="재고 필수 입력")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    } //createItem 종료

    public static ItemFormDTO of(Item item) {
        return modelMapper.map(item, ItemFormDTO.class);
    } //ItemFormDTO 종료

} // class 종료
