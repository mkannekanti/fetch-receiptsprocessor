package org.fetch.receiptsprocessor.rules;

import org.fetch.receiptsprocessor.openapi.model.Item;
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

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemDescriptionLenMultiplierOf3RuleTest {
    final ItemDescriptionLenMultiplierOf3Rule rule = new ItemDescriptionLenMultiplierOf3Rule();

    final String id = "dummy";

    @Mock
    Receipt receipt;

    @Test
    void getRuleTypeShouldReturnCorrectRuleType() {
        assertSame(RuleType.ITEM_DESC_LEN_MULTIPLE_OF_3, rule.getRuleType());
    }

    @Test
    void isItemDescriptionLenMultiplierOf3RuleEligibleForThisReceipt() {
        assertTrue(rule.isEligible(id, receipt));
    }


    static Stream<Arguments> receiptItemsProvider() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                mockItem("Milk", "3.10")
                        ),
                        0L
                ),
                Arguments.of(
                        List.of(
                                mockItem(" Egg    Salad", "5.20")
                        ),
                        2L
                ),
                Arguments.of(
                        List.of(
                                mockItem("Egg & Salad 100g", "5.75"),
                                mockItem("Jam", "2.55")
                        ),
                        1L
                ),
                Arguments.of(
                        List.of(
                                mockItem("Egg", "4.10"),
                                mockItem("Bread", "3.20")
                        ),
                        1L
                )
        );
    }

    @ParameterizedTest
    @MethodSource("receiptItemsProvider")
    void validatePointsForThisReceipt(List<Item> items, long expected) {
        // setup

        // mock
        when(receipt.getItems()).thenReturn(items);

        // act
        long actual = rule.calculatePoints(id, receipt);

        // verify
        assertEquals(expected, actual);
    }

    private static Item mockItem(String shortDescription, String price) {
        Item item = Mockito.mock(Item.class);
        when(item.getShortDescription()).thenReturn(shortDescription);
        when(item.getPrice()).thenReturn(price);
        return item;
    }
}