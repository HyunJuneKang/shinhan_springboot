package com.shinhan.bananaapp.security;

import com.shinhan.bananaapp.jpaprac.entity.entity3.MemberEntity;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
public class SecurityUser extends User {

    private static final long serialVersionUID = 1L;
    private static final String ROLE_PREFIX = "ROLE_";

    // 기본 제공 생성자
    public SecurityUser(String name, String password,
                        Collection<? extends GrantedAuthority> authorities) {
        super(name, password, authorities);
        System.out.println("!!SecurityUser생성자에서 출력 member name:" + name);
    }

    // MemberEntity로 직접 생성 (개발자가 추가한 코드)
    public SecurityUser(MemberEntity member) {
        super(member.getMid(), member.getMpassword(), makeRole(member));
        System.out.println("!!개발자가 추가한MemberEntity  member:" + member);
    }

    // Role을 여러 개 가질 수 있도록 List로 반환
    private static List<GrantedAuthority> makeRole(MemberEntity member) {
        List<GrantedAuthority> roleList = new ArrayList<>();
        roleList.add(new SimpleGrantedAuthority(ROLE_PREFIX + member.getMrole()));
        return roleList;
    }
}
