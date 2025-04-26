package org.fetch.receiptsprocessor.rules;

import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemPairCountRuleTest {
    final ItemPairCountRule rule = new ItemPairCountRule();

    final String id = "dummy";

    @Mock
    Receipt receipt;

    @Test
    void getRuleTypeShouldReturnCorrectRuleType() {
        assertSame(RuleType.ITEM_PAIR_CNT, rule.getRuleType());
    }

    @Test
    void isItemPairCountRuleEligibleForThisReceipt() {
        assertTrue(rule.isEligible(id, receipt));
    }

    private static Stream<Arguments> getItemPairCountPointsMap() {
        return Stream.of(
                Arguments.of(3, 5L),
                Arguments.of(1, 0L),
                Arguments.of(101, 250L),
                Arguments.of(4, 10L),
                Arguments.of(100, 250L),
                Arguments.of(2, 5L)
        );
    }
    @ParameterizedTest
    @MethodSource("getItemPairCountPointsMap")
    void validatePointsForThisReceipt(int itemCount, long expected) {
        // setup
        ArrayList items = Mockito.mock(ArrayList.class);

        // mock
        when(receipt.getItems()).thenReturn(items);
        when(items.size()).thenReturn(itemCount);

        // act
        long actual = rule.calculatePoints(id, receipt);

        // verify
        assertEquals(expected, actual);
    }
}