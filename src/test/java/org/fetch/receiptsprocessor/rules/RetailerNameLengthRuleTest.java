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
class RetailerNameLengthRuleTest {
    final RetailerNameLengthRule rule = new RetailerNameLengthRule();

    final String id = "dummy";

    @Mock
    Receipt receipt;

    @Test
    void getRuleTypeShouldReturnCorrectRuleType() {
        assertSame(RuleType.RETAILER_NAME_LEN, rule.getRuleType());
    }

    private static Stream<Arguments> getRetailerNameEligibilityMap() {
        return Stream.of(
                Arguments.of("", false),
                Arguments.of(" ", false),
                Arguments.of("a", true),
                Arguments.of(" a b  ", true),
                Arguments.of("1234", true),
                Arguments.of(" a12 b34 & $ %  ", true),
                Arguments.of("a&-!@#b", true)
        );
    }

    @ParameterizedTest()
    @MethodSource("getRetailerNameEligibilityMap")
    void isRetailerNameRuleEligibleForThisReceipt(String name, boolean expected) {
        // setup

        // mock
        when(receipt.getRetailer()).thenReturn(name);

        // act
        boolean actual = rule.isEligible(id, receipt);

        // verify
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> getRetailerNamePointsMap() {
        return Stream.of(
                Arguments.of("", 0L),
                Arguments.of(" ", 0L),
                Arguments.of("a", 1L),
                Arguments.of(" a b  ", 2L),
                Arguments.of("1234", 4L),
                Arguments.of(" a12 b34 & $ %  ", 6L),
                Arguments.of("a&-!@#b", 2L),
                Arguments.of("a".repeat(100) + " ".repeat(10) + "1".repeat(100), 200L)
        );
    }
    @ParameterizedTest
    @MethodSource("getRetailerNamePointsMap")
    void validatePointsForThisReceipt(String name, Long expected) {
        // setup

        // mock
        when(receipt.getRetailer()).thenReturn(name);

        // act
        long actual = rule.calculatePoints(id, receipt);

        // verify
        assertEquals(expected, actual);
    }
}