package me.youngjin.infleranjavatest;

import me.youngjin.infleranjavatest.domain.Member;
import me.youngjin.infleranjavatest.domain.Study;
import me.youngjin.infleranjavatest.member.MemberService;
import me.youngjin.infleranjavatest.study.StudyRepository;
import me.youngjin.infleranjavatest.study.StudyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock MemberService memberService;

    @Mock
    StudyRepository studyRepository;

    // static 안 붙이면 각 테스트마다 인스턴스 만들어서 사용 -> beforeEach에서 데이터 삭제하고 사용하면 되니까
    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("studytest");

    @BeforeEach
    void beforeEach() {
        studyRepository.deleteAll();
    }

    @Test
    void createNewStudy() {
        // given
        StudyService studyService = new StudyService(memberService, studyRepository); // 구현체가 없는 경우 mock을 사용하기 좋다.
        Assertions.assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("youngjinjee@email.com");
        Study study = new Study(10, "테스트");
        // 객체를 stubbing 하자
        Mockito.when(memberService.findById(ArgumentMatchers.any())).thenReturn(Optional.of(member));
        Mockito.when(studyRepository.save(study)).thenReturn(study);

        BDDMockito.given(memberService.findById(1L)).willReturn(Optional.of(member));
        BDDMockito.given(studyRepository.save(study)).willReturn(study);

        Optional<Member> byId = memberService.findById(1L);
        Assertions.assertEquals("youngjinjee@email.com", byId.get().getEmail());

        // when
        studyService.createNewStudy(1L, study);

        // then
        Mockito.verify(memberService, Mockito.times(1)).notify(study);
        BDDMockito.then(memberService).should(Mockito.times(1)).notify(study);

        Mockito.verify(memberService, Mockito.never()).validate(ArgumentMatchers.any());
        BDDMockito.then(memberService).shouldHaveNoMoreInteractions();

        // inOrder, verifyNoMoreInteractions
    }

}
