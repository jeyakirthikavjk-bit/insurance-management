package com.insurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "claims")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_number", unique = true, nullable = false)
    private String claimNumber;

    @NotBlank(message = "Claim title is required")
    private String name;

    @Column(nullable = false)
    private String description;

    @NotNull(message = "Claim amount is required")
    @Column(name = "claim_amount", nullable = false)
    private Double claimAmount;

    @Column(name = "approved_amount")
    private Double approvedAmount;

    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(name = "filed_date")
    private LocalDate filedDate;

    @Column(name = "settled_date")
    private LocalDate settledDate;

    @Enumerated(EnumType.STRING)
    private ClaimStatus status = ClaimStatus.FILED;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "incident_location")
    private String incidentLocation;

    @Column(name = "police_report_number")
    private String policeReportNumber;

    @Column(name = "investigator_notes")
    private String investigatorNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    @JsonIgnoreProperties({"claims", "payments", "documents", "customer", "agent", "hibernateLazyInitializer"})
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties({"policies", "documents", "agent", "hibernateLazyInitializer"})
    private Customer customer;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Document> documents;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public enum ClaimStatus { FILED, UNDER_REVIEW, APPROVED, REJECTED, SETTLED, CLOSED }
}
