package com.insurance.repository;

import com.insurance.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByNationalId(String nationalId);
    List<Customer> findByStatus(Customer.CustomerStatus status);
    List<Customer> findByAgentId(Long agentId);

    @Query("SELECT c FROM Customer c WHERE c.name LIKE %?1% OR c.email LIKE %?1% OR c.phone LIKE %?1%")
    List<Customer> searchCustomers(String keyword);

    boolean existsByEmail(String email);
}
