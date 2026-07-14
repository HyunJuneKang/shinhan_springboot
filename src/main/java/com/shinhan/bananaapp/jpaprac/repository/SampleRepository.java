package com.shinhan.bananaapp.jpaprac.repository;

import com.shinhan.bananaapp.jpaprac.entity.entity1.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//JPA사용: 개발자는 Repository를 설계한다. 구현은 Spring이 런타임에 생성.
public interface SampleRepository extends
        JpaRepository<SampleEntity,Integer> {

}
