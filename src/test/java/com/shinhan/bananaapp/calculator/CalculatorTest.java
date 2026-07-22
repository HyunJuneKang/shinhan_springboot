package com.shinhan.bananaapp.calculator;

import com.shinhan.bananaapp.crosscuttingprac.aopprac.aop.CalculatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Calculator 단위 테스트")
class CalculatorTest {

    CalculatorImpl calc;

    @BeforeEach
    void setUp() {
        calc = new CalculatorImpl();  // 각 테스트 전 새 객체 생성
    }

    @Test
    @DisplayName("add: 2 + 3 = 5")
    void testAdd() {
        // assertEquals(기대값, 실제값)
        assertEquals(5,  calc.add(2, 3));
        assertEquals(0,  calc.add(-3, 3));
        assertEquals(-1, calc.add(-4, 3));
    }

    @Test
    @DisplayName("subtract: 10 - 4 = 6")
    void testSubtract() {
        assertEquals(6,  calc.subtract(10, 4));
        assertEquals(-2, calc.subtract(3, 5));
    }

    @Test
    @DisplayName("multiply: 결과가 0이 아님")
    void testMultiply_notZero() {
        assertNotEquals(0, calc.multiply(3, 4)); // 12 ≠ 0 → 통과
    }

    @Test
    @DisplayName("multiply: 결과가 양수인지")
    void testMultiply_positive() {
        int result = calc.multiply(3, 4); // 12
        assertTrue(result > 0,  "양수여야 합니다");  // 통과
        assertFalse(result < 0, "음수이면 안 됩니다"); // 통과
    }

    @Test
    @DisplayName("divide: 0으로 나누면 ArithmeticException")
    void testDivide_byZero() {
        // 람다 안에서 예외가 발생해야 통과
        assertThrows(ArithmeticException.class,
                () -> calc.divide(10, 0));
    }

    @Test
    @DisplayName("객체 null 검증")
    void testNullCheck() {
        assertNotNull(calc);    // calc가 null이 아님 → 통과
        assertNull(null);       // null임을 확인
    }

    @Test
    @DisplayName("assertAll: 사칙연산 한 번에 검증")
    void testAll() {
        assertAll("사칙연산 전체 검증",
                () -> assertEquals(5,  calc.add(2, 3),       "덧셈 실패"),
                () -> assertEquals(7,  calc.subtract(10, 3), "뺄셈 실패"),
                () -> assertEquals(12, calc.multiply(3, 4),  "곱셈 실패"),
                () -> assertEquals(5,  calc.divide(10, 2),   "나눗셈 실패")
        );
        // assertEquals 하나씩: 첫 번째 실패 시 나머지 실행 안 됨
        // assertAll:          전부 실행 후 실패 목록 한꺼번에 표시
    }

    @Test
    @DisplayName("AssertJ assertThat -- 가독성 높은 체이닝")
    void testWithAssertJ() {
        int result = calc.add(10, 5); // 15

        assertThat(result)
                .isEqualTo(15)       // 값이 15
                .isGreaterThan(10)   // 10보다 큼
                .isLessThan(20)      // 20보다 작음
                .isBetween(10, 20)   // 10~20 사이
                .isNotZero()         // 0이 아님
                .isPositive();       // 양수
    }

    @ParameterizedTest
    @DisplayName("add: 다양한 입력값 반복 테스트")
    @CsvSource({
            "1,  2,  3",   // add(1,2)   = 3
            "0,  0,  0",   // add(0,0)   = 0
            "-1, 1,  0",   // add(-1,1)  = 0
            "10, -3, 7",   // add(10,-3) = 7
    })
    void testAdd_parameterized(int a, int b, int expected) {
        assertEquals(expected, calc.add(a, b));
    }


}
