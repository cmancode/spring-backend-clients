package com.cmancode.backend.apirest.controllers;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import com.cmancode.backend.apirest.models.entity.Client;
import com.cmancode.backend.apirest.models.services.IClientService;
import com.cmancode.backend.apirest.models.services.IUploadFile;
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
	@Autowired
	private IUploadFile uploadFile;
	
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
	
	@PostMapping("/clients/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
		
		String fileName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename().replace(" ", "_");
		Map<String, Object> response = new HashMap<>();
		Client client = null;
		client = this.clienteService.findByIdClient(id);
		
		if(client == null) {
			response.put("mensaje", "El cliente con 'id' ingresado NO exite en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		if(file.isEmpty()) {
			response.put("mensaje", "No se encontró ningún archivo en el registro relacionado.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		if(client.getFile()!=null && client.getFile().length() > 0) {
			log.info("Resultado de eliminación: "+this.uploadFile.deleteFile(client.getFile()));
			log.info("Nombre del archivo: "+client.getFile());
		}
		
		if(!this.uploadFile.uploadFile(file, fileName)) {
			response.put("mensaje", "No es posible realizar la inserción del archivo en la base de datos.");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		client.setFile(fileName);
		this.clienteService.update(client);
		response.put("mensaje", "Archivo cargado exitósamente.");
		response.put("cliente", client);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/upload/img/{fileName:.+}")
	public ResponseEntity<Resource> searchFile(@PathVariable("fileName") String fileName) {
		log.info("Foto: "+fileName);		
		Path pathFile = Paths.get("files").resolve(fileName).toAbsolutePath();
		Resource resource = null;
		
		try {
			resource = new UrlResource(pathFile.toUri());
		} catch (MalformedURLException e) {
			e.getCause();
		}
		
		if(!resource.exists() && !resource.isReadable()) {
			throw new RuntimeException("No es posible cargar la imagen: "+ fileName);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
}
