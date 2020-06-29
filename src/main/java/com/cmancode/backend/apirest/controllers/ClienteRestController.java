package com.cmancode.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
	
	@GetMapping("/clients/{page}")
	public ResponseEntity<Page<Client>> clients(@PathVariable("page") Integer page) {
		Page<Client> clients = this.clienteService.findAll(PageRequest.of((page-1), 5));
		return new ResponseEntity<Page<Client>>(clients, HttpStatus.OK);
	}

	@GetMapping("/client/{id}")
	public ResponseEntity<?> findClientById(@PathVariable("id") Long id) {
		Client client = null;
		Map<String, Object> mensaje = new HashMap<>();
		try {
			client = this.clienteService.findByIdClient(id);
		} catch (DataAccessException e) {
			mensaje.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(mensaje, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(client == null) {
			mensaje.put("mensaje", "No existen registros con base al criterio ingresado.");
			return new ResponseEntity<Map<String, Object>>(mensaje, HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<Client>(client, HttpStatus.OK);
	}
	
	@PostMapping("/client")
	public ResponseEntity<?> createClient(@Valid @RequestBody Client client, BindingResult result) {
		
		Client clientCreated = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(error -> {
						return "El campo "+error.getField()+" "+error.getDefaultMessage();
					}).collect(Collectors.toList());
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		try {
			clientCreated = this.util.convertToUpperCase(client);
			this.clienteService.save(clientCreated);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error: "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "¡Datos guardados de manera exitosa!");
		response.put("cliente", clientCreated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PutMapping("client/{id}")
	public ResponseEntity<?> updateClient(@PathVariable("id") Long id, @RequestBody Client client) {
		Client clientFinded = null;
		Map<String, Object> response = new HashMap<>();
		log.info("ID: "+id);
		clientFinded = this.clienteService.findByIdClient(id);
		if(clientFinded == null) {
			response.put("mensaje", "No existen registros con base al criterio ingresado.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		try {
			clientFinded.setNames(client.getNames().toUpperCase());
			clientFinded.setLastName(client.getLastName().toUpperCase());
			clientFinded.setEmail(client.getEmail());
			clientFinded.setBirthDate(client.getBirthDate());
			this.clienteService.update(clientFinded);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error: "+e.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "¡Datos actualizados de manera exitosa!");
		response.put("cliente", clientFinded);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
