package com.insurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("LIFE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LifePolicy extends Policy {

    @Column(name = "beneficiary_name")
    private String beneficiaryName;

    @Column(name = "beneficiary_relation")
    private String beneficiaryRelation;

    @Column(name = "death_benefit")
    private Double deathBenefit;

    @Column(name = "maturity_benefit")
    private Double maturityBenefit;

    @Column(name = "surrender_value")
    private Double surrenderValue;

    @Column(name = "policy_term_years")
    private Integer policyTermYears;

    @Column(name = "sum_assured")
    private Double sumAssured;

    @Column(name = "rider_critical_illness")
    private Boolean riderCriticalIllness = false;

    @Column(name = "rider_accidental_death")
    private Boolean riderAccidentalDeath = false;

    @Column(name = "smoker")
    private Boolean smoker = false;

    @Override
    public Double calculatePremium() {
        double base = (getSumAssured() != null ? getSumAssured() : getCoverageAmount()) * 0.02;
        if (Boolean.TRUE.equals(smoker)) base *= 1.25;
        if (Boolean.TRUE.equals(riderCriticalIllness)) base += 500;
        if (Boolean.TRUE.equals(riderAccidentalDeath)) base += 300;
        return base;
    }

    @Override
    public String getPolicyDetails() {
        return String.format("Life Policy | Sum Assured: %.2f | Beneficiary: %s | Term: %d years",
                sumAssured != null ? sumAssured : 0, beneficiaryName, policyTermYears != null ? policyTermYears : 0);
    }
}
