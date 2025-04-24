package org.fetch.receiptsprocessor.controller;

import lombok.RequiredArgsConstructor;
import org.fetch.receiptsprocessor.openapi.api.ReceiptsApiDelegate;
import org.fetch.receiptsprocessor.openapi.model.GetReceiptsPoints200Response;
import org.fetch.receiptsprocessor.openapi.model.ProcessReceipts200Response;
import org.fetch.receiptsprocessor.openapi.model.Receipt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptsApiDelegateImpl implements ReceiptsApiDelegate {

    @Override
    public ResponseEntity<GetReceiptsPoints200Response> getReceiptsPoints(String id) {
        GetReceiptsPoints200Response response = new GetReceiptsPoints200Response();
        response.points(100L);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ProcessReceipts200Response> processReceipts(Receipt receipt) {
        ProcessReceipts200Response response = new ProcessReceipts200Response("adb6b560-0eef-42bc-9d16-df48f30e89b2");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
