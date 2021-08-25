package com.springboot.DocumentAPI.Entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
@Entity
public class EntityData {
	@Id
	private String entityId;
	private String documentId;
	private String date;
	private boolean verfied;
	@CreationTimestamp
	public Timestamp timestamp;
}
