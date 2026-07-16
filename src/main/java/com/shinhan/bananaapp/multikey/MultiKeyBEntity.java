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
@Table(name = "tbl_childB")
public class MultiKeyBEntity {
    @EmbeddedId
    MultiKeyB id;

    String name;
    String phone;
}
