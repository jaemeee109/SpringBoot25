package org.mbc.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mbc.board.dto.ItemSearchDTO;
import org.mbc.board.dto.MainItemDTO;
import org.mbc.board.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
@Log4j2
public class MainController {

    private final ItemService itemService;

    @GetMapping("")
    public String main(ItemSearchDTO itemSearchDTO, Optional<Integer> page, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,6);
        Page<MainItemDTO> items = itemService.getMainItemDTOPage(itemSearchDTO, pageable);
        model.addAttribute("items", items);
        model.addAttribute("ItemSearchDTO", itemSearchDTO);
        model.addAttribute("maxPage", 5);

        return "main";

    }//GemMapping-main 종료

} //class종료
