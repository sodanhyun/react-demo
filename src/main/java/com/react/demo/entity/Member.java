package com.react.demo.entity;

import com.react.demo.constant.Role;
import com.react.demo.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name="member")
@Getter
@Setter
public class Member {

    @Id
    @Column(name="member_id")
    private String id;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto dto,  PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setId(dto.getId());
        member.setName(dto.getName());
        String password = passwordEncoder.encode(dto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }

}
