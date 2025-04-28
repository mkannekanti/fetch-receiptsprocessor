package org.fetch.receiptsprocessor.rules;

import lombok.extern.slf4j.Slf4j;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RetailerNameLengthRule implements Rule {
    final long POINTS_PER_VALID_CHAR = 1L;

    @Override
    public RuleType getRuleType() {
        return RuleType.RETAILER_NAME_LEN;
    }

    @Override
    public Boolean isEligible(String id, Receipt receipt) {
        /*
         * Retailer name must be non-empty.
         */
            String retailer = receipt.getRetailer();
            boolean isEligible = (retailer != null && !retailer.isBlank());

            if (isEligible) {
                log.info("[{}]: Eligible since retailer name ({}) is not blank", id, retailer);
            } else {
                log.info("[{}]: Not Eligible since retailer name is empty or blank", id);
            }

            return isEligible;
    }

    @Override
    public Long calculatePoints(String id, Receipt receipt) {
        /*
         * 1 point for every alphanumeric character in the retailer name.
         */
        long alphaNumericCount = receipt
                .getRetailer()
                    .chars()
                    .filter(Character::isLetterOrDigit)
                    .count();

            long points = POINTS_PER_VALID_CHAR * alphaNumericCount;
            log.info("[{}]: Earned {} Points based on {} alpha numeric characters", id, points, alphaNumericCount);
            return points;
    }
}
