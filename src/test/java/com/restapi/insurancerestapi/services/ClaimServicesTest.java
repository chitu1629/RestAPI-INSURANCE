package com.restapi.insurancerestapi.services;

import com.restapi.insurancerestapi.entities.Claim;
import com.restapi.insurancerestapi.exceptions.ClaimNotFoundException;
import com.restapi.insurancerestapi.repositories.ClaimRepository;
import com.restapi.insurancerestapi.utils.ClaimStatus;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClaimServicesTest {
    @Mock
    private ClaimRepository claimRepository;

    @InjectMocks
    private ClaimServices claimServices;

    @Test
    @Order(1)
    void test_FindAllClaims() {

        List<Claim> claims = Arrays.asList(
                new Claim(1, 1, 1, "Random Description", LocalDate.of(2980, 1, 1), ClaimStatus.APPROVED),
                new Claim(2, 2, 2, "Random Description", LocalDate.of(2980, 1, 1), ClaimStatus.REJECTED)
        );

        when(claimRepository.findAll()).thenReturn(claims);

        assertEquals(2, claimServices.findAllClaims().size());           //test success
        assertEquals(claims.get(0), claimServices.findAllClaims().get(0));       //test success
        assertEquals(claims.get(1), claimServices.findAllClaims().get(1));       //test success
    }

    @Test
    @Order(2)
    void test_FindSpecificClaim() {

        Claim claim = new Claim(1, 1, 1, "Random Description", LocalDate.of(2980, 1, 1), ClaimStatus.APPROVED);

        when(claimRepository.existsById(claim.getClaimNumber())).thenReturn(true);           //true - entry exists - doesn't throw exception
        when(claimRepository.findById(claim.getClaimNumber())).thenReturn(Optional.of(claim));

        Claim result = claimServices.findSpecificClaim(claim.getClaimNumber());

        assertEquals(claim, result);       //test success - claim retrieved successfully
    }

    @Test
    @Order(3)
    void test_FindSpecificClaimNotFound() {

        Integer claimNumber = 1;

        when(claimRepository.existsById(claimNumber)).thenReturn(false); // false - entry doesn't exist - throws Exception

        //claimServices.findSpecificClaim(claimNumber);  => throws Exception
        assertThrows(ClaimNotFoundException.class, () -> claimServices.findSpecificClaim(claimNumber)); //test success - exception asserted
    }

    @Test
    @Order(4)
    void test_CreateNewClaim() {

        Claim claim = new Claim(1, 1, 1, "Random Description", LocalDate.of(2980, 1, 1), ClaimStatus.APPROVED);

        when(claimRepository.existsById(claim.getClaimNumber())).thenReturn(false);  //false - entry doesn't exist - doesn't throw Exception
        when(claimRepository.save(claim)).thenReturn(claim);

        ResponseEntity<String> response = claimServices.createNewClaim(claim);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());     // test success - new claim created successfully

    }

    @Test
    @Order(5)
    void test_CreateNewClaimAlreadyExists() {

        Claim claim = new Claim(1, 1, 1, "Random Description", LocalDate.of(2980, 1, 1), ClaimStatus.APPROVED);

        when(claimRepository.existsById(claim.getClaimNumber())).thenReturn(true);   //true - entry exists - throws Exception

        //claimServices.createNewClaim(claim));  => throws Exception
        assertThrows(ClaimNotFoundException.class, () -> claimServices.createNewClaim(claim)); //test success - exception asserted
    }

    @Test
    @Order(6)
    void test_UpdateClaim() {

        Integer claimNumber = 1;

        Claim existingClaim = new Claim(1, 1, 1, "Random Description", LocalDate.of(2980, 1, 1), ClaimStatus.APPROVED);
        Claim updatedClaim = new Claim(1, 2, 1, "Random New Description", LocalDate.of(2980, 1, 1), ClaimStatus.APPROVED);

        when(claimRepository.existsById(claimNumber)).thenReturn(true);                   //true - entry exists - doesn't throw Exception
        when(claimRepository.findById(claimNumber)).thenReturn(Optional.of(existingClaim));
        when(claimRepository.save(existingClaim)).thenReturn(updatedClaim);

        ResponseEntity<String> response = claimServices.updateClaim(claimNumber, updatedClaim);

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); //test success - claim updated successfully

    }

    @Test
    @Order(7)
    void test_UpdateClaimNotFound() {

        Integer claimNumber = 1;

        Claim claim = new Claim(1, 1, 1, "Random Description", LocalDate.of(2980, 1, 1), ClaimStatus.APPROVED);

        when(claimRepository.existsById(claimNumber)).thenReturn(false); //false - entry doesn't exist - throw Exception

        //claimServices.updateClaim(claimNumber, claim); => throws Exception
        assertThrows(ClaimNotFoundException.class, () -> claimServices.updateClaim(claimNumber, claim)); //test success - exception asserted
    }

    @Test
    @Order(8)
    void test_DeleteClaim() {

        Integer claimNumber = 1;

        when(claimRepository.existsById(claimNumber)).thenReturn(true); //true - entry exist - doesn't throw Exception
        doNothing().when(claimRepository).deleteById(claimNumber);

        ResponseEntity<String> response = claimServices.deleteClaim(claimNumber);

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); //test success - claim deleted successfully

    }

    @Test
    @Order(9)
    void test_DeleteClaimNotFound() {

        Integer claimNumber = 1;

        when(claimRepository.existsById(claimNumber)).thenReturn(false); //false - entry doesn't exist - throws Exception

        // claimServices.deleteClaim(claimNumber); => throws Exception
        assertThrows(ClaimNotFoundException.class, () -> claimServices.deleteClaim(claimNumber)); //test success - exception asserted

    }
}