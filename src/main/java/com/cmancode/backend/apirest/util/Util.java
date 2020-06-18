package com.cmancode.backend.apirest.util;

import org.springframework.stereotype.Component;

import com.cmancode.backend.apirest.models.entity.Client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Util {
	
	
	public Client convertToUpperCase(Client client) {
		log.info(client.getNames().concat(" ").concat(client.getLastName()));
		client.setNames(client.getNames().toUpperCase());
		client.setLastName(client.getLastName().toUpperCase());
		client.setEmail(client.getEmail());
		client.setBirthDate(client.getBirthDate());
		log.info(client.getNames().concat(" ").concat(client.getLastName()));
		return client;
	}
	
}
