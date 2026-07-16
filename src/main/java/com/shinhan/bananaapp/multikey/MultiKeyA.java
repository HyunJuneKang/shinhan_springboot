package com.shinhan.bananaapp.multikey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//2개의 컬럼의 조합이 PK로 사용되는 경우
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultiKeyA {
    Integer id1;
    Integer id2;
}
