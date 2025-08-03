package org.mbc.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mbc.board.dto.ItemFormDTO;
import org.mbc.board.dto.ItemImgDTO;
import org.mbc.board.entity.Item;
import org.mbc.board.entity.ItemImg;
import org.mbc.board.repository.ItemImgRepository;
import org.mbc.board.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    
    public Long saveItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품등록
         Item item = itemFormDTO.createItem();
         itemRepository.save(item);

         //이미지 등록
        for(int i = 0; i <itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        } // for종료

        return item.getMid();

    } // saveItem() 종료
    
    
    @Transactional(readOnly = true)
    public ItemFormDTO getItemDtl(Long mid){
        // 상품 정보 가져오기

        List<ItemImg> itemImgList = itemImgRepository.findByItem_MidOrderByMidAsc(mid);

        List<ItemImgDTO> itemImgDTOList = new ArrayList<>();

        for(ItemImg itemImg : itemImgList) {
            ItemImgDTO itemImgDTO = ItemImgDTO.of(itemImg);
            itemImgDTOList.add(itemImgDTO);
        } // for 종료

        Item item = itemRepository.findById(mid).orElseThrow(EntityNotFoundException::new);
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
        itemFormDTO.setItemImgDTOList(itemImgDTOList);
        return itemFormDTO;
        
    } //getItemDtl 종료

    public Long updateItem(ItemFormDTO itemFormDTO, List<MultipartFile> itemImgFileList) throws Exception {

        //상품수정
        Item item = itemRepository.findById(itemFormDTO.getMid()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDTO);
        List<Long> itemImgIds = itemFormDTO.getItemImgIds();

        // 이미지등록
        for(int i = 0; i < itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }//for종료
        return item.getMid();
    } //updateItem() 종료
} //class 종료
