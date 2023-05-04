package com.restapi.insurancerestapi.services;

import com.restapi.insurancerestapi.entities.Claim;
import com.restapi.insurancerestapi.exceptions.ClaimNotFoundException;
import com.restapi.insurancerestapi.repositories.ClaimRepository;
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
public class ClaimServices {

    private final ClaimRepository claimRepository;

    @Autowired
    public ClaimServices(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public List<Claim> findAllClaims() {
        log.info("findAllClaims() - called");
        return claimRepository.findAll();
    }

    public Claim findSpecificClaim(Integer id) {
        log.info("findSpecificClaim() - called");
        if (!(claimRepository.existsById(id))) {
            throw new ClaimNotFoundException("Claim information is not available - Claim Number: " + id);
        }
        return claimRepository.findById(id).get();
    }

    @Transactional
    public ResponseEntity<String> createNewClaim(Claim claim) {
        log.info("createNewClaim() - called");
        if (claimRepository.existsById(Math.toIntExact(claim.getClaimNumber()))) {
            throw new ClaimNotFoundException("Claim Number: " + claim.getClaimNumber() + " is already Present - Insert new Claim Number");
        }

        claimRepository.save(claim);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(claim.getClaimNumber())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Transactional
    public ResponseEntity<String> updateClaim(Integer id, Claim claim) {
        log.info("updateClaim() - called");
        if (!(claimRepository.existsById(id))) {
            throw new ClaimNotFoundException("Claim Number : " + id + " doesn't exist - Update Failed");
        }

        Claim curClaim = claimRepository.findById(id).get();
        curClaim.setClaimNumber(claim.getClaimNumber());
        curClaim.setPolicyNumber(claim.getPolicyNumber());
        curClaim.setClientId(claim.getClientId());
        curClaim.setDescription(claim.getDescription());
        curClaim.setClaimDate(claim.getClaimDate());
        curClaim.setClaimStatus(claim.getClaimStatus());
        claimRepository.save(curClaim);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(claim.getClaimNumber())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Transactional
    public ResponseEntity<String> deleteClaim(Integer id) {
        log.info("deleteClaim() - called");
        if (!(claimRepository.existsById(id))) {
            throw new ClaimNotFoundException("Claim Number : " + id + " doesn't exist - Deletion Failed");
        }
        claimRepository.deleteById(id);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
