package com.restapi.insurancerestapi.controller;

import com.restapi.insurancerestapi.entities.Claim;
import com.restapi.insurancerestapi.services.ClaimServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClaimController {

    private final ClaimServices claimServices;

    @Autowired
    public ClaimController(ClaimServices claimServices) {
        this.claimServices = claimServices;
    }

    @GetMapping("/api/claims")
    public List<Claim> fetchAllClaims() {
        return claimServices.findAllClaims();
    }

    @GetMapping("/api/claims/{id}")
    public Claim fetchClaim(@PathVariable Integer id) {
        return claimServices.findSpecificClaim(id);
    }

    @PostMapping("/api/claims")
    public ResponseEntity<String> createNewClaim(@Valid @RequestBody Claim claim) {
        return claimServices.createNewClaim(claim);
    }

    @PutMapping("/api/claims/{id}")
    public ResponseEntity<String> updateClaim(@PathVariable Integer id, @Valid @RequestBody Claim claim) {
        return claimServices.updateClaim(id, claim);
    }

    @DeleteMapping("/api/claims/{id}")
    public ResponseEntity<String> deleteClaim(@PathVariable Integer id) {
        return claimServices.deleteClaim(id);
    }
}
