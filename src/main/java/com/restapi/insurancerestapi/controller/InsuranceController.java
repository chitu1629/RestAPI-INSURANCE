package com.restapi.insurancerestapi.controller;

import com.restapi.insurancerestapi.entities.InsurancePolicy;
import com.restapi.insurancerestapi.services.InsurancePolicyServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InsuranceController {

    private final InsurancePolicyServices insurancePolicyServices;

    @Autowired
    public InsuranceController(InsurancePolicyServices insurancePolicyServices) {
        this.insurancePolicyServices = insurancePolicyServices;
    }

    @GetMapping("/api/policies")
    public List<InsurancePolicy> fetchAllPolicies() {
        return insurancePolicyServices.findAllPolicies();
    }

    @GetMapping("/api/policies/{id}")
    public InsurancePolicy fetchPolicy(@PathVariable Integer id) {
        return insurancePolicyServices.findSpecificPolicy(id);
    }

    @PostMapping("/api/policies")
    public ResponseEntity<String> createNewPolicy(@Valid @RequestBody InsurancePolicy insurancePolicy) {
        return insurancePolicyServices.createNewPolicy(insurancePolicy);
    }

    @PutMapping("/api/policies/{id}")
    public ResponseEntity<String> updatePolicy(@PathVariable Integer id, @Valid @RequestBody InsurancePolicy insurancePolicy) {
        return insurancePolicyServices.updatePolicy(id, insurancePolicy);
    }

    @DeleteMapping("/api/policies/{id}")
    public ResponseEntity<String> deletePolicy(@PathVariable Integer id) {
        return insurancePolicyServices.deletePolicy(id);
    }
}
