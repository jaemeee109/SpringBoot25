package org.mbc.board.repository;

import org.mbc.board.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItem_MidOrderByMidAsc(Long mid);;

}//interface 종료
