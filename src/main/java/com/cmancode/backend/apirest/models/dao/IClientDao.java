package com.cmancode.backend.apirest.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cmancode.backend.apirest.models.entity.Client;


public interface IClientDao extends JpaRepository<Client, Long> {

}
