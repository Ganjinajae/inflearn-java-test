package me.youngjin.infleranjavatest.study;

import me.youngjin.infleranjavatest.member.MemberService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class StudyServiceTest {

    @Test
    void createStudyService() {
        MemberService memberService = mock(MemberService.class);
        StudyRepository studyRepository = mock(StudyRepository.class);
        StudyService studyService = new StudyService(memberService, studyRepository); // 구현체가 없는 경우 mock을 사용하기 좋다.
        assertNotNull(studyService);
    }

}
