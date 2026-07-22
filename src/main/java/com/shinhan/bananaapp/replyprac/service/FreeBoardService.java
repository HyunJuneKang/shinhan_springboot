package com.shinhan.bananaapp.replyprac.service;

import com.shinhan.bananaapp.replyprac.dto.FreeBoardDTO;
import com.shinhan.bananaapp.replyprac.dto.FreeReplyDTO;
import com.shinhan.bananaapp.replyprac.entity.FreeBoardEntity;
import com.shinhan.bananaapp.replyprac.entity.FreeReplyEntity;
import com.shinhan.bananaapp.replyprac.repository.FreeBoardRepository;
import com.shinhan.bananaapp.replyprac.repository.FreeReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;// ── 게시글 삭제 ──
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/*
 * FreeBoard(게시글) - FreeReply(댓글) 양방향 연관관계 서비스
 *
 * - Controller와는 Entity가 아닌 DTO로만 주고받는다 (엔티티 직접 노출 금지)
 * - 서비스로 "들어올 때" : DTO → Entity 변환 후 Repository에 전달
 * - 서비스에서 "나갈 때" : Entity → DTO 변환 후 return
 * - 변환은 ModelMapper 사용. 단, FreeReplyEntity.board(FK 주인 객체) ↔ FreeReplyDTO.bno는
 *   필드명/타입이 달라 ModelMapper가 자동 매핑하지 못하므로 수동으로 세팅한다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FreeBoardService {

    private final FreeBoardRepository boardRepository;
    private final FreeReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    // ── 게시글 등록 ──
    public FreeBoardDTO register(FreeBoardDTO dto) {
        FreeBoardEntity entity = modelMapper.map(dto, FreeBoardEntity.class);
        FreeBoardEntity saved = boardRepository.save(entity);
        return entityToDto(saved);
    }

    // ── 게시글 목록 (댓글 fetch join → N+1 방지) ──
    @Transactional(readOnly = true)
    public List<FreeBoardDTO> getList() {
        return boardRepository.findAll2()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    // ── 게시글 단건 조회 ──
    // 존재하지 않는 bno면 예외 대신 null 반환 (Controller에서 404 처리하기 편하도록)
    @Transactional(readOnly = true)
    public FreeBoardDTO f_detail(Long bno) {
        return boardRepository.findById(bno)
                .map(this::entityToDto)
                .orElse(null);
    }

    // ── 게시글 수정 ──
    // save() 호출 없이 setter만 호출해도 트랜잭션 종료 시
    // JPA 변경 감지(Dirty Checking)로 자동 UPDATE 쿼리 발생.
    public void modify(FreeBoardDTO dto) {
        FreeBoardEntity board = findBoard(dto.getBno());
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());

    }

// FreeBoardEntity.replyList가 cascade = ALL 이므로
// 게시글 삭제 시 연관된 댓글도 함께 삭제된다.
public void remove(Long bno) {
        findBoard(bno);  // ← 존재 확인 (없으면 예외)
        boardRepository.deleteById(bno);
}

// ── 댓글 등록 ──
public FreeReplyDTO registerReply(Long bno, FreeReplyDTO dto) {
    FreeBoardEntity board = findBoard(bno);
    FreeReplyEntity reply = modelMapper.map(dto, FreeReplyEntity.class);
    reply.setBoard(board); // FK(board_bno) 세팅 — ModelMapper가 못 채우는 부분을 수동 보완
    FreeReplyEntity saved = replyRepository.save(reply);
    return replyToDto(saved);
}

// ── 특정 게시글의 댓글 목록 ──
@Transactional(readOnly = true)
public List<FreeReplyDTO> getReplyList(Long bno) {
    return findBoard(bno).getReplyList()
            .stream()
            .map(this::replyToDto)
            .collect(Collectors.toList());
}

// ── 댓글 삭제 ──
public void removeReply(Long rno) {

        replyRepository.deleteById(rno);
}

// ── 내부 공통 메서드 ──

private FreeBoardEntity findBoard(Long bno) {
    return boardRepository.findById(bno)
            .orElseThrow(() ->
                    new NoSuchElementException("게시글이 존재하지 않습니다. bno=" + bno));
}

// Entity → DTO (replyList는 LAZY이므로 반드시 @Transactional 안에서 호출되어야 함)
// ModelMapper가 entity.replyList → dto.replyList까지 한 번에 변환해주므로,
// 여기서는 ModelMapper가 채우지 못하는 각 reply의 bno(게시글 번호)만 보완한다.
private FreeBoardDTO entityToDto(FreeBoardEntity entity) {
    FreeBoardDTO dto = modelMapper.map(entity, FreeBoardDTO.class);

    List<FreeReplyDTO> replyList = dto.getReplyList() != null
            ? dto.getReplyList()
            : new ArrayList<>();

    replyList.forEach(replyDto -> replyDto.setBno(dto.getBno()));

    dto.setReplyList(replyList);
    dto.setReplyCount((long) replyList.size());
    return dto;
}

// Entity → DTO (board 객체 대신 bno만 세팅)
private FreeReplyDTO replyToDto(FreeReplyEntity entity) {
    FreeReplyDTO dto = modelMapper.map(entity, FreeReplyDTO.class);
    dto.setBno(entity.getBoard() != null ? entity.getBoard().getBno() : null);
    return dto;
}
}

