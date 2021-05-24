package com.springboot.DocumentAPI.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Document {
	@Id
	private String entityId;
	private String docId;
	private String date;
	private boolean verfied;
}
