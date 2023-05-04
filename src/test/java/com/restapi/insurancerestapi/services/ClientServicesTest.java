package com.restapi.insurancerestapi.services;

import com.restapi.insurancerestapi.entities.Client;
import com.restapi.insurancerestapi.exceptions.ClientNotFoundException;
import com.restapi.insurancerestapi.repositories.ClientRepository;
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
public class ClientServicesTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServices clientServices;

    @Test
    @Order(1)
    public void test_findAllClients() {

        List<Client> clients = Arrays.asList(
                new Client(1, "Random Name", LocalDate.of(1980, 1, 1), "123 AAAAAAAAAAAA BBBBBBBBB Main St", "1234567890"),
                new Client(2, "Random Name", LocalDate.of(1990, 2, 2), "456 AAAAAAAAAAAA BBBBBBBBB Elm St", "1234567890")
        );

        when(clientRepository.findAll()).thenReturn(clients);

        assertEquals(2, clientServices.findAllClients().size());            //test success
        assertEquals(clients.get(0), clientServices.findAllClients().get(0));       //test success
        assertEquals(clients.get(1), clientServices.findAllClients().get(1));       //test success
    }

    @Test
    @Order(2)
    public void test_findSpecificClient() {

        Client client = new Client(1, "Random Name", LocalDate.of(1980, 1, 1), "123 AAAAAAAAAAAA BBBBBBBBB Main St", "1234567890");

        when(clientRepository.existsById(client.getClientId())).thenReturn(true);           //true - entry exists - doesn't throw exception
        when(clientRepository.findById(client.getClientId())).thenReturn(Optional.of(client));

        Client result = clientServices.findSpecificClient(client.getClientId());

        assertEquals(client, result);       //test success - client retrieved successfully
    }

    @Test
    @Order(3)
    public void test_findSpecificClientNotFound() {

        Integer clientId = 1;

        when(clientRepository.existsById(clientId)).thenReturn(false); // false - entry doesn't exist - throws Exception

        //clientServices.findSpecificClient(clientId);  => throws Exception
        assertThrows(ClientNotFoundException.class, () -> clientServices.findSpecificClient(clientId)); //test success - exception asserted
    }

    @Test
    @Order(4)
    public void testCreateNewClient() {

        Client client = new Client(1, "Random Name", LocalDate.of(1980, 1, 1), "123 AAAAAAAAAAAA BBBBBBBBB Main St", "1234567890");

        when(clientRepository.existsById(client.getClientId())).thenReturn(false);  //false - entry doesn't exist - doesn't throw Exception
        when(clientRepository.save(client)).thenReturn(client);

        ResponseEntity<String> response = clientServices.createNewClient(client);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());     // test success - new client created successfully
    }

    @Test
    @Order(5)
    public void testCreateNewClientAlreadyExists() {

        Client client = new Client(1, "Random Name", LocalDate.of(1980, 1, 1), "123 AAAAAAAAAAAA BBBBBBBBB Main St", "1234567890");

        when(clientRepository.existsById(client.getClientId())).thenReturn(true);   //true - entry exists - throws Exception

        //clientServices.createNewClient(client); => throws Exception
        assertThrows(ClientNotFoundException.class, () -> clientServices.createNewClient(client)); //test success - exception asserted
    }

    @Test
    @Order(6)
    public void testUpdateClient() {

        Integer clientId = 1;

        Client existingClient = new Client(1, "Random Name", LocalDate.of(1980, 1, 1), "123 AAAAAAAAAAAA BBBBBBBBB Main St", "1234567890");
        Client updatedClient = new Client(1, "New Random Name", LocalDate.of(1980, 1, 1), "123 AAAAAAAAAAAA BBBBBBBBB Main St", "1234567890");

        when(clientRepository.existsById(clientId)).thenReturn(true);                   //true - entry exists - doesn't throw Exception
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(existingClient)).thenReturn(updatedClient);

        ResponseEntity<String> response = clientServices.updateClient(clientId, updatedClient);

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); //test success - client updated successfully
    }

    @Test
    @Order(7)
    public void testUpdateClientNotFound() {

        Integer id = 1;

        Client client = new Client(1, "Random Name", LocalDate.of(1980, 1, 1), "123 AAAAAAAAAAAA BBBBBBBBB Main St", "1234567890");

        when(clientRepository.existsById(id)).thenReturn(false); //false - entry doesn't exist - throw Exception

        //clientServices.updateClient(id, client); => throws Exception
        assertThrows(ClientNotFoundException.class, () -> clientServices.updateClient(id, client)); //test success - exception asserted
    }

    @Test
    @Order(8)
    void testDeleteClient() {

        Integer clientId = 1;

        when(clientRepository.existsById(clientId)).thenReturn(true); //true - entry exist - doesn't throw Exception
        doNothing().when(clientRepository).deleteById(clientId);

        ResponseEntity<String> response = clientServices.deleteClient(clientId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); //test success - client deleted successfully
    }

    @Test
    @Order(9)
    void testDeleteClientNotFound() {

        Integer clientId = 1;

        when(clientRepository.existsById(clientId)).thenReturn(false);  //false - entry doesn't exist - throw Exception

        //clientServices.deleteClient(clientId)); => throws Exception
        assertThrows(ClientNotFoundException.class, () -> clientServices.deleteClient(clientId)); //test success - exception asserted
    }
}