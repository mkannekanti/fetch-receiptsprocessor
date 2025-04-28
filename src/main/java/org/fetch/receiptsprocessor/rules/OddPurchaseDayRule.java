package org.fetch.receiptsprocessor.rules;

import lombok.extern.slf4j.Slf4j;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class OddPurchaseDayRule implements Rule {
    final long POINTS_IF_PURCHASED_ON_ODD_DAY = 6L;

    @Override
    public RuleType getRuleType() {
        return RuleType.ODD_PURCHASE_DAY;
    }

    @Override
    public Boolean isEligible(String id, Receipt receipt) {
        /*
         * if purchase day is odd
         */
        try {
            LocalDate purchaseDate = receipt.getPurchaseDate();
            int day = purchaseDate.getDayOfMonth();
            boolean isEligible = day % 2 == 1;

            if (isEligible) {
                log.info("[{}]: Eligible since purchase day {} (date: {}) is odd", id, day, purchaseDate);
            } else {
                log.info("[{}]: Not Eligible since purchase day {} (date: {}) is not odd", id, day, purchaseDate);
            }

            return isEligible;
        } catch (Exception e) {
            log.error("[{}]: Error checking purchase day eligibility: {}", id, e.getMessage());
            return false;
        }
    }

    @Override
    public Long calculatePoints(String id, Receipt receipt) {
        long points = POINTS_IF_PURCHASED_ON_ODD_DAY;

        log.info("[{}]: Earned {} Points based on purchase day [{}]", id, points, receipt.getPurchaseDate());
        return points;
    }
}
