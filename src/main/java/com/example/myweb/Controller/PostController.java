package com.example.myweb.Controller;

import com.example.myweb.Service.ItemService;
import com.example.myweb.model.Item;
import com.example.myweb.model.Post;
import com.example.myweb.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final ItemService itemService;

    @Autowired
    public PostController(PostService postService, ItemService itemService) {
        this.postService = postService;
        this.itemService = itemService;
    }

    @GetMapping("/list")
    public String getPostList(Model model) {
        List<Item> items = itemService.findAll();  // 게시글 목록을 가져옴

        // 게시글에 번호 매기기
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setNumber(i + 1); // 번호를 1번부터 매김
        }

        model.addAttribute("items", items);  // 모델에 게시글 리스트 추가
        return "list";  // list.html 페이지 반환
    }

    // 게시글 상세 보기
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        postService.getPostById(id).ifPresent(post -> model.addAttribute("post", post));
        return "post/view";  // view.html 페이지 반환
    }

    // 게시글 작성 폼
    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/new";  // new.html 페이지 반환
    }

    // 게시글 저장
    @PostMapping
    public String savePost(@ModelAttribute Post post) {
        postService.savePost(post);
        return "redirect:/posts";  // 게시글 목록으로 리다이렉트
    }

    // 게시글 삭제
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";  // 게시글 목록으로 리다이렉트
    }
}