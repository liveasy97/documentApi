package com.springboot.DocumentAPI.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class DocumentList {
	@Id
	private String Id;
	private String docId;
	private String docType;
	private String docLink;
	private boolean verified;
}
