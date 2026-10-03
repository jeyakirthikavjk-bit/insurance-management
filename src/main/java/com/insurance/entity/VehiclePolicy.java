package com.insurance.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("VEHICLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VehiclePolicy extends Policy {

    @Column(name = "vehicle_make")
    private String vehicleMake;

    @Column(name = "vehicle_model")
    private String vehicleModel;

    @Column(name = "vehicle_year")
    private Integer vehicleYear;

    @Column(name = "vehicle_number", unique = true)
    private String vehicleNumber;

    @Column(name = "vehicle_vin")
    private String vehicleVin;

    @Column(name = "vehicle_value")
    private Double vehicleValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_type")
    private CoverageType coverageType = CoverageType.COMPREHENSIVE;

    @Column(name = "deductible_amount")
    private Double deductibleAmount;

    @Column(name = "roadside_assistance")
    private Boolean roadsideAssistance = false;

    @Column(name = "rental_reimbursement")
    private Boolean rentalReimbursement = false;

    @Column(name = "anti_theft_device")
    private Boolean antiTheftDevice = false;

    @Override
    public Double calculatePremium() {
        double val = vehicleValue != null ? vehicleValue : getCoverageAmount();
        double base = val * 0.025;
        if (coverageType == CoverageType.COMPREHENSIVE) base *= 1.3;
        else if (coverageType == CoverageType.THIRD_PARTY) base *= 0.6;
        if (Boolean.TRUE.equals(roadsideAssistance)) base += 100;
        if (Boolean.TRUE.equals(rentalReimbursement)) base += 150;
        if (Boolean.TRUE.equals(antiTheftDevice)) base *= 0.95;
        return base;
    }

    @Override
    public String getPolicyDetails() {
        return String.format("Vehicle Policy | %s %s %d | Reg: %s | Coverage: %s | Value: %.2f",
                vehicleMake, vehicleModel, vehicleYear != null ? vehicleYear : 0,
                vehicleNumber, coverageType, vehicleValue != null ? vehicleValue : 0);
    }

    public enum CoverageType { COMPREHENSIVE, THIRD_PARTY, COLLISION, LIABILITY }
}
