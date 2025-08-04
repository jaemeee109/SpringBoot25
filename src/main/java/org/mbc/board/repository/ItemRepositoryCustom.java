package org.mbc.board.repository;

import org.mbc.board.dto.ItemSearchDTO;
import org.mbc.board.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {


    Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);
} //interface 종료
