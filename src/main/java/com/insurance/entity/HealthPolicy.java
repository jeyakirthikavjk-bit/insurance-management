package com.insurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("HEALTH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HealthPolicy extends Policy {

    @Column(name = "hospital_daily_benefit")
    private Double hospitalDailyBenefit;

    @Column(name = "max_hospitalization_days")
    private Integer maxHospitalizationDays;

    @Column(name = "deductible_amount")
    private Double deductibleAmount;

    @Column(name = "copay_percentage")
    private Double copayPercentage;

    @Column(name = "covers_dental")
    private Boolean coversDental = false;

    @Column(name = "covers_vision")
    private Boolean coversVision = false;

    @Column(name = "covers_maternity")
    private Boolean coversMaternity = false;

    @Column(name = "pre_existing_conditions")
    private String preExistingConditions;

    @Column(name = "network_hospitals")
    private String networkHospitals;

    @Column(name = "family_floater")
    private Boolean familyFloater = false;

    @Column(name = "family_members_covered")
    private Integer familyMembersCovered = 1;

    @Override
    public Double calculatePremium() {
        double base = getCoverageAmount() * 0.015;
        if (Boolean.TRUE.equals(coversDental)) base += 200;
        if (Boolean.TRUE.equals(coversVision)) base += 150;
        if (Boolean.TRUE.equals(coversMaternity)) base += 400;
        if (Boolean.TRUE.equals(familyFloater)) base *= (1 + (familyMembersCovered != null ? familyMembersCovered : 1) * 0.1);
        return base;
    }

    @Override
    public String getPolicyDetails() {
        return String.format("Health Policy | Coverage: %.2f | Daily Benefit: %.2f | Dental: %s | Vision: %s",
                getCoverageAmount(), hospitalDailyBenefit != null ? hospitalDailyBenefit : 0, coversDental, coversVision);
    }
}
