package com.shinhan.bananaapp.replyprac.controller;


import com.shinhan.bananaapp.replyprac.dto.FreeBoardDTO;
import com.shinhan.bananaapp.replyprac.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/freeboard")
@RequiredArgsConstructor
public class FreeBoardController {

    private final FreeBoardService boardService;

    // ── 목록 조회 ──────────────────────────────────
    // GET /freeboard/list
    @GetMapping("/list")
    public ResponseEntity<List<FreeBoardDTO>> getList() {
        List<FreeBoardDTO> list = boardService.getList();
        return ResponseEntity.ok(list);
    }

    // ── 단건 조회 ──────────────────────────────────
    // GET /freeboard/detail?bno=1
    @GetMapping("/detail")
    public ResponseEntity<FreeBoardDTO> getDetail(
            @RequestParam Long bno) {

        FreeBoardDTO dto = boardService.f_detail(bno);

        if (dto == null)
            return ResponseEntity.notFound().build(); // 404

        return ResponseEntity.ok(dto);
    }

    // ── 등록 ───────────────────────────────────────
    // POST /freeboard/register
    // Body: { "title": "제목", "content": "내용", "writer": "홍길동" }
    @PostMapping("/register")
    public ResponseEntity<FreeBoardDTO> register(
            @RequestBody FreeBoardDTO dto) {

        FreeBoardDTO saved = boardService.register(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)  // 201
                .body(saved);
    }

    // ── 수정 ───────────────────────────────────────
    // PUT /freeboard/modify
    // Body: { "bno": 1, "title": "수정 제목", "content": "수정 내용" }
    @PutMapping("/modify")
    public ResponseEntity<Void> modify(
            @RequestBody FreeBoardDTO dto) {

        boardService.modify(dto);
        return ResponseEntity.ok().build(); // 200
    }

    // ── 삭제 ───────────────────────────────────────
    // DELETE /freeboard/remove?bno=1
    @DeleteMapping("/remove")
    public ResponseEntity<Void> remove(
            @RequestParam Long bno) {

        boardService.remove(bno);
        return ResponseEntity.ok().build(); // 200
    }
}
