package com.insurance.service;

import com.insurance.entity.Claim;
import com.insurance.entity.Customer;
import com.insurance.entity.Policy;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.ClaimRepository;
import com.insurance.repository.CustomerRepository;
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

@Service
@Transactional
public class ClaimService {

    private static final Logger logger = LoggerFactory.getLogger(ClaimService.class);

    @Autowired private ClaimRepository claimRepository;
    @Autowired private PolicyRepository policyRepository;
    @Autowired private CustomerRepository customerRepository;

    public List<Claim> getAllClaims() {
        return claimRepository.findAll();
    }

    public Claim getClaimById(Long id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", id));
    }

    public Claim fileClaim(Claim claim, Long policyId) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", policyId));

        if (policy.getStatus() != Policy.PolicyStatus.ACTIVE) {
            throw new IllegalArgumentException("Claims can only be filed for ACTIVE policies");
        }

        claim.setPolicy(policy);
        claim.setCustomer(policy.getCustomer());
        claim.setClaimNumber(generateClaimNumber());
        claim.setFiledDate(LocalDate.now());
        claim.setStatus(Claim.ClaimStatus.FILED);

        logger.info("Filing claim {} for policy {}", claim.getClaimNumber(), policy.getPolicyNumber());
        return claimRepository.save(claim);
    }

    public Claim updateClaimStatus(Long id, Claim.ClaimStatus status, String reason) {
        Claim claim = getClaimById(id);
        claim.setStatus(status);

        if (status == Claim.ClaimStatus.APPROVED) {
            claim.setApprovedAmount(claim.getClaimAmount());
            logger.info("Claim {} approved for amount {}", id, claim.getClaimAmount());
        } else if (status == Claim.ClaimStatus.REJECTED) {
            claim.setRejectionReason(reason);
            logger.info("Claim {} rejected: {}", id, reason);
        } else if (status == Claim.ClaimStatus.SETTLED) {
            claim.setSettledDate(LocalDate.now());
        }

        return claimRepository.save(claim);
    }

    public Claim approveClaim(Long id, Double approvedAmount) {
        Claim claim = getClaimById(id);
        claim.setStatus(Claim.ClaimStatus.APPROVED);
        claim.setApprovedAmount(approvedAmount);
        return claimRepository.save(claim);
    }

    public Claim rejectClaim(Long id, String reason) {
        Claim claim = getClaimById(id);
        claim.setStatus(Claim.ClaimStatus.REJECTED);
        claim.setRejectionReason(reason);
        return claimRepository.save(claim);
    }

    public List<Claim> getClaimsByPolicy(Long policyId) {
        return claimRepository.findByPolicyId(policyId);
    }

    public List<Claim> getClaimsByCustomer(Long customerId) {
        return claimRepository.findByCustomerId(customerId);
    }

    public List<Claim> getClaimsByStatus(Claim.ClaimStatus status) {
        return claimRepository.findByStatus(status);
    }

    public Long countPendingClaims() {
        return claimRepository.countPendingClaims();
    }

    public Double totalClaimsPaid() {
        Double total = claimRepository.totalClaimsPaid();
        return total != null ? total : 0.0;
    }

    private String generateClaimNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        int rand = new Random().nextInt(9000) + 1000;
        return "CLM-" + timestamp + "-" + rand;
    }
}
