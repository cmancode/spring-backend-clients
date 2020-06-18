package com.cmancode.backend.apirest.models.services;

import java.util.List;

import com.cmancode.backend.apirest.models.entity.Client;


public interface IClientService {
	
	public Client save(Client client);
	public List<Client> findClients();
	public Client update(Client client);
	public Client findByIdClient(Long id);

}
