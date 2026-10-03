package com.insurance.service;

import com.insurance.entity.*;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class PolicyService {

    private static final Logger logger = LoggerFactory.getLogger(PolicyService.class);

    @Autowired private PolicyRepository policyRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private AgentRepository agentRepository;

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public Policy getPolicyById(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", id));
    }

    public Policy getPolicyByNumber(String policyNumber) {
        return policyRepository.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Policy not found: " + policyNumber));
    }

    public Policy createPolicy(Policy policy, Long customerId, Long agentId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));
        policy.setCustomer(customer);

        if (agentId != null) {
            Agent agent = agentRepository.findById(agentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Agent", agentId));
            policy.setAgent(agent);
        }

        // Set type label
        String typeLabel = switch (policy.getClass().getSimpleName()) {
            case "LifePolicy"    -> "LIFE";
            case "HealthPolicy"  -> "HEALTH";
            case "VehiclePolicy" -> "VEHICLE";
            default              -> "GENERAL";
        };
        policy.setPolicyTypeLabel(typeLabel);
        policy.setPolicyNumber(generatePolicyNumber(typeLabel));
        policy.setPremiumAmount(policy.calculatePremium());
        policy.setStatus(Policy.PolicyStatus.ACTIVE);

        logger.info("Creating {} policy for customer: {}", typeLabel, customer.getName());
        return policyRepository.save(policy);
    }

    public Policy updatePolicyStatus(Long id, Policy.PolicyStatus status) {
        Policy policy = getPolicyById(id);
        policy.setStatus(status);
        return policyRepository.save(policy);
    }

    public void deletePolicy(Long id) {
        Policy policy = getPolicyById(id);
        policy.setStatus(Policy.PolicyStatus.CANCELLED);
        policyRepository.save(policy);
    }

    public List<Policy> getPoliciesByCustomer(Long customerId) {
        return policyRepository.findByCustomerId(customerId);
    }

    public List<Policy> searchPolicies(String keyword) {
        return policyRepository.searchPolicies(keyword);
    }

    public List<Policy> getPoliciesByStatus(Policy.PolicyStatus status) {
        return policyRepository.findByStatus(status);
    }

    public Long countActivePolicies() {
        return policyRepository.countActivePolicies();
    }

    public Double totalPremiumRevenue() {
        Double total = policyRepository.totalPremiumRevenue();
        return total != null ? total : 0.0;
    }

    private String generatePolicyNumber(String typeLabel) {
        String prefix = switch (typeLabel) {
            case "LIFE"    -> "LIF";
            case "HEALTH"  -> "HLT";
            case "VEHICLE" -> "VEH";
            default        -> "POL";
        };
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int rand = new Random().nextInt(9000) + 1000;
        return prefix + "-" + timestamp + "-" + rand;
    }
}
