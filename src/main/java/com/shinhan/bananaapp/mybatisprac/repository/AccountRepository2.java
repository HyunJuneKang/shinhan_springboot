package com.shinhan.bananaapp.mybatisprac.repository;

import com.shinhan.bananaapp.mybatisprac.prev.AccountDTO;
import com.shinhan.bananaapp.mybatisprac.prev.AccountSearchDTO;
import com.shinhan.bananaapp.mybatisprac.prev.AttachmentDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AccountRepository2 {

    private final SqlSession sqlSession;

    private static final String NS =
            "com.shinhan.bananaapp.mapper.AccountMapper.";

    public List<AccountDTO> findAll() {
        return sqlSession.selectList(NS + "findAll");
    }

    public AccountDTO findById(Long id) {
        return sqlSession.selectOne(NS + "findById", id);
    }

    public List<AccountDTO> findByCondition(AccountSearchDTO search) {
        return sqlSession.selectList(NS + "findByCondition", search);
    }

    public int count() {
        return sqlSession.selectOne(NS + "count");
    }

    public int insert(AccountDTO account) {
        return sqlSession.insert(NS + "insert", account);
    }

    public int update(AccountDTO account) {
        return sqlSession.update(NS + "update", account);
    }

    public int delete(Long id) {
        return sqlSession.delete(NS + "delete", id);
    }

    public int withdraw(Map<String, Object> map) {
        return sqlSession.update(NS + "withdraw", map);
    }

    public int deposit(Map<String, Object> map) {
        return sqlSession.update(NS + "deposit", map);
    }

    public void insertAttachment(AttachmentDTO dto) {
        sqlSession.insert(NS + "insertAttachment", dto);
    }
}
