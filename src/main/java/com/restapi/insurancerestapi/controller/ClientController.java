package com.restapi.insurancerestapi.controller;

import com.restapi.insurancerestapi.entities.Client;
import com.restapi.insurancerestapi.services.ClientServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClientController {

    private final ClientServices clientServices;

    @Autowired
    public ClientController(ClientServices clientServices) {
        this.clientServices = clientServices;
    }

    @GetMapping("/api/clients")
    public List<Client> fetchAllClients() {
        return clientServices.findAllClients();
    }

    @GetMapping("/api/clients/{id}")
    public Client fetchClient(@PathVariable Integer id) {
        return clientServices.findSpecificClient(id);
    }

    @PostMapping("/api/clients")
    public ResponseEntity<String> createNewClient(@Valid @RequestBody Client client) {
        return clientServices.createNewClient(client);
    }

    @PutMapping("/api/clients/{id}")
    public ResponseEntity<String> updateClient(@PathVariable Integer id, @Valid @RequestBody Client client) {
        return clientServices.updateClient(id, client);
    }

    @DeleteMapping("/api/clients/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id) {
        return clientServices.deleteClient(id);
    }
}
