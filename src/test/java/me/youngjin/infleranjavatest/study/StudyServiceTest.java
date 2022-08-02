package me.youngjin.infleranjavatest.study;

import me.youngjin.infleranjavatest.domain.Member;
import me.youngjin.infleranjavatest.domain.Study;
import me.youngjin.infleranjavatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

//    @Mock
//    MemberService memberService;
//
//    @Mock
//    StudyRepository studyRepository;

    @Test
    void createNewStudy(@Mock MemberService memberService, @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository); // 구현체가 없는 경우 mock을 사용하기 좋다.
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("youngjinjee@email.com");
        // 객체를 stubbing 하자
        when(memberService.findById(any())).thenReturn(Optional.of(member));
//        studyService.createNewStudy(1L, study);
        assertEquals("youngjinjee@email.com", memberService.findById(1L).get().getEmail());
        assertEquals("youngjinjee@email.com", memberService.findById(2L).get().getEmail());

        // when은 리턴타입 있는 메서드를 확인할 때 쓰는데
//        when(memberService.findById(any())).thenThrow(new RuntimeException());
        // void 메서드 같은 경우는
        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L);
        });

        memberService.validate(2L);
    }

}
