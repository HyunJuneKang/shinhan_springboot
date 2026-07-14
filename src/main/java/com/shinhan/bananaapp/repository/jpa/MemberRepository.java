package com.shinhan.bananaapp.repository.jpa;

import com.shinhan.bananaapp.entity3.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity,String> {
}
