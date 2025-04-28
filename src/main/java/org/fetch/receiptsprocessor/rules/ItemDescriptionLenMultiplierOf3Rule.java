package org.fetch.receiptsprocessor.rules;

import lombok.extern.slf4j.Slf4j;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ItemDescriptionLenMultiplierOf3Rule implements Rule {
    final double POINT_MULTIPLIER = 0.2;

    @Override
    public RuleType getRuleType() {
        return RuleType.ITEM_DESC_LEN_MULTIPLE_OF_3;
    }

    @Override
    public Boolean isEligible(String id, Receipt receipt) {
        return true;
    }

    @Override
    public Long calculatePoints(String id, Receipt receipt) {
        try {
            long points = receipt
                    .getItems()
                    .stream()
                    .peek(item -> log.info("[{}]: Item ({}): Trimmed Length: {}, Price: {}",
                            id,
                            item.getShortDescription(),
                            item.getShortDescription().trim().length(),
                            item.getPrice()))
                    .filter(item -> item.getShortDescription().trim().length() % 3 == 0)
                    .peek(item -> log.info("[{}]: Item ({}) is eligible", id, item.getShortDescription()))
                    .map(item -> Math.round(Math.ceil(POINT_MULTIPLIER*Double.parseDouble(item.getPrice()))))
                    .reduce(0L, Long::sum);

            log.info("[{}]: Earned {} Points based on items trimmed lengths", id, points);
            return points;
        } catch (NumberFormatException e) {
            log.error("[{}]: Error parsing item price: {}", id, e.getMessage());
            return 0L;
        } catch (Exception e) {
            log.error("[{}]: Error calculating points: {}", id, e.getMessage());
            return 0L;
        }
    }
}
