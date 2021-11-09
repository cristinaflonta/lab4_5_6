package ro.ubb.exam.Service;

import ro.ubb.exam.Domain.Client;
import ro.ubb.exam.Domain.Exceptions.ValidatorException;
import ro.ubb.exam.Repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientService {

    private Repository<Long, Client> clientRepository;

    public ClientService(Repository<Long, Client> repository) {
        this.clientRepository = repository;
    }

    public void addClient(Client client) throws ValidatorException {
        clientRepository.save(client);
    }

    public Iterable<Client> getAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findOne(Long id) {
        Optional<Client> clientOptional = clientRepository.findOne(id);
        return clientOptional;
    }


    public Optional<Client> deleteClient(Client client) throws ValidatorException {
        Optional<Client> clientOptional = clientRepository.delete(client.getId());
        return clientOptional;
    }

    public Optional<Client> updateClient(Long id, String name, String email) {
        Optional<Client> existing = clientRepository.findOne(id);
        if (existing.isPresent()) {
            Client client = existing.get();
            client.setName(name);
            client.setEmail(email);
            clientRepository.update(client);
        } else {
            System.out.println("this client does not exist");
        }
        return existing;
    }

    public List<Client> sortClientsAlph() {
        Set<Client> clientSet = (Set<Client>) clientRepository.findAll();
        return clientSet.stream().sorted((client1, client2) -> {
            return client1.getName().compareToIgnoreCase(client2.getName());
        }).collect(Collectors.toList());

    }
}
