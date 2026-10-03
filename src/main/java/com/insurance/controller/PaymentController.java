package com.insurance.controller;

import com.insurance.entity.Payment;
import com.insurance.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    // HTML calls: POST /api/payments?policyId=X  with JSON body
    @PostMapping
    public ResponseEntity<Payment> makePayment(@RequestBody Payment payment,
                                                @RequestParam Long policyId) {
        Payment made = paymentService.makePayment(payment, policyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(made);
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<Payment>> getByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(paymentService.getPaymentsByPolicy(policyId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Payment>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(paymentService.getPaymentsByCustomer(customerId));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPaymentStats() {
        return ResponseEntity.ok(Map.of(
                "totalPaymentsReceived", paymentService.totalPaymentsReceived()
        ));
    }
}
