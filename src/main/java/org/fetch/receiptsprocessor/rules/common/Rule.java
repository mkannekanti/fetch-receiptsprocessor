package org.fetch.receiptsprocessor.rules.common;

import org.fetch.receiptsprocessor.openapi.model.Receipt;

public interface Rule {
    /**
     * getRuleType - Returns the RuleType of this Rule.
     *
     * @return return RuleType
     */
    RuleType getRuleType();


    /**
     * isEligible - Checks the eligibility of this rule against given receipt
     *
     * @param  id receipt_id
     * @param receipt Receipt
     * @return true if this rule applies to this receipt
     */
    Boolean isEligible(String id, Receipt receipt);

    /**
     * calculatePoints - Calculate the reward points for this receipt based on this rule
     *
     * @param  id receipt_id
     * @param receipt Receipt
     * @return points
     */
    Long calculatePoints(String id, Receipt receipt);
}
