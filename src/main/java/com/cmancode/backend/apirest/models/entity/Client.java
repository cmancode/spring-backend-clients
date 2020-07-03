package com.cmancode.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@AllArgsConstructor
public class Client implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter @Setter
	private Long id;
	
	@Column(name="names", length = 60, nullable = false)
	@Getter @Setter
	@NotEmpty
	private String names;
	
	@Column(name="last_name", length = 50, nullable = false)
	@Getter @Setter
	@NotEmpty
	private String lastName;
	
	@Column(name="email", length = 60, nullable = false, unique = false)
	@Email @NotEmpty
	@Getter @Setter
	private String email;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="birth_date", nullable = false)
	@Getter @Setter
	private Date birthDate;
	
	@Column(name="create_at", nullable = false)
	@Temporal(TemporalType.DATE)
	@Getter @Setter
	private Date createAt;
	
	@Column(name = "file", nullable = true, length = 100)
	@Getter @Setter
	private String file;
	
	@PrePersist
	private void prePersist() {
		this.createAt = new Date();
	}
	
	private static final long serialVersionUID = 1L;

}
