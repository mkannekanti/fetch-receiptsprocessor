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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OddPurchaseDayRuleTest {
    final OddPurchaseDayRule rule = new OddPurchaseDayRule();

    final String id = "dummy";

    @Mock
    Receipt receipt;

    @Test
    void getRuleTypeShouldReturnCorrectRuleType() {
        assertSame(RuleType.ODD_PURCHASE_DAY, rule.getRuleType());
    }

    private static Stream<Arguments> getPurchaseDayEligibilityMap() {
        return Stream.of(
                Arguments.of("2000-12-20", false),
                Arguments.of("2025-11-11", true),
                Arguments.of("2022-03-21", true)
        );
    }

    @ParameterizedTest
    @MethodSource("getPurchaseDayEligibilityMap")
    void isPurchaseDayRuleEligibleForThisReceipt(String date, boolean expected) {
        // setup

        // mock
        when(receipt.getPurchaseDate()).thenReturn(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));

        // act
        boolean actual = rule.isEligible(id, receipt);

        // verify
        assertEquals(expected, actual);
    }

    @Test
    void validatePointsForThisReceipt() {
        assertSame(6L, rule.calculatePoints(id, receipt));
    }
}