package com.example.guestbook.controller;

import com.example.guestbook.dto.GuestbookDTO;
import com.example.guestbook.dto.PageRequestDTO;
import com.example.guestbook.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/guestbook")
@RequiredArgsConstructor//자동 주입을 위한 Annotation
public class GuestBookController {
    private final GuestbookService service;

    @GetMapping("/")
    public String index(){
        return "redirect:/guestbook/list";
    }
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list==================="+pageRequestDTO);

        model.addAttribute("result",service.getList(pageRequestDTO));
    }
    @GetMapping("/register")
    public void register(){
        log.info("register get.../././.././");
    }
    @PostMapping("register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes)
    {   //RedirectAttributes를 이용해서 한번만 화면에서 msg라는 변수를 사용할수 있게 처리, flash= 단한번만 데이터 전달
        log.info("dto///////////////"+dto);
        Long gno = service.register(dto);
        redirectAttributes.addFlashAttribute("msg" , gno);
        return "redirect:/guestbook/list";
    }
    @GetMapping({"/read","/modify"})
    public  void read(long gno, @ModelAttribute("requestDTO")PageRequestDTO pageRequestDTO, Model model){
        log.info("gno 리드에요 : "+gno);

        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto",dto);

    }
    @PostMapping("/remove")
    public  String remove(long gno, RedirectAttributes redirectAttributes){


        service.remove(gno);
        redirectAttributes.addFlashAttribute("msg", gno);
        log.info("삭제된 gno : "+gno);
        return "redirect:/guestbook/list";
    }
    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes){
    log.info("post modify:+++++++++++++++++++++++++++++++++++");
    log.info("dto : "+dto);
    service.modify(dto);
    redirectAttributes.addAttribute("page",requestDTO.getPage());
    redirectAttributes.addAttribute("type",requestDTO.getType());
    redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());
    redirectAttributes.addAttribute("gno",dto.getGno());

    return "redirect:/guestbook/read";
    }
}