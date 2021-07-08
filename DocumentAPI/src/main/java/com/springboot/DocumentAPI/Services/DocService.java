package com.springboot.DocumentAPI.Services;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.springboot.DocumentAPI.Entities.EntityData;
import com.springboot.DocumentAPI.Model.AddEntityDoc;
import com.springboot.DocumentAPI.Model.DocData;
import com.springboot.DocumentAPI.Model.UpdateEntityDoc;
import com.springboot.DocumentAPI.Response.DocumentCreateResponse;
import com.springboot.DocumentAPI.Response.DocumentUpdateResponse;
import com.springboot.DocumentAPI.Model.GetEntityDoc;

public interface DocService {

	DocumentCreateResponse addDocument(AddEntityDoc addEntityDoc);

	GetEntityDoc getDocuments(String entityId);

	List<EntityData> getByEntityType(String entityType);

	DocumentUpdateResponse updateDocuments(String entityId, UpdateEntityDoc updateEntityDoc);

}
