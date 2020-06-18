package com.cmancode.backend.apirest.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmancode.backend.apirest.models.dao.IClientDao;
import com.cmancode.backend.apirest.models.entity.Client;


@Service
public class ClientServiceImpl implements IClientService {

	@Autowired
	private IClientDao clienteDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Client> findClients() {
		return (List<Client>) this.clienteDao.findAll();
	}

	@Override
	@Transactional
	public Client save(Client client) {
		// TODO Auto-generated method stub
		return this.clienteDao.save(client);
	}

	@Override
	@Transactional
	public Client update(Client client) {
		// TODO Auto-generated method stub
		return this.clienteDao.save(client);
	}

	@Override
	@Transactional(readOnly = true)
	public Client findByIdClient(Long id) {
		// TODO Auto-generated method stub
		return this.clienteDao.findById(id).orElse(null);
	}

}
