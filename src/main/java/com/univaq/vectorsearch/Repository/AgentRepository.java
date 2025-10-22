package com.univaq.vectorsearch.Repository;

import com.univaq.vectorsearch.Model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    // Spring automatically get save(), findAll(), ecc....
}