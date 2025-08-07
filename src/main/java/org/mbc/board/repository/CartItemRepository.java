package org.mbc.board.repository;

import org.mbc.board.dto.CartDetailDTO;
import org.mbc.board.entity.Cart;
import org.mbc.board.entity.CartItem;
import org.mbc.board.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartAndItem(Cart cart, Item item);

    @Query("select new org.mbc.board.dto.CartDetailDTO(ci.mid, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci " +
            "join ci.item i " +
            "join i.imageSet im " +
            "where ci.cart.mid = :cartId " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regDate desc")
    List<CartDetailDTO> findCartDetailDTOList(Long cartId);




}//interface 종료
