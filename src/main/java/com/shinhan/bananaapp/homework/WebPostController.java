package com.shinhan.bananaapp.homework;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class WebPostController {

    private final WebPostService boardService;

    // ── 단일 페이지 ──────────────────────────────
    @GetMapping({"", "/", "/index"})
    public String index(
            @RequestParam(defaultValue = "0")  int    page,
            @RequestParam(required = false)    String keyword,
            Model model) {

        Page<WebPostDTO> postPage =
                boardService.getPostList(page, keyword);

        model.addAttribute("postPage",    postPage);
        model.addAttribute("keyword",     keyword);
        model.addAttribute("currentPage", page);
        return "board/index";
    }

    // ── 게시글 등록 (Ajax POST) ───────────────────
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<?> save(
            @RequestBody WebPostDTO dto
            //,            @AuthenticationPrincipal UserDetails user
    ) {

        //dto.setWriter(user.getUsername());
        //dto.setWriterId(user.getUsername());
        dto.setWriter("찐");
        dto.setWriterId("jin");
        Long id = boardService.savePost(dto);
        System.out.println(id + "등록" + dto);
        return ResponseEntity.ok(Map.of("id", id, "msg", "등록완료"));
    }

    // ── 게시글 수정 (Ajax PUT) ────────────────────
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody WebPostDTO dto
             //,          @AuthenticationPrincipal UserDetails user
    ) {

        boardService.updatePost(id, dto);
        return ResponseEntity.ok(Map.of("msg", "수정완료"));
    }

    // ── 게시글 삭제 (Ajax DELETE) ─────────────────
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(
            @PathVariable Long id
           // @AuthenticationPrincipal UserDetails user
           ) {

        boardService.deletePost(id);
        return ResponseEntity.ok(Map.of("msg", "삭제완료"));
    }

    // ── 게시글 목록 갱신용 (Ajax GET) ─────────────
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(
            @RequestParam(defaultValue = "0")  int    page,
            @RequestParam(required = false)    String keyword) {

        return ResponseEntity.ok(boardService.getPostList(page, keyword));
    }
}