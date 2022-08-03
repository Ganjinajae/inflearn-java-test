package me.youngjin.infleranjavatest.member;

import me.youngjin.infleranjavatest.domain.Member;
import me.youngjin.infleranjavatest.domain.Study;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {
    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.empty();
    }

    @Override
    public void validate(Long memberId) {

    }

    @Override
    public void notify(Study study) {

    }
}
