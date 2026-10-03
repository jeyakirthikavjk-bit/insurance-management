package com.insurance.service;

import com.insurance.entity.Payment;
import com.insurance.entity.Policy;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.PaymentRepository;
import com.insurance.repository.PolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private PolicyRepository policyRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", id));
    }

    public Payment makePayment(Payment payment, Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", policyId));

        payment.setPolicy(policy);
        payment.setCustomer(policy.getCustomer());
        payment.setTransactionId(generateTransactionId());
        payment.setPaymentDate(LocalDate.now());
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        payment.setPaymentType(Payment.PaymentType.PREMIUM);

        if (payment.getName() == null || payment.getName().isEmpty()) {
            payment.setName("Premium Payment - " + policy.getPolicyNumber());
        }

        logger.info("Payment {} processed for policy {}", payment.getTransactionId(), policy.getPolicyNumber());
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByPolicy(Long policyId) {
        return paymentRepository.findByPolicyId(policyId);
    }

    public List<Payment> getPaymentsByCustomer(Long customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }

    public Double totalPaymentsReceived() {
        Double total = paymentRepository.totalPaymentsReceived();
        return total != null ? total : 0.0;
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
