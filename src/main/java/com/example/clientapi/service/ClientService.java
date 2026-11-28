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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado. Id: " + id));
    }

    @Transactional
    public Client insert(Client client) {
        if (repository.existsByCpf(client.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado: " + client.getCpf());
        }
        return repository.save(client);
    }

    @Transactional
    public Client update(Long id, Client client) {
        Client entity = findById(id);
        if (repository.existsByCpfAndIdNot(client.getCpf(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado: " + client.getCpf());
        }
        entity.setName(client.getName());
        entity.setCpf(client.getCpf());
        entity.setIncome(client.getIncome());
        entity.setBirthDate(client.getBirthDate());
        entity.setChildren(client.getChildren());
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(findById(id));
    }
}