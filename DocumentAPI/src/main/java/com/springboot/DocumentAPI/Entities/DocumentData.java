package com.springboot.DocumentAPI.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class DocumentData {
	@Id
	private String Id;
	private String documentId;
	private String documentType;
	private String documentLink;
	private boolean verified;
}
