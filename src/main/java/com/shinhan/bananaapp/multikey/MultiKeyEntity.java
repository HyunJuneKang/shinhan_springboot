package com.shinhan.bananaapp.multikey;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_childA")
@IdClass(value = MultiKeyA.class)
public class MultiKeyEntity {
    @Id
    Integer id1;
    @Id
    Integer id2;

    String name;
    String phone;
}
