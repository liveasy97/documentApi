package com.springboot.DocumentAPI.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.DocumentAPI.Entities.DocumentData;

@Repository
public interface DocumentDataDao extends JpaRepository<DocumentData, String> {
	@Query("select d from DocumentData d where d.documentId = :documentId")
	List<DocumentData> findByDocId(String documentId);

//	@Transactional
	@Modifying
	@Query("delete from DocumentData d where d.documentId = :documentId")
	void deleteByDocumentId(String documentId);
}
