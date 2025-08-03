package org.mbc.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.action.internal.EntityActionVetoException;
import org.mbc.board.dto.ItemFormDTO;
import org.mbc.board.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    }// itemForm() 종료

    @PostMapping( "/new")
    public String itemNew(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        
        if(bindingResult.hasErrors()) {
            return "item/itemForm";
        } // if 종료

        if (itemImgFileList.size() > 10){
            model.addAttribute("errorMessage","상품 이미지는 최대 10개까지 업로드 할 수 있습니다");
            return "item/itemForm"; // ← 이거 필수
        }

        if(itemImgFileList.get(0).isEmpty()&&itemFormDTO.getMid()==null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값 입니다");
            return "item/itemForm";
        }
        try{
            itemService.saveItem(itemFormDTO, itemImgFileList);
        }catch (Exception e) {
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생했습니다");
            return "item/itemForm";
        } // try-catch 종료

        return "redirect:/";
    } //  itemNew 종료
    
    @GetMapping("/{mid}")
    public String itemDtl(@PathVariable("mid") Long mid, Model model) {

        try{
            ItemFormDTO itemFormDTO = itemService.getItemDtl(mid);
            model.addAttribute("itemFormDTO", itemFormDTO);
        }catch (EntityActionVetoException e){
        model.addAttribute("errorMessage","존재하지 않는 상품입니다");
        model.addAttribute("itemFormDTO", new ItemFormDTO());
        return "item/itemForm";
        }// try-catch 종료
        return "item/itemForm";
    } //itemDtl 종료
    
    @PostMapping("/{mid}")
    public String itemUpdate(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {

        if(bindingResult.hasErrors()) {
            return "item/itemForm";
        } //if종료
        if (itemImgFileList.size() > 10){
            model.addAttribute("errorMessage","상품 이미지는 최대 10개까지 업로드 할 수 있습니다");
            return "item/itemForm"; // ← 이거 필수
        }
        if(itemImgFileList.get(0).isEmpty()&&itemFormDTO.getMid()==null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수 입력 값 입니다");
            return "item/itemForm";
        }   //if 종료
        try {
            itemService.updateItem(itemFormDTO, itemImgFileList);
        }catch (Exception e) {
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생했습니다");
            return "item/itemForm";
        } // try-catch 종료
        return "redirect:/";
    } //itemUpdate()종료
}//class 종료
