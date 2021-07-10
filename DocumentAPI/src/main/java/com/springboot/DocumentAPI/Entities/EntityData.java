package com.springboot.DocumentAPI.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class EntityData {
	@Id
	private String entityId;
	private String documentId;
	private String date;
	private boolean verfied;
}
