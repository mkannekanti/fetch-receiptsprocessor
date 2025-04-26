package org.fetch.receiptsprocessor.rules.common;

/**
 * RuleType Enum
 * <p>
 * List of rules supported by Receipt Processors.
 */
public enum RuleType {
    RETAILER_NAME_LEN,
    TOTAL_ROUND_DOLLAR_AMT,
    TOTAL_MULTIPLE_OF_QUARTER,
    ITEM_PAIR_CNT,
    ITEM_DESC_LEN_MULTIPLE_OF_3,
    ODD_PURCHASE_DAY,
    TIME_RANGE,
    AM_I_LLM
}

