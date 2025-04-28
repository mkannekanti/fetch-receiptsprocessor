package org.fetch.receiptsprocessor.controller;

import lombok.RequiredArgsConstructor;
import org.fetch.receiptsprocessor.openapi.api.ReceiptsApiDelegate;
import org.fetch.receiptsprocessor.openapi.model.GetReceiptsPoints200Response;
import org.fetch.receiptsprocessor.openapi.model.ProcessReceipts200Response;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.fetch.receiptsprocessor.service.ReceiptsProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReceiptsApiDelegateImpl implements ReceiptsApiDelegate {
    @Autowired
    ReceiptsProcessorService receiptsProcessorService;

    @Override
    public ResponseEntity<GetReceiptsPoints200Response> getReceiptsPoints(String id) {
        try {
            Optional<Long> points = receiptsProcessorService.getPoints(id);

            if (points.isPresent()) {
                GetReceiptsPoints200Response response = new GetReceiptsPoints200Response();
                response.points(points.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ProcessReceipts200Response> processReceipts(Receipt receipt) {
        try {
            Optional<String> id = receiptsProcessorService.processReceipt(receipt);

            if (id.isPresent()) {
                ProcessReceipts200Response response = new ProcessReceipts200Response(id.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
