package com.insurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "policies")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@EntityListeners(AuditingEntityListener.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = LifePolicy.class,    name = "LIFE"),
    @JsonSubTypes.Type(value = HealthPolicy.class,  name = "HEALTH"),
    @JsonSubTypes.Type(value = VehiclePolicy.class, name = "VEHICLE")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
public abstract class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_number", unique = true, nullable = false)
    private String policyNumber;

    @NotBlank(message = "Policy name is required")
    @Column(nullable = false)
    private String name;

    // Human-readable policy type label (e.g. "LIFE", "HEALTH", "VEHICLE")
    // Stored in a separate column, NOT the discriminator
    @Column(name = "policy_type_label")
    private String policyTypeLabel;

    @NotNull(message = "Coverage amount is required")
    @Column(name = "coverage_amount", nullable = false)
    private Double coverageAmount;

    @NotNull(message = "Premium amount is required")
    @Column(name = "premium_amount", nullable = false)
    private Double premiumAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency")
    private PaymentFrequency paymentFrequency = PaymentFrequency.MONTHLY;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private PolicyStatus status = PolicyStatus.PENDING;

    private String description;

    @Column(name = "terms_conditions")
    private String termsConditions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnoreProperties({"policies", "documents", "agent", "hibernateLazyInitializer"})
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    @JsonIgnoreProperties({"policies", "customers", "hibernateLazyInitializer"})
    private Agent agent;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Claim> claims;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Payment> payments;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Document> documents;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public abstract Double calculatePremium();
    public abstract String getPolicyDetails();

    public enum PolicyStatus   { PENDING, ACTIVE, EXPIRED, CANCELLED, SUSPENDED }
    public enum PaymentFrequency { MONTHLY, QUARTERLY, SEMI_ANNUALLY, ANNUALLY }
}
