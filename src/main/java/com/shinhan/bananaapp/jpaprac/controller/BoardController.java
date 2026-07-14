package com.shinhan.bananaapp.jpaprac.controller;

import com.shinhan.bananaapp.jpaprac.dto.Board2DTOMapping;
import com.shinhan.bananaapp.jpaprac.entity.entity1.BoardEntity;
import com.shinhan.bananaapp.jpaprac.service.Board2Service;
import com.shinhan.bananaapp.jpaprac.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final Board2Service board2Service;

    @GetMapping
    public List<BoardEntity> selectAll(){
        return boardService.selectAll();
    }
    @GetMapping("/{bno}")
    public BoardEntity selectById(@PathVariable Long bno){
        return boardService.selectById(bno);
    }
    @PostMapping
    public ResponseEntity<BoardEntity> createBoard(@RequestBody BoardEntity dto){
        BoardEntity board = boardService.createBoard(dto);
        return ResponseEntity.ok(board);
    }
    @PutMapping("/{bno}")
    public ResponseEntity<BoardEntity> updateBoard(@PathVariable Long bno,
                                      @RequestBody BoardEntity dto){
        BoardEntity updateBoard = boardService.update(bno,dto);
        return ResponseEntity.ok(updateBoard);
    }
    @DeleteMapping("/{bno}")
    public int f_delete(@PathVariable Long bno){
        return boardService.delete(bno);
    }

    @GetMapping("/page")
    public List<Board2DTOMapping>selectAll_page(){
        return board2Service.selectAll();
    }

}
