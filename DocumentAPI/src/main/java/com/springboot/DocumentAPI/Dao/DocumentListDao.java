package com.springboot.DocumentAPI.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.springboot.DocumentAPI.Entities.DocumentList;

public interface DocumentListDao extends JpaRepository<DocumentList, String> {
	@Query("select d from DocumentList d where d.docId = :docId")
	List<DocumentList> findByDocId(String docId);
}
