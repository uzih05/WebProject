package com.example.myweb.Controller;

import com.example.myweb.model.Item;
import com.example.myweb.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ItemRepository itemRepository;

    // 게시글 목록
    @GetMapping("/list")
    public String getPostList(Model model) {
        List<Item> items = itemRepository.findAll();  // 게시글을 가져오는 서비스 호출
        // 게시글에 번호 매기기
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setNumber(i + 1); // 순차적으로 번호를 매김 (1번부터 시작)
        }
        model.addAttribute("items", items);  // 게시글 목록을 모델에 추가
        return "list"; // list.html 화면을 반환
    }

    // 게시글 작성 페이지
    @GetMapping("/new")
    public String newItemForm() {
        return "new";  // new.html 페이지로 이동
    }

    // 게시글 저장
    @PostMapping("/save")
    public String saveItem(@RequestParam String name, @RequestParam String author, @RequestParam String description) {
        Item item = new Item();
        item.setName(name);
        item.setAuthor(author);
        item.setDescription(description);
        itemRepository.save(item);  // 데이터베이스에 저장
        return "redirect:/list";  // 저장 후 게시글 목록 페이지로 리다이렉트
    }

    // 게시글 상세 페이지
    @GetMapping("/view/{id}")
    public String viewItem(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id).orElse(null);  // 아이디로 게시글 조회
        model.addAttribute("item", item);  // 상세 페이지에 데이터 전달
        return "view";  // view.html 화면을 반환
    }

    // 게시글 삭제
    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemRepository.deleteById(id);  // 게시글 삭제
        return "redirect:/list";  // 삭제 후 게시글 목록 페이지로 리다이렉트
    }
}