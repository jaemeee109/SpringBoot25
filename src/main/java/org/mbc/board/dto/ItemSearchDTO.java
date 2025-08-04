package org.mbc.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.mbc.board.constant.ItemSellStatus;

@Getter
@Setter
public class ItemSearchDTO {

    private String searchDateType;
    private ItemSellStatus searchSellStatus;
    private String searchBy;
    private String searchQuery = "";



} // class 종료
