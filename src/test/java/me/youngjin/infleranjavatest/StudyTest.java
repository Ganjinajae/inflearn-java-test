package me.youngjin.infleranjavatest;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

// 테스트 메서드마다 StudyTest instance를 새롭게 만든다. -> 테스트간의 의존성을 없애기 위해서
// 이 기본전략을 변경할 수도 있다. -> 제약이 느슨해지거나 성능상 이점을 얻을 수도 있음 -> BeforeAll, AfterAll이 static method일 필요없음
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// 일반적으로 나열된 메서드 순서에 의존하면 안 된다. 순서를 중요하게 생각해서도 안 되고
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest  {

    int value = 1;

    @Order(2)
    @FastTest
    @DisplayName("스터디 만들기 fast")
//    @EnabledOnOs({OS.MAC, OS.LINUX})
//    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10, JRE.JAVA_11})
//    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    void create_new_study() {
        System.out.println(this);
        System.out.println("value: "+ value++);
        String testEnv = System.getenv("TEST_ENV"); // vi ~/.zshrc
        System.out.println(testEnv);
        assumeTrue("LOCAL".equalsIgnoreCase(testEnv));
        System.out.println("local");
        Study actual = new Study(10);
        org.assertj.core.api.Assertions.assertThat(actual.getLimit()).isGreaterThan(5);
        // 이런 환경변수 값에 따라 실행을 다르게 할 수 있다.
//        assumingThat("LOCAL".equalsIgnoreCase(testEnv), () -> {
//            System.out.println("local");
//            Study actual = new Study(10);
//            org.assertj.core.api.Assertions.assertThat(actual.getLimit()).isGreaterThan(5);
//        });
//        assumingThat("youngjin".equalsIgnoreCase(testEnv), () -> {
//            System.out.println("youngjin");
//            Study actual = new Study(10);
//            org.assertj.core.api.Assertions.assertThat(actual.getLimit()).isGreaterThan(5);
//        });
//        Study actual = new Study(10);
//        org.assertj.core.api.Assertions.assertThat(actual.getLimit()).isGreaterThan(5);
//        assertTimeout(Duration.ofMillis(100), () -> {
//            new Study(10);
//            Thread.sleep(300);
//        });
        // 조건 값으 기준을 넘어가면 바로 실패가 뜨도록한다.
        // 주의할 점: 코드블럭을 별도의 스레드에서 실행함
        // ThreadLocal 사용하는 코드가 있다. -> 예상치 못한 결과가 나올 수 있음
        // 일반적으로 다른 스레드와 공유가 안 됨. Transaction 관련 코드 위험
        // 관련 없는 코드는 괜찮다.
//        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
//            new Study(10);
//            Thread.sleep(300);
//        });
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
//        String message = exception.getMessage();
//        assertEquals("limit은 0보다 커야한다.", message);
//        Study study = new Study(-10);
//        assertNotNull(study);
//        assertTrue(1 < 2);
//        assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야한다.");
        // 성능을 신경쓰는 입장에서는 문자열로만 구성했을 경우 실패하든 성공하든 문자열 연산을 하는데
        // 람다식으로 했을 경우 최대한 필요한 시점에 한다.
        // 여기서 실패하면 다음 실패는 넘어가지 않음 -> assertAll을 쓴다.
//        assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값이 " + StudyStatus.DRAFT + "여야한다.");
//        assertAll(
//                () -> assertNotNull(study),
//                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "스터디를 처음 만들면 상태값이 " + StudyStatus.DRAFT + "여야한다."),
//                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야한다.")
//        );
    }

//    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "youngjin")
    @Order(1)
    @SlowTest
    @DisplayName("스터디 만들기 slow")
    @Disabled("disabled 테스트")
    void create_new_study_again() {
        System.out.println(this);
        System.out.println("value: " + value++);
        System.out.println("create1");
        assertNotNull("테스트");
    }

    @DisplayName("스터디 만들기")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void repeatTest(RepetitionInfo repetitionInfo) {
        assertNotNull("Repeated Test 테스트");
        System.out.println("test" + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }

    @DisplayName("스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "20, '스프링'"})
//    @EmptySource
//    @NullSource
//    @NullAndEmptySource
    void parameterizedTest(@AggregateWith(StudyAggregator.class) Study study) {
        assertNotNull("Parameterized Test 테스트");
        System.out.println(study.getLimit());
    }

    // public class 이거나 static inner class 이어야만 가능함
    // 그냥 해당 메서드 인자에 ArgumentAccessor를 사용해서 해도 됨
    // 이렇게 할 경우 Test Code 한 줄이 주는 정도
    static class StudyAggregator implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return  new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        }
    }

    // 파라미터 한 개만 받을 수 있다.
    static class StudyConverter extends SimpleArgumentConverter {

        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(Study.class, targetType, "Can only convert to Study");
            return new Study(Integer.parseInt(source.toString()));
        }
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each");
    }
}
