package com.shinhan.bananaapp.homework;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebPostService {

    private final WebPostRepository    postRepository;
    private final WebCommentRepository commentRepository;

    // ── 게시글 목록 (페이징) ──────────────────────
    @Transactional(readOnly = true)
    public Page<WebPostDTO> getPostList(int page, String keyword) {
        Pageable pageable = PageRequest.of(
                page, 10, Sort.by("createdAt").descending());

        Page<WebPostEntity> entities =
                (keyword != null && !keyword.isBlank())
                        ? postRepository.searchByKeyword(keyword, pageable)
                        : postRepository.findAll(pageable);

        return entities.map(this::toPostDTO);
    }

    // ── 게시글 단건 (댓글 포함) ───────────────────
    @Transactional
    public WebPostDTO getPost(Long id) {
        WebPostEntity post = postRepository
                .findByIdWithComments(id);
        if (post == null)
            throw new RuntimeException("게시글 없음: " + id);

        post.increaseViewCount();  // 조회수 증가

        WebPostDTO dto = toPostDTO(post);
        dto.setComments(post.getComments().stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    // ── 게시글 등록 ───────────────────────────────
    @Transactional
    public Long savePost(WebPostDTO dto) {
        WebPostEntity entity = WebPostEntity.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .writerId(dto.getWriterId())
                .build();
        return postRepository.save(entity).getId();
    }

    // ── 게시글 수정 ───────────────────────────────
    @Transactional
    public void updatePost(Long id, WebPostDTO dto) {
        WebPostEntity post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));
        post.update(dto.getTitle(), dto.getContent());
    }

    // ── 게시글 삭제 ───────────────────────────────
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
        // orphanRemoval=true → 댓글 자동 삭제
    }

    // ── 댓글 목록 ─────────────────────────────────
    @Transactional(readOnly = true)
    public List<WebCommentDTO> getComments(Long postId) {
        return commentRepository
                .findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::toCommentDTO)
                .collect(Collectors.toList());
    }

    // ── 댓글 등록 ─────────────────────────────────
    @Transactional
    public WebCommentDTO saveComment(WebCommentDTO dto) {
        WebPostEntity post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        WebCommentEntity comment = WebCommentEntity.builder()
                .content(dto.getContent())
                .writer(dto.getWriter())
                .writerId(dto.getWriterId())
                .post(post)
                .build();

        return toCommentDTO(commentRepository.save(comment));
    }

    // ── 댓글 수정 ─────────────────────────────────
    @Transactional
    public WebCommentDTO updateComment(Long id, WebCommentDTO dto) {
        WebCommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글 없음"));
        comment.update(dto.getContent());
        return toCommentDTO(comment);
    }

    // ── 댓글 삭제 ─────────────────────────────────
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    // ── Entity → DTO 변환 ─────────────────────────
    private WebPostDTO toPostDTO(WebPostEntity e) {
        return WebPostDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .content(e.getContent())
                .writer(e.getWriter())
                .writerId(e.getWriterId())
                .viewCount(e.getViewCount())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .commentCount(e.getComments().size())
                .build();
    }

    private WebCommentDTO toCommentDTO(WebCommentEntity e) {
        return WebCommentDTO.builder()
                .id(e.getId())
                .content(e.getContent())
                .writer(e.getWriter())
                .writerId(e.getWriterId())
                .postId(e.getPost().getId())
                .createdAt(e.getCreatedAt())
                .build();
    }
}