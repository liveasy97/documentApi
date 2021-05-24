package com.springboot.DocumentAPI.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.springboot.DocumentAPI.Entities.Document;
import com.springboot.DocumentAPI.Model.MainDocForPost;
import com.springboot.DocumentAPI.Model.MainDocForUpdate;
import com.springboot.DocumentAPI.Response.DocumentCreateResponse;
import com.springboot.DocumentAPI.Response.DocumentUpdateResponse;
import com.springboot.DocumentAPI.Model.MainDocForGet;
import com.springboot.DocumentAPI.Services.DocService;

@RestController
public class Controller {
	
	@Autowired
	private DocService docService;
	
	@PostMapping("/document")
	public DocumentCreateResponse addShipper(@RequestPart(value = "file") MultipartFile[] file, @RequestPart(value = "maindoc") MainDocForPost mainDocForPost) {
		return docService.addDocument(file, mainDocForPost);
	}
	
	@GetMapping("/document/{entityId}")
	public MainDocForGet getData(@PathVariable String entityId) {
		return docService.getDocuments(entityId);
	}
	
	@GetMapping("/document")
	public List<Document> getByEntityType(@RequestParam String entityType) {
		return docService.getByEntityType(entityType);
	}
	
	@PutMapping("/document/{entityId}")
	public DocumentUpdateResponse updateDocuments(@PathVariable String entityId, @RequestPart(value = "file") MultipartFile[] file, @RequestPart(value = "maindoc") MainDocForUpdate mainDocForUpdate ) {
		return docService.updateDocuments(entityId, file, mainDocForUpdate);
	}
}
