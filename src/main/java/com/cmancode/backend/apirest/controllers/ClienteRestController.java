package com.cmancode.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmancode.backend.apirest.models.entity.Client;
import com.cmancode.backend.apirest.models.services.IClientService;
import com.cmancode.backend.apirest.util.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping(value="/api")
public class ClienteRestController {
	
	@Autowired
	private IClientService clienteService;
	@Autowired 
	private Util util;
	
	@GetMapping("/clients")
	public ResponseEntity<List<Client>> clients() {
		List<Client> clients = null;
		clients = this.clienteService.findClients();
		if(clients.isEmpty()) {
			return new ResponseEntity<List<Client>>(HttpStatus.FOUND);
		}
		return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
	}

	@GetMapping("/client/{id}")
	public ResponseEntity<Client> findClientById(@PathVariable("id") Long id) {
		Client client = null;
		client = this.clienteService.findByIdClient(id);
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}
	
	@PostMapping("/client")
	public ResponseEntity<Client> createClient(@RequestBody Client client) {
		Client clientCreated = this.util.convertToUpperCase(client);
		this.clienteService.save(clientCreated);
		return new ResponseEntity<Client>(clientCreated, HttpStatus.OK);
	}
	
	@PutMapping("client/{id}")
	public ResponseEntity<Client> updateClient(@PathVariable("id") Long id, @RequestBody Client client) {
		Client clientFinded = null;
		clientFinded = this.clienteService.findByIdClient(id);
		
		clientFinded.setNames(client.getNames());
		clientFinded.setLastName(client.getLastName());
		clientFinded.setEmail(client.getEmail());
		clientFinded.setBirthDate(client.getBirthDate());
		this.clienteService.update(clientFinded);
		return new ResponseEntity<Client>(clientFinded, HttpStatus.OK);
	}
	
}
