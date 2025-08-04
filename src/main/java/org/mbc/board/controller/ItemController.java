package org.mbc.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.action.internal.EntityActionVetoException;
import org.mbc.board.dto.ItemFormDTO;
import org.mbc.board.dto.ItemSearchDTO;
import org.mbc.board.dto.upload.UploadResultDTO;
import org.mbc.board.entity.Item;
import org.mbc.board.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/admin/item")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/new")
    public String itemForm(Model model) {
        model.addAttribute("itemFormDTO", new ItemFormDTO());
        return "item/itemForm";
    }

    @PostMapping("/new")
    public String itemNew(@Valid ItemFormDTO itemFormDTO,
                          BindingResult bindingResult,
                          Model model,
                          @RequestParam("files") List<MultipartFile> files) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (files == null || files.isEmpty()) {
            model.addAttribute("errorMessage", "상품 이미지는 최소 1개 이상 등록해야 합니다");
            return "item/itemForm";
        }

        try {
            // 업로드 서비스 호출
            List<UploadResultDTO> uploadResultDTOList = itemService.uploadFiles(files);

            // 저장 서비스 호출 (DTO + 업로드 결과 리스트)
            itemService.saveItem(itemFormDTO, uploadResultDTOList);
        } catch (Exception e) {
            log.error("상품 등록 중 에러", e);
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping("/{mid}")
    public String itemDtl(@PathVariable("mid") Long mid, Model model) {
        try {
            ItemFormDTO itemFormDTO = itemService.getItemDtl(mid);
            model.addAttribute("itemFormDTO", itemFormDTO);
        } catch (EntityActionVetoException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다");
            model.addAttribute("itemFormDTO", new ItemFormDTO());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping("/{mid}")
    public String itemUpdate(@Valid ItemFormDTO itemFormDTO,
                             BindingResult bindingResult,
                             @RequestParam(value = "files", required = false) List<MultipartFile> files,@RequestParam(value = "deleteImgIds", required = false) List<Long> deleteImgIds,
                             Model model) {

        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }

        if (files == null || files.isEmpty()) {
            model.addAttribute("errorMessage", "상품 이미지는 최소 1개 이상 등록해야 합니다");
            return "item/itemForm";
        }

        try {
            List<UploadResultDTO> uploadResultDTOList = itemService.uploadFiles(files);
            itemService.updateItem(itemFormDTO, uploadResultDTOList);

            if (deleteImgIds != null && deleteImgIds.isEmpty()) {
                for(Long imgId : deleteImgIds) {
                    itemService.getItemDtl(imgId);
                }// for종료
            }//if종료



        } catch (Exception e) {
            log.error("상품 수정 중 에러", e);
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping("/{page}")
    public String itemManage(ItemSearchDTO itemSearchDTO,
                             @PathVariable("page") Optional<Integer> page,
                             Model model) {

        Pageable pageable = PageRequest.of(page.orElse(0), 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDTO, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDTO", itemSearchDTO);
        model.addAttribute("maxPage", 5);

        return "item/itemMng";
    } //itemManage 종료
} // class종료
