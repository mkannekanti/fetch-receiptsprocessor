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
class TimeRangeRuleTest {
    TimeRangeRule rule = new TimeRangeRule();

    String id = "dummy";

    @Mock
    Receipt receipt;

    @Test
    void getRuleTypeShouldReturnCorrectRuleType() {
        assertSame(RuleType.TIME_RANGE, rule.getRuleType());
    }

    private static Stream<Arguments> getPurchaseTimeEligibilityMap() {
        return Stream.of(
                Arguments.of("10:00", false),
                Arguments.of("14:00", true),
                Arguments.of("15:59", true),
                Arguments.of("16:00", true),
                Arguments.of("16.01", false),
                Arguments.of("23.50", false),
                Arguments.of("58.59", false)
        );
    }

    @ParameterizedTest()
    @MethodSource("getPurchaseTimeEligibilityMap")
    void isTimeRangeRuleEligibleForThisReceipt(String purchaseTime, boolean expected) {
        // setup

        // mock
        when(receipt.getPurchaseTime()).thenReturn(purchaseTime);

        // act
        boolean actual = rule.isEligible(id, receipt);

        // verify
        assertEquals(expected, actual);
    }

    @Test
    void validatePointsForTotal() {
        assertEquals(10L, rule.calculatePoints(id, receipt));
    }
}