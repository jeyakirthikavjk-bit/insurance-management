package com.insurance.controller;

import com.insurance.entity.*;
import com.insurance.service.PolicyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
public class PolicyController {

    private static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    @Autowired private PolicyService policyService;

    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getPolicyById(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getPolicyById(id));
    }

    @GetMapping("/number/{policyNumber}")
    public ResponseEntity<Policy> getByPolicyNumber(@PathVariable String policyNumber) {
        return ResponseEntity.ok(policyService.getPolicyByNumber(policyNumber));
    }

    // Single unified endpoint - accepts any policy subtype via @JsonTypeInfo on Policy
    @PostMapping
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy policy,
                                               @RequestParam Long customerId,
                                               @RequestParam(required = false) Long agentId) {
        Policy created = policyService.createPolicy(policy, customerId, agentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Keep subtype-specific endpoints for backward compat and Postman
    @PostMapping("/life")
    public ResponseEntity<Policy> createLifePolicy(@RequestBody LifePolicy policy,
                                                    @RequestParam Long customerId,
                                                    @RequestParam(required = false) Long agentId) {
        Policy created = policyService.createPolicy(policy, customerId, agentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/health")
    public ResponseEntity<Policy> createHealthPolicy(@RequestBody HealthPolicy policy,
                                                      @RequestParam Long customerId,
                                                      @RequestParam(required = false) Long agentId) {
        Policy created = policyService.createPolicy(policy, customerId, agentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/vehicle")
    public ResponseEntity<Policy> createVehiclePolicy(@RequestBody VehiclePolicy policy,
                                                       @RequestParam Long customerId,
                                                       @RequestParam(required = false) Long agentId) {
        Policy created = policyService.createPolicy(policy, customerId, agentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Policy> updateStatus(@PathVariable Long id,
                                                @RequestParam Policy.PolicyStatus status) {
        return ResponseEntity.ok(policyService.updatePolicyStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> cancelPolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.ok(Map.of("message", "Policy cancelled successfully"));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Policy>> getPoliciesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(policyService.getPoliciesByCustomer(customerId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Policy>> searchPolicies(@RequestParam String keyword) {
        return ResponseEntity.ok(policyService.searchPolicies(keyword));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Policy>> getPoliciesByStatus(@PathVariable Policy.PolicyStatus status) {
        return ResponseEntity.ok(policyService.getPoliciesByStatus(status));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getPolicyStats() {
        return ResponseEntity.ok(Map.of(
                "activePolicies", policyService.countActivePolicies(),
                "totalPremiumRevenue", policyService.totalPremiumRevenue()
        ));
    }
}
