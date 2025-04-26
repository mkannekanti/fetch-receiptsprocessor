package org.fetch.receiptsprocessor.service;

import lombok.extern.slf4j.Slf4j;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ReceiptsProcessorService {
    final Map<String, Long> receiptPointsMap = new ConcurrentHashMap<>();
    final Map<String, String> receiptMap = new ConcurrentHashMap<>();
    final List<Rule> ruleList;

    @Autowired
    public ReceiptsProcessorService(List<Rule> rules) {
        /*
         * List<Rule> will be populated with all Rule implementations
         * tagged with @Component
         */
        log.info("Loaded {} rules",  rules.size());
        this.ruleList = rules;
    }

    /**
     * Process Receipt - processes the receipt and calculates the points.
     *
     * @param receipt - Receipt
     * @return id - ReceiptId
     */
    public Optional<String> processReceipt(Receipt receipt) {
        String id = String.valueOf(UUID.randomUUID());
        log.info("[{}]: Receipt: {}", id, receipt.toString());

        Long points = calculatePoints(id, receipt);

        log.info("[{}]: Total Points Earned = {}", id, points);
        receiptPointsMap.put(id, points);
        receiptMap.put(id, receipt.toString());

        return Optional.ofNullable(id);
    }

    /**
     * GetPoints - returns the points earned for this receipt.
     *
     * @param id receipt id
     * @return points earned
     */
    public Optional<Long> getPoints(String id) {
        Long points = receiptPointsMap.getOrDefault(id, null);

        if (points == null) {
            log.info("[{}]: Failed to find the receipt", id);
        } else {
            log.info("[{}]: Points earned is {}", id, points);
        }

        return Optional.ofNullable(points);
    }


    /**
     * calculatePoints - Calculate Reward Points.
     * <p>
     *
     * 1. checks the eligibility of all rules on this receipt.
     * 2. calculate points for eligible rules.
     * 3. adds all the rewards.
     * <p>
     * NOTE: we are leveraging the parallel execution for steps 1&2 as these rules are independent.
     *
     * @param id - receipt id
     * @param receipt - receipt
     * @return points earned
     */
    private Long calculatePoints(String id, Receipt receipt) {
        return ruleList
                .parallelStream() // execute them in parallel
                .filter(rule -> rule.isEligible(id, receipt))
                .map(rule -> rule.calculatePoints(id, receipt))
                .reduce(0L, Long::sum);
    }
}
