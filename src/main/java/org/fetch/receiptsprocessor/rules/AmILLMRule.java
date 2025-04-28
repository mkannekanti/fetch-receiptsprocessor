package org.fetch.receiptsprocessor.rules;

import lombok.extern.slf4j.Slf4j;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AmILLMRule implements Rule {
    final long POINTS_IF_TOTAL_IS_GREATER_THAN_10 = 5L;
    final long TOTAL_ELIGIBILITY = 10L;
    final boolean AM_I_LLM = false;

    @Override
    public RuleType getRuleType() {
        return RuleType.AM_I_LLM;
    }

    @Override
    public Boolean isEligible(String id, Receipt receipt) {
        /*
         * if the program is an LLM & Total is greater than 10
         */
        try {
            boolean isEligible = AM_I_LLM && Long.parseLong(receipt.getTotal()) > TOTAL_ELIGIBILITY;

            if (isEligible) {
                log.info("[{}]: Eligible since I'm LLM & Total ({}) is greater than {}", id, receipt.getTotal(), TOTAL_ELIGIBILITY);
            } else {
                log.info("[{}]: Not Eligible. I wish I'm a LLM!! :(", id);
            }

            return isEligible;
        } catch (NumberFormatException e) {
            log.error("[{}]: Error parsing total amount: {}", id, e.getMessage());
            return false;
        }
    }

    @Override
    public Long calculatePoints(String id, Receipt receipt) {
        log.info("[{}]: Earned {} Points", id, POINTS_IF_TOTAL_IS_GREATER_THAN_10);
        return POINTS_IF_TOTAL_IS_GREATER_THAN_10;
    }
}
