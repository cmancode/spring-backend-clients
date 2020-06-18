package com.cmancode.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
		return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
	}

	@GetMapping("/client/{id}")
	public ResponseEntity<?> findClientById(@PathVariable("id") Long id) {
		Client client = null;
		Map<String, Object> mensaje = new HashMap<>();
		
		try {
			client = this.clienteService.findByIdClient(id);
		} catch (DataAccessException e) {
			mensaje.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(mensaje, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(client == null) {
			mensaje.put("mensaje", "No existen registros con base al criterio ingresado.");
			return new ResponseEntity<Map<String, Object>>(mensaje, HttpStatus.NOT_FOUND);
		}
		
		
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
		
		clientFinded.setNames(client.getNames().toUpperCase());
		clientFinded.setLastName(client.getLastName().toUpperCase());
		clientFinded.setEmail(client.getEmail());
		clientFinded.setBirthDate(client.getBirthDate());
		this.clienteService.update(clientFinded);
		return new ResponseEntity<Client>(clientFinded, HttpStatus.OK);
	}
	
}
