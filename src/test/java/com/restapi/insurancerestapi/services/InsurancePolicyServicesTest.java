package com.restapi.insurancerestapi.services;

import com.restapi.insurancerestapi.entities.InsurancePolicy;
import com.restapi.insurancerestapi.exceptions.InsurancePolicyNotFoundException;
import com.restapi.insurancerestapi.repositories.InsurancePolicyRepository;
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
class InsurancePolicyServicesTest {
    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @InjectMocks
    private InsurancePolicyServices insurancePolicyServices;

    @Test
    @Order(1)
    void test_FindAllPolicies() {

        List<InsurancePolicy> insurancePolicies = Arrays.asList(
                new InsurancePolicy(1, 1, "Health Insurance", 200000L, 1000L, LocalDate.of(1999, 02, 03), LocalDate.of(2029, 02, 03)),
                new InsurancePolicy(2, 2, "Life  Insurance", 200000L, 1000L, LocalDate.of(1999, 02, 03), LocalDate.of(2029, 02, 03))
        );

        when(insurancePolicyRepository.findAll()).thenReturn(insurancePolicies);

        assertEquals(2, insurancePolicyServices.findAllPolicies().size());                      //test success
        assertEquals(insurancePolicies.get(0), insurancePolicyServices.findAllPolicies().get(0));       //test success
        assertEquals(insurancePolicies.get(1), insurancePolicyServices.findAllPolicies().get(1));       //test success
    }

    @Test
    @Order(2)
    void test_FindSpecificPolicy() {

        InsurancePolicy insurancePolicy = new InsurancePolicy(1, 1, "Health Insurance", 200000L, 1000L, LocalDate.of(1999, 02, 03), LocalDate.of(2029, 02, 03));

        when(insurancePolicyRepository.existsById(insurancePolicy.getPolicyNumber())).thenReturn(true);           //true - entry exists - doesn't throw exception
        when(insurancePolicyRepository.findById(insurancePolicy.getPolicyNumber())).thenReturn(Optional.of(insurancePolicy));

        InsurancePolicy result = insurancePolicyServices.findSpecificPolicy(insurancePolicy.getPolicyNumber());

        assertEquals(insurancePolicy, result);       //test success - insurance policy retrieved successfully
    }

    @Test
    @Order(3)
    void test_FindSpecificPolicyNotFound() {

        Integer policyNumber = 1;

        when(insurancePolicyRepository.existsById(policyNumber)).thenReturn(false); // false - entry doesn't exist - throws Exception

        //insurancePolicyServices.findSpecificPolicy(policyNumber);  => throws Exception
        assertThrows(InsurancePolicyNotFoundException.class, () -> insurancePolicyServices.findSpecificPolicy(policyNumber)); //test success - exception asserted
    }

    @Test
    @Order(4)
    void test_CreateNewPolicy() {

        InsurancePolicy insurancePolicy = new InsurancePolicy(1, 1, "Health Insurance", 200000L, 1000L, LocalDate.of(1999, 02, 03), LocalDate.of(2029, 02, 03));

        when(insurancePolicyRepository.existsById(insurancePolicy.getPolicyNumber())).thenReturn(false);  //false - entry doesn't exist - doesn't throw Exception
        when(insurancePolicyRepository.save(insurancePolicy)).thenReturn(insurancePolicy);

        ResponseEntity<String> response = insurancePolicyServices.createNewPolicy(insurancePolicy);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());     // test success - new insurance policy created successfully

    }

    @Test
    @Order(5)
    void test_CreateNewPolicyAlreadyExists() {

        InsurancePolicy insurancePolicy = new InsurancePolicy(1, 1, "Health Insurance", 200000L, 1000L, LocalDate.of(1999, 02, 03), LocalDate.of(2029, 02, 03));

        when(insurancePolicyRepository.existsById(insurancePolicy.getPolicyNumber())).thenReturn(true);   //true - entry exists - throws Exception

        //insurancePolicyServices.createNewPolicy(insurancePolicy);  => throws Exception
        assertThrows(InsurancePolicyNotFoundException.class, () -> insurancePolicyServices.createNewPolicy(insurancePolicy)); //test success - exception asserted
    }

    @Test
    @Order(6)
    void test_UpdatePolicy() {

        Integer policyNumber = 1;

        InsurancePolicy existingInsurancePolicy = new InsurancePolicy(1, 1, "Health Insurance", 200000L, 1000L, LocalDate.of(1999, 02, 03), LocalDate.of(2029, 02, 03));
        InsurancePolicy updatedInsurancePolicy = new InsurancePolicy(1, 1, "Life Insurance", 200000L, 1000L, LocalDate.of(1999, 02, 03), LocalDate.of(2029, 02, 03));

        when(insurancePolicyRepository.existsById(policyNumber)).thenReturn(true);                   //true - entry exists - doesn't throw Exception
        when(insurancePolicyRepository.findById(policyNumber)).thenReturn(Optional.of(existingInsurancePolicy));
        when(insurancePolicyRepository.save(existingInsurancePolicy)).thenReturn(updatedInsurancePolicy);

        ResponseEntity<String> response = insurancePolicyServices.updatePolicy(policyNumber, updatedInsurancePolicy);

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); //test success - insurance policy updated successfully

    }

    @Test
    @Order(7)
    void test_UpdatePolicyNotFound() {

        Integer policyNumber = 1;

        InsurancePolicy insurancePolicy = new InsurancePolicy(1, 1, "Health Insurance", 200000L, 1000L, LocalDate.of(1999, 02, 03), LocalDate.of(2029, 02, 03));

        when(insurancePolicyRepository.existsById(policyNumber)).thenReturn(false); //false - entry doesn't exist - throw Exception

        //insurancePolicyServices.updatePolicy(policyNumber, insurancePolicy); => throws Exception
        assertThrows(InsurancePolicyNotFoundException.class, () -> insurancePolicyServices.updatePolicy(policyNumber, insurancePolicy)); //test success - exception asserted
    }

    @Test
    @Order(8)
    void test_DeletePolicy() {

        Integer policyNumber = 1;

        when(insurancePolicyRepository.existsById(policyNumber)).thenReturn(true); //true - entry exist - doesn't throw Exception
        doNothing().when(insurancePolicyRepository).deleteById(policyNumber);

        ResponseEntity<String> response = insurancePolicyServices.deletePolicy(policyNumber);

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); //test success - insurance policy deleted successfully
    }

    @Test
    @Order(9)
    void test_DeletePolicyNotFound() {

        Integer policyNumber = 1;

        when(insurancePolicyRepository.existsById(policyNumber)).thenReturn(false); //false - entry doesn't exist - throws Exception

        // insurancePolicyServices.deletePolicy(policyNumber); => throws Exception
        assertThrows(InsurancePolicyNotFoundException.class, () -> insurancePolicyServices.deletePolicy(policyNumber)); //test success - exception asserted

    }
}