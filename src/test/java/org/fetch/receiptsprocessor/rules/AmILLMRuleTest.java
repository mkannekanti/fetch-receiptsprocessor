package org.fetch.receiptsprocessor.rules;

import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AmILLMRuleTest {
    final AmILLMRule rule = new AmILLMRule();

    final String id = "dummy";

    @Mock
    Receipt receipt;

    @Test
    void getRuleTypeShouldReturnCorrectRuleType() {
        assertSame(RuleType.AM_I_LLM, rule.getRuleType());
    }

    @Test
    void isAmILLMRuleEligibleForThisReceipt() {
        assertFalse(rule.isEligible(id, receipt));
    }

    @Test
    void validatePointsForThisReceipt() {
        assertSame(5L, rule.calculatePoints(id, receipt));
    }
}