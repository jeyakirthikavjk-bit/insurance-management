package com.insurance.service;

import com.insurance.entity.Agent;
import com.insurance.exception.DuplicateResourceException;
import com.insurance.exception.ResourceNotFoundException;
import com.insurance.repository.AgentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);

    @Autowired private AgentRepository agentRepository;

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public Agent getAgentById(Long id) {
        return agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", id));
    }

    public Agent createAgent(Agent agent) {
        if (agentRepository.existsByEmail(agent.getEmail())) {
            throw new DuplicateResourceException("Agent with email already exists: " + agent.getEmail());
        }
        agent.setAgentCode(generateAgentCode());
        logger.info("Creating agent: {}", agent.getName());
        return agentRepository.save(agent);
    }

    public Agent updateAgent(Long id, Agent updated) {
        Agent existing = getAgentById(id);
        existing.setName(updated.getName());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setSpecialization(updated.getSpecialization());
        existing.setCommissionRate(updated.getCommissionRate());
        existing.setStatus(updated.getStatus());
        return agentRepository.save(existing);
    }

    public void deleteAgent(Long id) {
        Agent agent = getAgentById(id);
        agent.setStatus(Agent.AgentStatus.INACTIVE);
        agentRepository.save(agent);
    }

    public List<Agent> searchAgents(String keyword) {
        return agentRepository.searchAgents(keyword);
    }

    public long countAgents() {
        return agentRepository.count();
    }

    private String generateAgentCode() {
        String prefix = "AGT";
        int rand = new Random().nextInt(90000) + 10000;
        return prefix + rand;
    }
}
