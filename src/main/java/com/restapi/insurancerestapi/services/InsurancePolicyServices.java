package com.restapi.insurancerestapi.services;

import com.restapi.insurancerestapi.entities.InsurancePolicy;
import com.restapi.insurancerestapi.exceptions.InsurancePolicyNotFoundException;
import com.restapi.insurancerestapi.repositories.InsurancePolicyRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class InsurancePolicyServices {
    private final InsurancePolicyRepository insurancePolicyRepository;

    @Autowired
    public InsurancePolicyServices(InsurancePolicyRepository insurancePolicyRepository) {
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    public List<InsurancePolicy> findAllPolicies() {
        log.info("findAllPolicies() - called");
        return insurancePolicyRepository.findAll();
    }

    public InsurancePolicy findSpecificPolicy(Integer id) {
        log.info("findSpecificPolicy() - called");
        if (!(insurancePolicyRepository.existsById(id))) {
            throw new InsurancePolicyNotFoundException("Policy information is not available - Policy Number: " + id);
        }

        return insurancePolicyRepository.findById(id).get();
    }

    @Transactional
    public ResponseEntity<String> createNewPolicy(InsurancePolicy insurancePolicy) {
        log.info("createNewPolicyCalled() - called");
        if (insurancePolicyRepository.existsById(Math.toIntExact(insurancePolicy.getPolicyNumber()))) {
            throw new InsurancePolicyNotFoundException("Policy Number: " + insurancePolicy.getPolicyNumber() + " already Present - Insert new Policy Number");
        }

        insurancePolicyRepository.save(insurancePolicy);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(insurancePolicy.getPolicyNumber())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Transactional
    public ResponseEntity<String> updatePolicy(Integer id, InsurancePolicy insurancePolicy) {
        log.info("updatePolicy() - called");
        if (!(insurancePolicyRepository.existsById(id))) {
            throw new InsurancePolicyNotFoundException("Policy Number : " + id + " doesn't exist - Update Failed");
        }

        InsurancePolicy curInsurancePolicy = insurancePolicyRepository.findById(id).get();
        curInsurancePolicy.setClientId(insurancePolicy.getClientId());
        curInsurancePolicy.setPolicyNumber(insurancePolicy.getPolicyNumber());
        curInsurancePolicy.setPolicyType(insurancePolicy.getPolicyType());
        curInsurancePolicy.setCoverageAmount(insurancePolicy.getCoverageAmount());
        curInsurancePolicy.setPremium(insurancePolicy.getPremium());
        curInsurancePolicy.setStartDate(insurancePolicy.getStartDate());
        curInsurancePolicy.setEndDate(insurancePolicy.getEndDate());
        insurancePolicyRepository.save(curInsurancePolicy);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(insurancePolicy.getPolicyNumber())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Transactional
    public ResponseEntity<String> deletePolicy(Integer id) {
        log.info("deletePolicy() - called");
        if (!(insurancePolicyRepository.existsById(id))) {
            throw new InsurancePolicyNotFoundException("Policy ID : " + id + " doesn't exist - Deletion Failed");
        }

        insurancePolicyRepository.deleteById(id);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
