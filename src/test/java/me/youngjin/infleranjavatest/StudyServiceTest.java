package me.youngjin.infleranjavatest;

import me.youngjin.infleranjavatest.domain.Member;
import me.youngjin.infleranjavatest.domain.Study;
import me.youngjin.infleranjavatest.member.MemberService;
import me.youngjin.infleranjavatest.study.StudyRepository;
import me.youngjin.infleranjavatest.study.StudyService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock MemberService memberService;

    @Mock
    StudyRepository studyRepository;

    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("studytest");

    // manual 하게 container 관리
    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
        System.out.println(postgreSQLContainer.getJdbcUrl());
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
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
