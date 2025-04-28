package org.fetch.receiptsprocessor.rules;

import lombok.extern.slf4j.Slf4j;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class TotalMultipleOfQuarter implements Rule {
    final long POINTS_IF_TOTAL_MULTIPLE_OF_QUARTER = 25L;
    final double QUARTER = 0.25;

    @Override
    public RuleType getRuleType() {
        return RuleType.TOTAL_MULTIPLE_OF_QUARTER;
    }

    @Override
    public Boolean isEligible(String id, Receipt receipt) {
        /*
         * Total is multiple of 0.25
         *
         * NOTE: 0.0 is multiple of 0.25
         */
        try {
            String total = receipt.getTotal();
            boolean isEligible = new BigDecimal(receipt.getTotal())
                    .remainder(new BigDecimal(QUARTER))
                    .compareTo(BigDecimal.ZERO) == 0;

            if (isEligible) {
                log.info("[{}]: Eligible since total {} is multiple of {}", id, total, QUARTER);
            } else {
                log.info("[{}]: Not Eligible since total {} is not multiple of {}", id, total, QUARTER);
            }

            return isEligible;
        } catch (NumberFormatException e) {
            log.error("[{}]: Error parsing total amount: {}", id, e.getMessage());
            return false;
        }
    }

    @Override
    public Long calculatePoints(String id, Receipt receipt) {
        /*
         * 25 points if eligible.
         */
        long points = POINTS_IF_TOTAL_MULTIPLE_OF_QUARTER;
        log.info("[{}]: Earned {} Points based on total({})", id, points, receipt.getTotal());
        return points;
    }
}
