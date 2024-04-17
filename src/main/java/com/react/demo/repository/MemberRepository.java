package com.react.demo.repository;

import com.react.demo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository  extends JpaRepository<Member, String> {
}
