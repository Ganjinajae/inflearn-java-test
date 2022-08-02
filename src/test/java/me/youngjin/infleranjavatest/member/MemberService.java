package me.youngjin.infleranjavatest.member;

import me.youngjin.infleranjavatest.domain.Member;

import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(Long memberId);

    void validate(Long memberId);
}
