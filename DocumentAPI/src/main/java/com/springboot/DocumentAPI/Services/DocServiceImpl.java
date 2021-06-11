package com.springboot.DocumentAPI.Services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.springboot.DocumentAPI.Constants.CommonConstants;
import com.springboot.DocumentAPI.Dao.DocumentDataDao;
import com.springboot.DocumentAPI.Dao.EntityDao;
import com.springboot.DocumentAPI.Entities.DocumentData;
import com.springboot.DocumentAPI.Entities.EntityData;
import com.springboot.DocumentAPI.Model.AddEntityDoc;
import com.springboot.DocumentAPI.Model.DocData;
import com.springboot.DocumentAPI.Model.GetEntityDoc;
import com.springboot.DocumentAPI.Model.UpdateEntityDoc;
import com.springboot.DocumentAPI.Response.DocumentCreateResponse;
import com.springboot.DocumentAPI.Response.DocumentUpdateResponse;

@Service
public class DocServiceImpl implements DocService {

	@Autowired
	private DocumentDataDao docDataDao;
	@Autowired
	private EntityDao entityDao;
	@Autowired
	private AmazonS3 client;
	
	private String bucketname = "liveasyuploadimage";

	@Override
	public DocumentCreateResponse addDocument(AddEntityDoc entityDoc) {
		DocumentCreateResponse dcr = new DocumentCreateResponse(); 
		Optional<EntityData> D = entityDao.findById(entityDoc.getEntityId());
		
		//check if account already exist or not
		if(D.isPresent()) {
			dcr.setStatus(CommonConstants.docExists);
			return dcr;
		}
		
		EntityData entityData = new EntityData();
		DocumentData docList = new DocumentData();
			
		//get current date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM, yyyy");
	    LocalDateTime now = LocalDateTime.now();
	    
	    //generate random document id
	    String docId = "doc:"+UUID.randomUUID();
			
		//set entity data
	    entityData.setEntityId(entityDoc.getEntityId());
		entityData.setDocumentId(docId);
	    entityData.setDate(dtf.format(now).toString());
	    entityData.setVerfied(false);
	    
		for(DocData user: entityDoc.getDocuments()) {
			
			String imageName = entityDoc.getEntityId()+user.getDocumentType()+".jpg";
		
			//convert bytedata to file object
		    File fileobj = new File(imageName);
		    try (FileOutputStream fos = new FileOutputStream(fileobj)) { 
		    	fos.write(user.getData());
		    }
		   	catch(IOException e) {	
		   	}
		    
		   	//upload data and generate url
		    client.putObject(bucketname, imageName, fileobj);
			client.setObjectAcl(bucketname, imageName, CannedAccessControlList.PublicReadWrite);
		   	URL url = client.getUrl(bucketname, imageName);
		   	fileobj.delete();
		   	
		   	//set document data
		   	docList.setId("id"+UUID.randomUUID());
			docList.setDocumentId(docId);
			docList.setDocumentType(user.getDocumentType());
			docList.setDocumentLink(url.toString());
			docDataDao.save(docList);
			
		}
		
		entityDao.save(entityData);
		dcr.setStatus(CommonConstants.uploadSuccess);
		return dcr;
	}


	@Override
	public GetEntityDoc getDocuments(String entityId) {
		if(entityId==null) {
			return null;
		}
		
		GetEntityDoc getEntity = new GetEntityDoc();
		String documentId = entityDao.findByEntityId(entityId);
		getEntity.setEntityId(entityId);
		getEntity.setDocuments(docDataDao.findByDocId(documentId));
		return getEntity;
	}


	@Override
	public List<EntityData> getByEntityType(String entityType) {
		// TODO Auto-generated method stub
		if(entityType != null) {
			if(entityType.equalsIgnoreCase("Truck")) {
				return entityDao.findByTruckType();
			}
			else if(entityType.equalsIgnoreCase("Transporter")) {
				return entityDao.findByTransporterType();
			}
			else if(entityType.equalsIgnoreCase("Shipper")) {
				return entityDao.findByShipperType();
			}
		}
		return entityDao.findAll();
	}

	@Override
	public DocumentUpdateResponse updateDocuments(String entityId, UpdateEntityDoc updateEntityDoc) {
		
		DocumentUpdateResponse dur = new DocumentUpdateResponse();
		DocumentData updateDoc = new DocumentData();
		String documentId = entityDao.findByEntityId(entityId);
		
		//check if document with entityId exists or not
		if(documentId == null) {
			dur.setStatus(CommonConstants.docNotExists);
			return dur;
		}
		
		List<DocumentData> doclist = docDataDao.findByDocId(documentId);
		boolean found;
		for(DocData user: updateEntityDoc.getDocuments()) {
			found=false;
			for(DocumentData database: doclist) {
				//update existing document
				if(user.getDocumentType().equals(database.getDocumentType())) {
					
					if(user.getData()!= null) {
						String s[] = database.getDocumentLink().split("/");
						client.deleteObject(bucketname, s[3]);
						String imageName = entityId+user.getDocumentType()+".jpg";
						File fileobj = new File(imageName);
						
						try (FileOutputStream fos = new FileOutputStream(fileobj)) { 
					    	fos.write(user.getData());
						}
					   	catch(IOException e) {	
					
					   	}
						
						client.putObject(bucketname, imageName, fileobj);
						client.setObjectAcl(bucketname, imageName, CannedAccessControlList.PublicReadWrite);
						URL url = client.getUrl(bucketname, imageName);
						database.setDocumentLink(url.toString());
						fileobj.delete();
					}
	
					database.setVerified(user.isVerified());
					docDataDao.save(database);
					found = true;
				}
			}
			
			//add new data
			if(!found) {
				//set object name
				String imageName = entityId+user.getDocumentType()+".jpg";
				
				//convert byte data to file object
			    File fileobj = new File(imageName);
			    try (FileOutputStream fos = new FileOutputStream(fileobj)) { 
			    	fos.write(user.getData());
			    }
			   	catch(IOException e) {	
			   	}
			    
			    //upload data and get url
			   	client.putObject(bucketname, imageName, fileobj);
				client.setObjectAcl(bucketname, imageName, CannedAccessControlList.PublicReadWrite);
			   	URL url = client.getUrl(bucketname, imageName);
			   	fileobj.delete();
				
				//set new document data
				updateDoc.setDocumentId(documentId);
				updateDoc.setId("doc:"+UUID.randomUUID());
				updateDoc.setDocumentType(user.getDocumentType());
				updateDoc.setDocumentLink(url.toString());
				docDataDao.save(updateDoc);
			}
		}
		
		//check if all documents of given entityId are verfied or not
		EntityData entity = entityDao.findById(entityId).get();
		List<DocumentData> verifiedTest = docDataDao.findByDocId(documentId);
		for(DocumentData d: verifiedTest) {
			if(d.isVerified()==false) {
				entity.setVerfied(false);
				break;
			}
			entity.setVerfied(true);
		}
		entityDao.save(entity);
		
		dur.setStatus(CommonConstants.updatedSuccess);
		return dur;
	}
	
}
