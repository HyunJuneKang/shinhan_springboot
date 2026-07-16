package com.shinhan.bananaapp.homework;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class WebCommentController {

    private final WebPostService boardService;

    // ── 댓글 목록 ─────────────────────────────────
    // GET /api/comment?postId=1
    @GetMapping
    public ResponseEntity<List<WebCommentDTO>> list(
            @RequestParam Long postId) {
        return ResponseEntity.ok(boardService.getComments(postId));
    }

    // ── 댓글 등록 ─────────────────────────────────
    // POST /api/comment
    @PostMapping
    public ResponseEntity<WebCommentDTO> save(
            @RequestBody WebCommentDTO dto,
            @AuthenticationPrincipal UserDetails user
            ) {

        dto.setWriter(user.getUsername());
        dto.setWriterId(user.getUsername());
        return ResponseEntity.ok(boardService.saveComment(dto));
    }

    // ── 댓글 수정 ─────────────────────────────────
    // PUT /api/comment/{id}
    @PutMapping("/{id}")
    public ResponseEntity<WebCommentDTO> update(
            @PathVariable Long id,
            @RequestBody WebCommentDTO dto,
            @AuthenticationPrincipal UserDetails user
            ) {
        dto.setWriter(user.getUsername());
        dto.setWriterId(user.getUsername());
        return ResponseEntity.ok(boardService.updateComment(id, dto));
    }

    // ── 댓글 삭제 ─────────────────────────────────
    // DELETE /api/comment/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable Long id
            ) {
        boardService.deleteComment(id);
        return ResponseEntity.ok(Map.of("result", "삭제완료"));
    }
}