package com.shinhan.bananaapp.mybatisprac.prev;

import lombok.*;

@NoArgsConstructor@AllArgsConstructor
@Getter@Setter@Builder
@ToString

public class MemberDTO {
    String email;
    String password;
    String mName;
    String mRole; //권한check ---ADMIN,MANAGER,USER 권한 체크
}
