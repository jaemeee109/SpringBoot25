package org.mbc.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mbc.board.constant.ItemSellStatus;
import org.mbc.board.entity.Item;
import org.mbc.board.entity.QItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
/*@TestPropertySource(locations = "classpath:application-test.properties") <- 오류남 */
@SpringBootTest
@Log4j2
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("===== 상품 저장 테스트=====")
    public void createItemTest() {

        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        log.info(savedItem.toString());

    }//createItemTest() 종료

    @Test
    public void createItemList() {

        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        } // for종료
    } //createItemList() 종료


    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트상품1");
        for (Item item : itemList) {
            log.info(item.toString());
        }// for종료

    }// findByItemNmTest() 종료

    @Test
    public void findByItemNmOrItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트상품1", "테스트 상품 상세설명5");
        for (Item item : itemList) {
            log.info(item.toString());
        } // for종료
    } // findByItemNmOrItemDetailTest() 종료

    @Test
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        // 10005 보다 가격이 작은 상품 조회
        for (Item item : itemList) {
            log.info(item.toString());
        } // for 종료
    } // findByPriceLessThanTest() 종료

    @Test
    public void findByPriceLessThanOrderByPriceDescTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
            log.info(item.toString());
        } // for 종료
    }//findByPriceLessThanOrderByPriceDescTest() 종료

    @Test
    public void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList) {
            log.info(item.toString());
        }//for 종료
    }//findByItemDetailTest() 

    @Test
    public void findByItemDetailNative() {

        this.createItemList();
        List<Item> itemList =
                itemRepository.findByItemDetailNative("테스트 상품 상세 설명");
        for (Item item : itemList) {
            log.info(item.toString());
        }//for 종료
    } // findByItemDetailNative() 종료
  
    @PersistenceContext
    EntityManager em;
    
    @Test
    public void queryDslTest(){
        this.createItemList();
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = jpaQueryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"테스트 상품 상세 설명"+"%"))
                .orderBy(qItem.price.desc());
        
        List<Item> itemList = query.fetch();
        for (Item item : itemList) {
            log.info(item.toString());
        } //for종료
    }//queryDslTest() 종료

    @Test
    public void createItemList2() {

        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        } // for종료

        for (int i = 6; i <= 6; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }//for종료

    } //createItemList() 종료


    @Test
    public void queryDslTest2(){
        this.createItemList2();
        
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        
        int price = 10003;
        String itemSellStat = "SELL";
        
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));
        
        if(StringUtils.equals(itemSellStat,ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }// if종료

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        itemRepository.findAll(booleanBuilder, pageable);
        log.info("total elements: "+itemPagingResult.getTotalElements());
        
        List<Item> resultList = itemPagingResult.getContent();
        for(Item resultItem: resultList){
            log.info(resultItem.toString());
        }// for종료
    }//queryDslTest() 종료

} //class종료
