package com.shinhan.bananaapp.multikey;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//2개의 컬럼의 조합이 PK로 사용되는 경우
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable//다른곳에서 나를 가져갈거임
public class MultiKeyB {
    Integer id1;
    Integer id2;
}
