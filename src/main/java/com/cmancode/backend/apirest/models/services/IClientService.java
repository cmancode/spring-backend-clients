package com.cmancode.backend.apirest.models.services;

import java.util.List;

import com.cmancode.backend.apirest.models.entity.Client;


public interface IClientService {
	
	public List<Client> findClients();

}
