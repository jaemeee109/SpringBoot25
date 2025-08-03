package org.mbc.board.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mbc.board.constant.ItemSellStatus;
import org.mbc.board.dto.ItemFormDTO;
import org.mbc.board.entity.Item;
import org.mbc.board.entity.ItemImg;
import org.mbc.board.repository.ItemImgRepository;
import org.mbc.board.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i = 0; i<5 ; i++){
            String path = "C:/upload";
            String imageName = "image"+i+".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName,"image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        } // for 종료
        return multipartFileList;
    } //createMultipartFiles()종료

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        ItemFormDTO itemFormDTO = new ItemFormDTO();
        itemFormDTO.setItemNm("테스트상품");
        itemFormDTO.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDTO.setItemDetail("테스트 상품임");
        itemFormDTO.setPrice(1000);
        itemFormDTO.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long mid = itemService.saveItem(itemFormDTO, multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItem_MidOrderByMidAsc(mid);
        Item item = itemRepository.findById(mid).orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDTO.getItemNm(), item.getItemNm());
        assertEquals(itemFormDTO.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDTO.getPrice(), item.getPrice());
        assertEquals(itemFormDTO.getStockNumber(), item.getStockNumber());

// 원본 이미지 파일명 비교
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());

    } //saveItem() 종료
} // class종료
