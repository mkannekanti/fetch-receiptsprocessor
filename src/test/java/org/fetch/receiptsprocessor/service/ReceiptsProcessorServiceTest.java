package org.fetch.receiptsprocessor.service;

import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.rules.common.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptsProcessorServiceTest {
    private ReceiptsProcessorService service;

    @Mock
    private Rule rule1;

    @Mock
    private Rule rule2;

    @Mock
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        List<Rule> rules = Arrays.asList(rule1, rule2);
        service = new ReceiptsProcessorService(rules);
    }

    @Test
    void processReceiptShouldReturnValidId() {
        // setup

        // mock
        when(rule1.isEligible(anyString(), any(Receipt.class))).thenReturn(true);
        when(rule2.isEligible(anyString(), any(Receipt.class))).thenReturn(true);
        when(rule1.calculatePoints(anyString(), any(Receipt.class))).thenReturn(10L);
        when(rule2.calculatePoints(anyString(), any(Receipt.class))).thenReturn(20L);

        // act
        Optional<String> result = service.processReceipt(receipt);

        // verify
        assertTrue(result.isPresent());
        assertDoesNotThrow(() -> UUID.fromString(result.get()));
    }

    @Test
    void processReceiptShouldCalculateAndStorePoints() {
        // setup

        // mock
        when(rule1.isEligible(anyString(), any(Receipt.class))).thenReturn(true);
        when(rule2.isEligible(anyString(), any(Receipt.class))).thenReturn(true);
        when(rule1.calculatePoints(anyString(), any(Receipt.class))).thenReturn(10L);
        when(rule2.calculatePoints(anyString(), any(Receipt.class))).thenReturn(20L);

        // act 1
        Optional<String> result = service.processReceipt(receipt);

        // verify 1
        assertTrue(result.isPresent());

        // act 2
        Optional<Long> points = service.getPoints(result.get());

        // verify 2
        assertTrue(points.isPresent());
        assertEquals(30L, points.get());
    }

    @Test
    void processReceiptShouldHandleNoEligibleRules() {
        // setup
        
        // mock
        when(rule1.isEligible(anyString(), any(Receipt.class))).thenReturn(false);
        when(rule2.isEligible(anyString(), any(Receipt.class))).thenReturn(false);

        // act 1
        Optional<String> result = service.processReceipt(receipt);

        // verify 1
        assertTrue(result.isPresent());

        // act 2
        Optional<Long> points = service.getPoints(result.get());

        // verify 2
        assertTrue(points.isPresent());
        assertEquals(0L, points.get());
    }
} 