package com.springboot.DocumentAPI.Services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.springboot.DocumentAPI.Entities.Document;
import com.springboot.DocumentAPI.Model.MainDocForPost;
import com.springboot.DocumentAPI.Model.MainDocForUpdate;
import com.springboot.DocumentAPI.Response.DocumentCreateResponse;
import com.springboot.DocumentAPI.Response.DocumentUpdateResponse;
import com.springboot.DocumentAPI.Model.MainDocForGet;

public interface DocService {

	DocumentCreateResponse addDocument(MultipartFile[] file, MainDocForPost mainDocForPost);

	MainDocForGet getDocuments(String entityId);

	List<Document> getByEntityType(String entityType);

	DocumentUpdateResponse updateDocuments(String entityId, MultipartFile[] file, MainDocForUpdate mainDocForUpdate);

}
