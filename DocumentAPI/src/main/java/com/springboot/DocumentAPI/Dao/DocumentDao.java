package com.springboot.DocumentAPI.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.DocumentAPI.Entities.Document;

public interface DocumentDao extends JpaRepository<Document, String> {
	@Query("select docId from Document d where d.entityId = :entityId")
	String findByEntityId(String entityId);
	
	@Query("select d from Document d where d.entityId LIKE 'truck%'")
	List<Document> findByTruckType();
	
	@Query("select d from Document d where d.entityId LIKE 'transporter%'")
	List<Document> findByTransporterType();
	
	
}
