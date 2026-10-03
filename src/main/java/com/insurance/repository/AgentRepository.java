package com.insurance.repository;

import com.insurance.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByAgentCode(String agentCode);
    Optional<Agent> findByEmail(String email);
    Optional<Agent> findByLicenseNumber(String licenseNumber);
    List<Agent> findByStatus(Agent.AgentStatus status);

    @Query("SELECT a FROM Agent a WHERE a.name LIKE %?1% OR a.email LIKE %?1% OR a.agentCode LIKE %?1%")
    List<Agent> searchAgents(String keyword);

    boolean existsByEmail(String email);
    boolean existsByAgentCode(String agentCode);
}
