package com.springboot.DocumentAPI.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.DocumentAPI.Response.DocumentCreateResponse;
import com.springboot.DocumentAPI.Response.DocumentUpdateResponse;
import com.springboot.DocumentAPI.Entities.EntityData;
import com.springboot.DocumentAPI.Model.AddEntityDoc;
import com.springboot.DocumentAPI.Model.DocData;
import com.springboot.DocumentAPI.Model.GetEntityDoc;
import com.springboot.DocumentAPI.Model.UpdateEntityDoc;
import com.springboot.DocumentAPI.Services.DocService;

@RestController
public class Controller {
	
	@Autowired
	private DocService docService;
	
	@GetMapping("/home")
	public String home() {
		return "Welcome to documentApi...!!!";
	}
	
	@PostMapping("/document")
	public DocumentCreateResponse addDocument(@RequestBody AddEntityDoc addEntityDoc) {
		return docService.addDocument(addEntityDoc);
	}
	
	@GetMapping("/document/{entityId}")
	public GetEntityDoc getDocuments(@PathVariable String entityId) {
		return docService.getDocuments(entityId);
	}
	
	@GetMapping("/document")
	public List<EntityData> getByEntityType(@RequestParam(required = false) String entityType) {
		return docService.getByEntityType(entityType);
	}
	
	@PutMapping("/document/{entityId}")
	public ResponseEntity<Object> updateDocuments(@PathVariable String entityId, @RequestBody UpdateEntityDoc updateEntityDoc ) {
		return new ResponseEntity<>(docService.updateDocuments(entityId, updateEntityDoc),HttpStatus.OK);
	}
	
//	@DeleteMapping("/document/{entityId}")
//	public ResponseEntity<Object> deleteDocument(@PathVariable String entityId){
//		docService.deleteDocument(entityId);
//		return new ResponseEntity<>("Successfully deleted",HttpStatus.OK);
//	}
}
