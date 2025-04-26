package org.fetch.receiptsprocessor.rules;

import lombok.extern.slf4j.Slf4j;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.fetch.receiptsprocessor.rules.common.RuleType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ItemPairCountRule implements Rule {
    final long POINTS_PER_PAIR = 5L;

    @Override
    public RuleType getRuleType() {
        return RuleType.ITEM_PAIR_CNT;
    }

    @Override
    public Boolean isEligible(String id, Receipt receipt) {
        return true;
    }

    @Override
    public Long calculatePoints(String id, Receipt receipt) {
        /*
         * 5 points for every two items on the receipt.
         */
        int itemsCount = receipt.getItems().size();
        int pairs = itemsCount / 2;
        long points = POINTS_PER_PAIR * pairs;

        log.info("[{}]: Earned {} Points based on {} pairs of items", id, points, pairs);
        return points;
    }
}
