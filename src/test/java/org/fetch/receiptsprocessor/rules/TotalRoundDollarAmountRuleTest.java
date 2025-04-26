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
class TotalRoundDollarAmountRuleTest {
    TotalRoundDollarAmountRule rule = new TotalRoundDollarAmountRule();

    String id = "dummy";

    @Mock
    Receipt receipt;

    @Test
    void getRuleTypeShouldReturnCorrectRuleType() {
        assertSame(RuleType.TOTAL_ROUND_DOLLAR_AMT, rule.getRuleType());
    }

    private static Stream<Arguments> getTotalEligibilityMap() {
        return Stream.of(
                Arguments.of("30.00", true),
                Arguments.of("0.12", false),
                Arguments.of("0.00", true),
                Arguments.of("4.12", false)
        );
    }

    @ParameterizedTest()
    @MethodSource("getTotalEligibilityMap")
    void isRoundDollarAmountRuleEligibleForThisReceipt(String total, boolean expected) {
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
        assertSame(50L, rule.calculatePoints(id, receipt));
    }
}