package org.fetch.receiptsprocessor.rules;

import lombok.extern.slf4j.Slf4j;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@Slf4j
public class TimeRangeRule implements Rule {
    final long POINTS_IF_PURCHASED_IN_TIME_RANGE = 10L;

    @Override
    public RuleType getRuleType() {
        return RuleType.TIME_RANGE;
    }

    @Override
    public Boolean isEligible(String id, Receipt receipt) {
        /*
         * if purchase time is between 2pm to 4pm
         */
        try {
            boolean isEligible = false;

            String purchaseTime = receipt.getPurchaseTime();
            LocalTime time = LocalTime.parse(purchaseTime, DateTimeFormatter.ofPattern("HH:mm"));
            int hour = time.getHour();
            int minute = time.getMinute();

            // after 2pm and before 4pm
            if (hour >= 14 && (hour < 16 || (hour == 16 && minute == 0))) {
                isEligible = true;
            }

            if (isEligible) {
                log.info("[{}]: Eligible since purchase hour {} (Time: {}) is not between 2PM and 4PM", id, hour, time);
            } else {
                log.info("[{}]: Not Eligible since purchase hour {} (Time: {}) is not between 2PM and 4PM", id, hour, time);
            }

            return isEligible;
        } catch (DateTimeParseException e) {
            log.error("Failed to parse purchaseData ({}): {}", receipt.getPurchaseTime(), e.getMessage());
        }

        return false;
    }

    @Override
    public Long calculatePoints(String id, Receipt receipt) {
        long points = POINTS_IF_PURCHASED_IN_TIME_RANGE;
        log.info("[{}]: Earned {} Points based on purchase time [{}]", id, points, receipt.getPurchaseTime());
        return points;
    }
}
