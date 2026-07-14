package com.shinhan.bananaapp.jpaprac.service;

import com.shinhan.bananaapp.jpaprac.dto.Board2DTOMapping;
import com.shinhan.bananaapp.jpaprac.repository.BoardRepository2;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Board2Service {
    private final BoardRepository2 boardRepository2;
    private final ModelMapper modelMapper;
    /*
    모두조회 -> 1page(page당 5건), bno sort
    DTO return하기
    */
    public List<Board2DTOMapping> selectAll(){
        Pageable pageable1 = PageRequest.of(
                0,
                5,
                Sort.by(Sort.Direction.DESC,"bno")
        );
        return boardRepository2.findAll(pageable1)
                .getContent()
                .stream().map(board->{
                    Board2DTOMapping dto = modelMapper.map(board,Board2DTOMapping.class);
                    dto.setTitle("해킹범이다");
                    return dto;
                })
                .toList();
    }

    //GPT 피셜 page로 반환하면 page의 기능을 쓸 수 있따
    public Page<Board2DTOMapping> selectAll2() {

        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by(Sort.Direction.DESC, "bno")
        );

        return boardRepository2.findAll(pageable)
                .map(board ->
                        modelMapper.map(
                                board,
                                Board2DTOMapping.class
                        )
                );
    }
}
