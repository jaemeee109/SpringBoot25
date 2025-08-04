package org.mbc.board.repository;

import org.mbc.board.dto.ItemSearchDTO;
import org.mbc.board.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {



    List<Item> findByItemNm(String itemNm);

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    // 상품명, 상품 상세설명 조회

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable);

    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail") String itemDetail);


    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select i from Item i join fetch i.imageSet where i.mid = :mid")
    Optional<Item> findByIdWithImage(Long mid);

    @Query("SELECT i FROM Item i JOIN i.imageSet img WHERE img.mid = :imgId")
    Optional<Item> findByItemImgId(@Param("imgId") Long imgId);




} // interface종료
