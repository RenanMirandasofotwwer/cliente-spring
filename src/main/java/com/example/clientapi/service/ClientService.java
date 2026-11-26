package com.example.clientapi.service;

import com.example.clientapi.entity.Client;
import com.example.clientapi.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    @Transactional
    public Client insert(Client client) {
        client.setName(client.getName().trim());
        client.setCpf(client.getCpf().trim());
        return repository.save(client);
    }

    @Transactional
    public Client update(Long id, Client updatedClient) {
        Client client = findById(id);
        client.setName(updatedClient.getName().trim());
        client.setCpf(updatedClient.getCpf().trim());
        client.setIncome(updatedClient.getIncome());
        client.setBirthDate(updatedClient.getBirthDate());
        client.setChildren(updatedClient.getChildren());
        return repository.save(client);
    }

    @Transactional
    public void delete(Long id) {
        Client client = findById(id);
        repository.delete(client);
    }
}
