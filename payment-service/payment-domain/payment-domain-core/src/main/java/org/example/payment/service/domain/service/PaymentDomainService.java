package org.example.payment.service.domain.service;

import org.example.payment.service.domain.entity.CreditEntry;
import org.example.payment.service.domain.entity.CreditHistory;
import org.example.payment.service.domain.entity.Payment;
import org.example.payment.service.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment, CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories, List<String> failureMessages);

    PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry,
                                          List<CreditHistory> creditHistories, List<String> failureMessages);
}
