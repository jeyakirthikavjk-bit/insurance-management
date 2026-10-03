package com.insurance.repository;

import com.insurance.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    Optional<Claim> findByClaimNumber(String claimNumber);
    List<Claim> findByPolicyId(Long policyId);
    List<Claim> findByCustomerId(Long customerId);
    List<Claim> findByStatus(Claim.ClaimStatus status);

    @Query("SELECT SUM(c.approvedAmount) FROM Claim c WHERE c.status = 'SETTLED'")
    Double totalClaimsPaid();

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.status IN ('UNDER_REVIEW', 'FILED')")
    Long countPendingClaims();
}
