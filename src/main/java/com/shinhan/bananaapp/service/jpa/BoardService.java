package com.shinhan.bananaapp.service.jpa;

import com.shinhan.bananaapp.entity1.BoardEntity;
import com.shinhan.bananaapp.repository.jpa.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardEntity> selectAll(){
        return boardRepository.findAll();
    }

    public BoardEntity selectById(Long bno){
        return boardRepository.findById(bno).orElseThrow(IllegalArgumentException::new);
    }
    //삭제
    public int delete(Long bno){
        boardRepository.deleteById(bno);
        return selectById(bno)==null?1:0;
    }
    @Transactional
    public BoardEntity update(Long bno,BoardEntity dto) {
        BoardEntity board = boardRepository.findById(bno).orElseThrow(IllegalArgumentException::new);
        board.setTitle(dto.getTitle());
        board.setWriter(dto.getWriter());
        return board;
    }
    @Transactional
    public BoardEntity createBoard(BoardEntity dto) {
        if(boardRepository.existsById(dto.getBno()))
            throw new IllegalArgumentException("이미 존재하는 게시판");
        BoardEntity savedBoard = boardRepository.save(dto);

        return dto;
    }
}
