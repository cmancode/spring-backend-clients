package com.cmancode.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmancode.backend.apirest.models.entity.Client;
import com.cmancode.backend.apirest.models.services.IClientService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping(value="/api")
public class ClienteRestController {
	
	@Autowired
	private IClientService clienteService;
	
	@GetMapping("/clients")
	public ResponseEntity<List<Client>> clients() {
		List<Client> clients = null;
		clients = this.clienteService.findClients();
		if(clients.isEmpty()) {
			return new ResponseEntity<List<Client>>(HttpStatus.FOUND);
		}
		log.info("Datos encontrados con éxito!!");
		return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
	}

}
