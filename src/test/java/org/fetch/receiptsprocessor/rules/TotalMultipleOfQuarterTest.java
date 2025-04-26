package org.fetch.receiptsprocessor.rules;

import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TotalMultipleOfQuarterTest {
    TotalMultipleOfQuarter rule = new TotalMultipleOfQuarter();

    String id = "dummy";

    @Mock
    Receipt receipt;

    @Test
    void getRuleTypeShouldReturnCorrectRuleType() {
        assertSame(RuleType.TOTAL_MULTIPLE_OF_QUARTER, rule.getRuleType());
    }

    private static Stream<Arguments> getTotalEligibilityMap() {
        return Stream.of(
                Arguments.of("30.00", true),
                Arguments.of("0.00", true),
                Arguments.of("0.25", true),
                Arguments.of("1.50", true),
                Arguments.of("4.75", true),
                Arguments.of("58.76", false)
        );
    }

    @ParameterizedTest()
    @MethodSource("getTotalEligibilityMap")
    void isTotalMultipleOfQuarterRuleEligibleForThisReceipt(String total, boolean expected) {
        // setup

        // mock
        when(receipt.getTotal()).thenReturn(total);

        // act
        boolean actual = rule.isEligible(id, receipt);

        // verify
        assertEquals(expected, actual);
    }

    @Test
    void validatePointsForTotal() {
        assertSame(25L, rule.calculatePoints(id, receipt));
    }
}