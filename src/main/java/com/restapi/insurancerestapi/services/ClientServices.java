package com.restapi.insurancerestapi.services;

import com.restapi.insurancerestapi.entities.Client;
import com.restapi.insurancerestapi.exceptions.ClientNotFoundException;
import com.restapi.insurancerestapi.repositories.ClientRepository;
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
public class ClientServices {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServices(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAllClients() {
        log.info("findAllClients() - called");
        return clientRepository.findAll();
    }

    public Client findSpecificClient(Integer id) {
        log.info("findSpecificClient() - called");
        if (!(clientRepository.existsById(id))) {
            throw new ClientNotFoundException("Client ID : " + id + " information is not available");
        }
        return clientRepository.findById(id).get();
    }

    @Transactional
    public ResponseEntity<String> createNewClient(Client client) {
        log.info("createNewClient() - called");
        if (clientRepository.existsById(Math.toIntExact(client.getClientId()))) {
            throw new ClientNotFoundException("Client ID : " + client.getClientId() + " is already Present - Insert new Client ID");
        }

        clientRepository.save(client);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getClientId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Transactional
    public ResponseEntity<String> updateClient(Integer id, Client client) {
        log.info("updateClient() - called");
        if (!(clientRepository.existsById(id))) {
            throw new ClientNotFoundException("Client ID : " + id + " doesn't exist - Update Failed");
        }

        Client curClient = clientRepository.findById(id).get();
        curClient.setClientId(client.getClientId());
        curClient.setAddress(client.getAddress());
        curClient.setName(client.getName());
        curClient.setContactInfo(client.getContactInfo());
        curClient.setDateOfBirth(client.getDateOfBirth());
        clientRepository.save(curClient);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.getClientId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @Transactional
    public ResponseEntity<String> deleteClient(Integer id) {
        log.info("deleteClient() - called");
        if (!(clientRepository.existsById(id))) {
            throw new ClientNotFoundException("Client ID : " + id + " doesn't exist - Deletion Failed");
        }
        clientRepository.deleteById(id);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
