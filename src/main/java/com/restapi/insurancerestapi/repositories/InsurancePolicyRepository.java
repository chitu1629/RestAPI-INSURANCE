package com.restapi.insurancerestapi.repositories;

import com.restapi.insurancerestapi.entities.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Integer> {
}

