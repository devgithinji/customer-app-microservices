package com.densoft.fraud.service;

import lombok.AllArgsConstructor;
import com.densoft.fraud.model.FraudCheckHistory;
import com.densoft.fraud.repo.FraudCheckHistoryRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FraudCheckService {

    private final FraudCheckHistoryRepo fraudCheckHistoryRepo;

    public boolean isFraudulentCustomer(Integer customerId) {
        fraudCheckHistoryRepo.save(FraudCheckHistory.builder()
                .customerId(customerId)
                .isFraudster(false)
                .createdAt(LocalDateTime.now())
                .build());
        return false;
    }
}
