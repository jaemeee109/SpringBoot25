package org.mbc.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.mbc.board.constant.ItemSellStatus;
import org.mbc.board.dto.ItemSearchDTO;
import org.mbc.board.dto.MainItemDTO;
import org.mbc.board.dto.QMainItemDTO;
import org.mbc.board.entity.Item;
import org.mbc.board.entity.QItem;
import org.mbc.board.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {


    private JPAQueryFactory queryFactory;
    
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    } //ItemRepositoryCustomImpl 종료
    
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }//searchSellStatusEq 종료
    
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();
        
        if(StringUtils.equals("all", searchDateType) || searchDateType == null ){
            return null;
        }else if(StringUtils.equals("id",searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    } //regDtsAfter 종료

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createBy", searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;

    } //searchByLike종료

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {

        QueryResults<Item> result = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDTO.getSearchDateType()),
                        searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),
                        searchByLike(itemSearchDTO.getSearchBy(),
                                itemSearchDTO.getSearchQuery()))
                .orderBy(QItem.item.mid.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = result.getResults();
        long total = result.getTotal();
        return new PageImpl<>(content, pageable, total);


    }

    @Override
    public Page<MainItemDTO> getMainItemDTOPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDTO> results = queryFactory.select(
                        new QMainItemDTO(
                                item.mid,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDTO.getSearchQuery()))
                .orderBy(item.mid.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDTO> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);



    }//getMainItemDTOPage 종료



    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery)? null : QItem.item.itemNm.like("%"+searchQuery+"%");
    }//itemNmLike 종료

} //class종료
