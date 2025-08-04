package org.mbc.board.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.mbc.board.dto.ItemFormDTO;
import org.mbc.board.dto.ItemImgDTO;
import org.mbc.board.dto.upload.UploadResultDTO;
import org.mbc.board.entity.Item;
import org.mbc.board.entity.ItemImg;
import org.mbc.board.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final String uploadPath = "C:/upload"; // 환경에 맞게 수정
    private final FileService fileService;

    public Long saveItem(ItemFormDTO itemFormDTO, List<UploadResultDTO> uploadResultDTOList) {
        Item item = itemFormDTO.createItem();

        for (int i = 0; i < uploadResultDTOList.size(); i++) {
            UploadResultDTO dto = uploadResultDTOList.get(i);

            ItemImg itemImg = ItemImg.builder()
                    .imgName(dto.getUuid() + "_" + dto.getFileName())
                    .oriImgName(dto.getFileName())
                    .imgUrl("/upload/" + dto.getUuid() + "_" + dto.getFileName())
                    .repimgYn(i == 0 ? "Y" : "N")
                    .item(item)
                    .build();

            item.getImageSet().add(itemImg);
        }

        itemRepository.save(item);
        return item.getMid();
    }



    public Long updateItem(ItemFormDTO itemFormDTO, List<UploadResultDTO> uploadResultDTOList) {
        Item item = itemRepository.findByIdWithImage(itemFormDTO.getMid())
                .orElseThrow(EntityNotFoundException::new);

        item.updateItem(itemFormDTO);

        item.getImageSet().clear();

        for (int i = 0; i < uploadResultDTOList.size(); i++) {
            UploadResultDTO dto = uploadResultDTOList.get(i);

            ItemImg itemImg = ItemImg.builder()
                    .imgName(dto.getUuid() + "_" + dto.getFileName())
                    .oriImgName(dto.getFileName())
                    .imgUrl("/upload/" + dto.getUuid() + "_" + dto.getFileName())
                    .repimgYn(i == 0 ? "Y" : "N")
                    .item(item)
                    .build();

            item.getImageSet().add(itemImg);
        }

        return item.getMid();
    }

    public List<UploadResultDTO> uploadFiles(List<MultipartFile> files) {
        List<UploadResultDTO> resultList = new ArrayList<>();

        for (MultipartFile multipartFile : files) {
            String originalName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

            boolean image = false;

            try {
                multipartFile.transferTo(savePath.toFile());

                if (Files.probeContentType(savePath).startsWith("image")) {
                    image = true;
                    File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                    Thumbnails.of(savePath.toFile())
                            .size(200, 200)
                            .toFile(thumbFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            resultList.add(UploadResultDTO.builder()
                    .uuid(uuid)
                    .fileName(originalName)
                    .img(image)
                    .build());
        }

        return resultList;
    }

    @Transactional(readOnly = true)
    public ItemFormDTO getItemDtl(Long mid) {
        Item item = itemRepository.findById(mid)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDTO dto = ItemFormDTO.of(item);

        // item.getImageSet()에서 DTO로 변환
        List<ItemImgDTO> itemImgDTOList = item.getImageSet().stream()
                .map(itemImg -> {
                    ItemImgDTO imgDTO = new ItemImgDTO();
                    imgDTO.setMid(itemImg.getMid());
                    imgDTO.setImgUrl(itemImg.getImgUrl());
                    imgDTO.setOriImgName(itemImg.getOriImgName());
                    imgDTO.setRepImgYn(itemImg.getRepimgYn());
                    return imgDTO;
                })
                .toList();

        dto.setItemImgDTOList(itemImgDTOList);
        dto.setImgList(itemImgDTOList);

        return dto;
    }

    public void deleteItemImage(Long imgId) {
        Item item = itemRepository.findByItemImgId(imgId)
                .orElseThrow(() -> new EntityNotFoundException("이미지 삭제 대상 없음"));

        item.getImageSet().removeIf(img -> img.getMid().equals(imgId));
    }
}
