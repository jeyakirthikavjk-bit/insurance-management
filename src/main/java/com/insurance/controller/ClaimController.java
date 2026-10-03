package com.insurance.controller;

import com.insurance.entity.Claim;
import com.insurance.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimController {

    @Autowired private ClaimService claimService;

    @GetMapping
    public ResponseEntity<List<Claim>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Claim> getClaimById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    // HTML form calls: POST /api/claims/file?policyId=X  with JSON body
    @PostMapping("/file")
    public ResponseEntity<Claim> fileClaim(@RequestBody Claim claim,
                                            @RequestParam Long policyId) {
        Claim filed = claimService.fileClaim(claim, policyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(filed);
    }

    // Also support POST /api/claims with policyId in body or param
    @PostMapping
    public ResponseEntity<Claim> createClaim(@RequestBody Claim claim,
                                              @RequestParam(required = false) Long policyId) {
        if (policyId == null && claim.getPolicy() != null) {
            policyId = claim.getPolicy().getId();
        }
        Claim filed = claimService.fileClaim(claim, policyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(filed);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Claim> approveClaim(@PathVariable Long id,
                                               @RequestParam Double approvedAmount) {
        return ResponseEntity.ok(claimService.approveClaim(id, approvedAmount));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<Claim> rejectClaim(@PathVariable Long id,
                                              @RequestParam String reason) {
        return ResponseEntity.ok(claimService.rejectClaim(id, reason));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Claim> updateStatus(@PathVariable Long id,
                                               @RequestParam Claim.ClaimStatus status,
                                               @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(claimService.updateClaimStatus(id, status, reason));
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<Claim>> getByPolicy(@PathVariable Long policyId) {
        return ResponseEntity.ok(claimService.getClaimsByPolicy(policyId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Claim>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(claimService.getClaimsByCustomer(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Claim>> getByStatus(@PathVariable Claim.ClaimStatus status) {
        return ResponseEntity.ok(claimService.getClaimsByStatus(status));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getClaimStats() {
        return ResponseEntity.ok(Map.of(
                "pendingClaims", claimService.countPendingClaims(),
                "totalClaimsPaid", claimService.totalClaimsPaid()
        ));
    }
}
