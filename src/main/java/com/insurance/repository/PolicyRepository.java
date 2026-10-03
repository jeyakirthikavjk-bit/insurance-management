package com.insurance.repository;

import com.insurance.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Optional<Policy> findByPolicyNumber(String policyNumber);
    List<Policy> findByCustomerId(Long customerId);
    List<Policy> findByAgentId(Long agentId);
    List<Policy> findByStatus(Policy.PolicyStatus status);
    List<Policy> findByPolicyTypeLabel(String policyTypeLabel);

    @Query("SELECT p FROM Policy p WHERE p.policyNumber LIKE %?1% OR p.name LIKE %?1%")
    List<Policy> searchPolicies(String keyword);

    @Query("SELECT COUNT(p) FROM Policy p WHERE p.status = 'ACTIVE'")
    Long countActivePolicies();

    @Query("SELECT SUM(p.premiumAmount) FROM Policy p WHERE p.status = 'ACTIVE'")
    Double totalPremiumRevenue();
}
