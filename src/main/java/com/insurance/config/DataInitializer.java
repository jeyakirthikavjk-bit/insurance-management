package com.insurance.config;

import com.insurance.entity.*;
import com.insurance.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired private AgentRepository agentRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PolicyRepository policyRepository;
    @Autowired private ClaimRepository claimRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (agentRepository.count() > 0) {
            logger.info("Demo data already exists - skipping initialization.");
            return;
        }
        logger.info("Seeding demo data...");
        seedUsers();
        Agent a1 = seedAgents();
        Customer c1 = seedCustomers(a1);
        Policy p1 = seedPolicies(c1, a1);
        seedClaims(p1, c1);
        seedPayments(p1, c1);
        logger.info("Demo data seeded successfully!");
    }

    private void seedUsers() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setFullName("System Administrator");
        admin.setEmail("admin@insuresystem.com");
        admin.setPhone("+1-555-0001");
        admin.setRole(User.UserRole.ADMIN);
        admin.setDepartment("IT");
        admin.setActive(true);
        userRepository.save(admin);
    }

    private Agent seedAgents() {
        Agent a1 = new Agent();
        a1.setName("Sarah Johnson");
        a1.setEmail("sarah.johnson@insure.com");
        a1.setPhone("+1-555-0201");
        a1.setAgentCode("AGT10001");
        a1.setLicenseNumber("LIC-2024-001");
        a1.setLicenseExpiry(LocalDate.of(2026, 12, 31));
        a1.setSpecialization("Life & Health Insurance");
        a1.setCommissionRate(6.5);
        a1.setJoiningDate(LocalDate.of(2020, 3, 15));
        a1.setAddress("456 Commerce Ave");
        a1.setCity("Los Angeles");
        a1.setStatus(Agent.AgentStatus.ACTIVE);
        a1.setTotalPoliciesSold(42);
        a1.setTotalCommissionEarned(18500.0);
        agentRepository.save(a1);

        Agent a2 = new Agent();
        a2.setName("Michael Chen");
        a2.setEmail("michael.chen@insure.com");
        a2.setPhone("+1-555-0202");
        a2.setAgentCode("AGT10002");
        a2.setLicenseNumber("LIC-2024-002");
        a2.setLicenseExpiry(LocalDate.of(2025, 6, 30));
        a2.setSpecialization("Vehicle & Property Insurance");
        a2.setCommissionRate(5.0);
        a2.setJoiningDate(LocalDate.of(2021, 7, 1));
        a2.setCity("Chicago");
        a2.setStatus(Agent.AgentStatus.ACTIVE);
        a2.setTotalPoliciesSold(28);
        a2.setTotalCommissionEarned(11200.0);
        agentRepository.save(a2);

        return a1;
    }

    private Customer seedCustomers(Agent agent) {
        Customer c1 = new Customer();
        c1.setName("John Smith");
        c1.setEmail("john.smith@email.com");
        c1.setPhone("+1-555-1001");
        c1.setDateOfBirth(LocalDate.of(1985, 6, 15));
        c1.setGender(Customer.Gender.MALE);
        c1.setNationalId("SSN-123-45-6789");
        c1.setAddress("123 Maple Street");
        c1.setCity("New York");
        c1.setState("NY");
        c1.setZipCode("10001");
        c1.setOccupation("Software Engineer");
        c1.setAnnualIncome(95000.0);
        c1.setStatus(Customer.CustomerStatus.ACTIVE);
        c1.setAgent(agent);
        customerRepository.save(c1);

        Customer c2 = new Customer();
        c2.setName("Emily Davis");
        c2.setEmail("emily.davis@email.com");
        c2.setPhone("+1-555-1002");
        c2.setDateOfBirth(LocalDate.of(1990, 3, 22));
        c2.setGender(Customer.Gender.FEMALE);
        c2.setNationalId("SSN-987-65-4321");
        c2.setAddress("789 Oak Lane");
        c2.setCity("Chicago");
        c2.setState("IL");
        c2.setZipCode("60601");
        c2.setOccupation("Marketing Manager");
        c2.setAnnualIncome(72000.0);
        c2.setStatus(Customer.CustomerStatus.ACTIVE);
        c2.setAgent(agent);
        customerRepository.save(c2);

        Customer c3 = new Customer();
        c3.setName("Robert Wilson");
        c3.setEmail("robert.wilson@email.com");
        c3.setPhone("+1-555-1003");
        c3.setDateOfBirth(LocalDate.of(1978, 11, 8));
        c3.setGender(Customer.Gender.MALE);
        c3.setAddress("321 Pine Road");
        c3.setCity("Houston");
        c3.setState("TX");
        c3.setZipCode("77001");
        c3.setOccupation("Business Owner");
        c3.setAnnualIncome(150000.0);
        c3.setStatus(Customer.CustomerStatus.ACTIVE);
        customerRepository.save(c3);

        return c1;
    }

    private Policy seedPolicies(Customer customer, Agent agent) {
        // Life Policy
        LifePolicy lp = new LifePolicy();
        lp.setName("Premium Life Shield");
        lp.setPolicyNumber("LIF-20240101-1001");
        lp.setPolicyTypeLabel("LIFE");
        lp.setCoverageAmount(500000.0);
        lp.setSumAssured(500000.0);
        lp.setPolicyTermYears(20);
        lp.setBeneficiaryName("Mary Smith");
        lp.setBeneficiaryRelation("Spouse");
        lp.setDeathBenefit(500000.0);
        lp.setMaturityBenefit(200000.0);
        lp.setSmoker(false);
        lp.setRiderCriticalIllness(true);
        lp.setRiderAccidentalDeath(false);
        lp.setStartDate(LocalDate.of(2024, 1, 1));
        lp.setEndDate(LocalDate.of(2044, 1, 1));
        lp.setPaymentFrequency(Policy.PaymentFrequency.MONTHLY);
        lp.setPremiumAmount(lp.calculatePremium());
        lp.setStatus(Policy.PolicyStatus.ACTIVE);
        lp.setCustomer(customer);
        lp.setAgent(agent);
        policyRepository.save(lp);

        // Health Policy
        HealthPolicy hp = new HealthPolicy();
        hp.setName("Family Health Plus");
        hp.setPolicyNumber("HLT-20240115-2001");
        hp.setPolicyTypeLabel("HEALTH");
        hp.setCoverageAmount(100000.0);
        hp.setHospitalDailyBenefit(500.0);
        hp.setMaxHospitalizationDays(60);
        hp.setDeductibleAmount(1000.0);
        hp.setCopayPercentage(20.0);
        hp.setCoversDental(true);
        hp.setCoversVision(true);
        hp.setCoversMaternity(false);
        hp.setFamilyFloater(true);
        hp.setFamilyMembersCovered(3);
        hp.setNetworkHospitals("St. Mary's, City General, Metro Health");
        hp.setStartDate(LocalDate.of(2024, 1, 15));
        hp.setEndDate(LocalDate.of(2025, 1, 15));
        hp.setPaymentFrequency(Policy.PaymentFrequency.ANNUALLY);
        hp.setPremiumAmount(hp.calculatePremium());
        hp.setStatus(Policy.PolicyStatus.ACTIVE);
        hp.setCustomer(customer);
        hp.setAgent(agent);
        policyRepository.save(hp);

        // Vehicle Policy
        VehiclePolicy vp = new VehiclePolicy();
        vp.setName("Auto Comprehensive Cover");
        vp.setPolicyNumber("VEH-20240201-3001");
        vp.setPolicyTypeLabel("VEHICLE");
        vp.setCoverageAmount(30000.0);
        vp.setVehicleMake("Toyota");
        vp.setVehicleModel("Camry");
        vp.setVehicleYear(2022);
        vp.setVehicleNumber("NYC-4521-X");
        vp.setVehicleVin("1HGCM82633A123456");
        vp.setVehicleValue(28000.0);
        vp.setCoverageType(VehiclePolicy.CoverageType.COMPREHENSIVE);
        vp.setDeductibleAmount(500.0);
        vp.setRoadsideAssistance(true);
        vp.setRentalReimbursement(true);
        vp.setAntiTheftDevice(true);
        vp.setStartDate(LocalDate.of(2024, 2, 1));
        vp.setEndDate(LocalDate.of(2025, 2, 1));
        vp.setPaymentFrequency(Policy.PaymentFrequency.MONTHLY);
        vp.setPremiumAmount(vp.calculatePremium());
        vp.setStatus(Policy.PolicyStatus.ACTIVE);
        vp.setCustomer(customer);
        policyRepository.save(vp);

        return lp;
    }

    private void seedClaims(Policy policy, Customer customer) {
        Claim c1 = new Claim();
        c1.setClaimNumber("CLM-202401010001-1001");
        c1.setName("Medical Hospitalization Claim");
        c1.setDescription("Patient was hospitalized for 5 days due to appendicitis surgery.");
        c1.setClaimAmount(8500.0);
        c1.setApprovedAmount(7800.0);
        c1.setIncidentDate(LocalDate.of(2024, 3, 10));
        c1.setFiledDate(LocalDate.of(2024, 3, 15));
        c1.setStatus(Claim.ClaimStatus.APPROVED);
        c1.setIncidentLocation("St. Mary's Hospital, New York");
        c1.setPolicy(policy);
        c1.setCustomer(customer);
        claimRepository.save(c1);

        Claim c2 = new Claim();
        c2.setClaimNumber("CLM-202402010001-2001");
        c2.setName("Vehicle Accident Claim");
        c2.setDescription("Minor collision at intersection, front bumper and hood damage.");
        c2.setClaimAmount(3200.0);
        c2.setIncidentDate(LocalDate.of(2024, 4, 5));
        c2.setFiledDate(LocalDate.of(2024, 4, 8));
        c2.setStatus(Claim.ClaimStatus.UNDER_REVIEW);
        c2.setIncidentLocation("5th Ave & 42nd Street, New York");
        c2.setPoliceReportNumber("RPT-2024-0042");
        c2.setPolicy(policy);
        c2.setCustomer(customer);
        claimRepository.save(c2);
    }

    private void seedPayments(Policy policy, Customer customer) {
        for (int i = 1; i <= 3; i++) {
            Payment p = new Payment();
            p.setTransactionId("TXN-DEMO-" + String.format("%04d", i));
            p.setName("Monthly Premium - " + policy.getPolicyNumber());
            p.setAmount(policy.getPremiumAmount());
            p.setPaymentDate(LocalDate.of(2024, i, 1));
            p.setDueDate(LocalDate.of(2024, i, 5));
            p.setPaymentMethod(Payment.PaymentMethod.BANK_TRANSFER);
            p.setStatus(Payment.PaymentStatus.COMPLETED);
            p.setPaymentType(Payment.PaymentType.PREMIUM);
            p.setReferenceNumber("REF-2024-" + String.format("%03d", i));
            p.setBankName("Chase Bank");
            p.setPolicy(policy);
            p.setCustomer(customer);
            paymentRepository.save(p);
        }
    }
}
