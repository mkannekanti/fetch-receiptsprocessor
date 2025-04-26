package org.fetch.receiptsprocessor.rules;

import lombok.extern.slf4j.Slf4j;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TotalRoundDollarAmountRule implements Rule {
    final Long POINTS_IF_TOTAL_IS_ROUND = 50L;

    @Override
    public RuleType getRuleType() {
        return RuleType.TOTAL_ROUND_DOLLAR_AMT;
    }

    @Override
    public Boolean isEligible(String id, Receipt receipt) {
        /*
         * Total is a round dollar amount with no cents.
         *
         * e.g., 3 3.00
         * nit: technically 3.0 and 3. are not valid amount strings.
         */

        String total = receipt.getTotal();

        // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
        boolean isEligible = total.matches("^\\d+(\\.0{2})?$");

        if (isEligible) {
            log.info("[{}]: Eligible since Total({}) is a round dollar amount", id, total);
        } else {
            log.info("[{}]: Not Eligible since Total({}) is not a round dollar amount", id, total);
        }

        return isEligible;
    }

    @Override
    public Long calculatePoints(String id, Receipt receipt) {
        /*
         * Assign 50 points
         */

        long points = POINTS_IF_TOTAL_IS_ROUND;

        log.info("[{}]: Earned {} Points based on Total({})", id, points, receipt.getTotal());
        return points;
    }
}
