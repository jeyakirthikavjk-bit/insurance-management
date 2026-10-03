package com.insurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "agents")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Agent name is required")
    @Column(nullable = false)
    private String name;

    @Column(name = "agent_code", unique = true, nullable = false)
    private String agentCode;

    @Email(message = "Valid email required")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @Column(name = "license_expiry")
    private LocalDate licenseExpiry;

    private String address;
    private String city;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "commission_rate")
    private Double commissionRate = 5.0;

    @Column(name = "total_policies_sold")
    private Integer totalPoliciesSold = 0;

    @Column(name = "total_commission_earned")
    private Double totalCommissionEarned = 0.0;

    @Enumerated(EnumType.STRING)
    private AgentStatus status = AgentStatus.ACTIVE;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Customer> customers;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Policy> policies;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public enum AgentStatus { ACTIVE, INACTIVE, SUSPENDED, TERMINATED }
}
