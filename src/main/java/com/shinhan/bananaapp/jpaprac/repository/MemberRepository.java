package com.shinhan.bananaapp.jpaprac.repository;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity,String> {
}
