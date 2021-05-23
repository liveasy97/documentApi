package com.springboot.DocumentAPI.Services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.springboot.DocumentAPI.Constants.CommonConstants;
import com.springboot.DocumentAPI.Dao.DocumentDao;
import com.springboot.DocumentAPI.Dao.DocumentListDao;
import com.springboot.DocumentAPI.Entities.Document;
import com.springboot.DocumentAPI.Entities.DocumentList;
import com.springboot.DocumentAPI.Model.MainDocForGet;
import com.springboot.DocumentAPI.Model.MainDocForPost;
import com.springboot.DocumentAPI.Model.MainDocForUpdate;
import com.springboot.DocumentAPI.Response.DocumentCreateResponse;
import com.springboot.DocumentAPI.Response.DocumentUpdateResponse;

import ch.qos.logback.core.net.server.Client;

@Service
public class DocServiceImpl implements DocService {

	@Autowired
	private DocumentListDao docListDao;
	@Autowired
	private DocumentDao docDao;
	@Autowired
	private AmazonS3 client;
	
	private String bucketname = "liveasyuploadimage";

	@Override
	public DocumentCreateResponse addDocument(MultipartFile[] file, MainDocForPost mainDoc) {
		// TODO Auto-generated method stub
		DocumentCreateResponse dcr = new DocumentCreateResponse(); 
		Optional<Document> D = docDao.findById(mainDoc.getEntityId());
		
		//check if account already exist or not
		if(D.isPresent()) {
			dcr.setStatus(CommonConstants.docExists);
			return dcr;
		}
		
		Document doc = new Document();
		DocumentList docList = new DocumentList();
			
		//get current date
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM, yyyy");
	    LocalDateTime now = LocalDateTime.now();
	    
	    //generate random document id
	    String docId = "Doc:"+UUID.randomUUID();
			
		//set document data
	    doc.setEntityId(mainDoc.getEntityId());
		doc.setDocId(docId);
	    doc.setDate(dtf.format(now).toString());
	    doc.setVerfied(false);
	  
		//set all documents data
	    for(int i=0;i<mainDoc.getData().size();i++) {
			
			//save data to AWS s3
	    	File fileobj = convertToFile(file[i]);
			client.putObject(bucketname, file[i].getOriginalFilename(), fileobj);
			client.setObjectAcl(bucketname, file[i].getOriginalFilename(), CannedAccessControlList.PublicRead);	
			URL url = client.getUrl(bucketname, file[i].getOriginalFilename());
			
			//add data to documentlist object 
			docList.setId("No: "+UUID.randomUUID());
			docList.setDocId(docId);
			docList.setDocType(mainDoc.getData().get(i).getType());
			docList.setDocLink(url.toString());
			docList.setVerified(false);
			
			docListDao.save(docList);
		}
		
		docDao.save(doc);
		dcr.setStatus(CommonConstants.uploadSuccess);
		return dcr;
	}


	@Override
	public MainDocForGet getDocuments(String entityId) {
		// TODO Auto-generated method stub
		MainDocForGet maindoc2 = new MainDocForGet();
		String docId = docDao.findByEntityId(entityId);
		
		if(docId == null) {
			return null;
		}
		
		maindoc2.setEntityId(entityId);
		maindoc2.setData(docListDao.findByDocId(docId));
		return maindoc2;
	}


	@Override
	public List<Document> getByEntityType(String entityType) {
		// TODO Auto-generated method stub
		if(entityType.equalsIgnoreCase("Truck")) {
			return docDao.findByTruckType();
		}
		else if(entityType.equalsIgnoreCase("Transporter")) {
			return docDao.findByTransporterType();
		}
		return null;
	}

	@Override
	public DocumentUpdateResponse updateDocuments(String entityId, MultipartFile[] file, MainDocForUpdate mainDocForUpdate) {
		
		DocumentUpdateResponse dur = new DocumentUpdateResponse();
		DocumentList updateDoc = new DocumentList();
		String docId = docDao.findByEntityId(entityId);
		
		//check if document with entityId exists or not
		if(docId == null) {
			dur.setStatus(CommonConstants.docNotExists);
			return dur;
		}
		
		List<DocumentList> doclist = docListDao.findByDocId(docId);
		
		for(int i=0; i<doclist.size(); i++) {
			for(int j=0; j<mainDocForUpdate.getData().size();j++) {
				if (doclist.get(i).getDocType().equals(mainDocForUpdate.getData().get(j).getType())) {
					//split url to find image name
					String a[] = doclist.get(i).getDocLink().split("/");
					
					//update aws storage
					File fileobj = convertToFile(file[j]);
					client.deleteObject(bucketname, a[3]);
					client.putObject(bucketname, file[j].getOriginalFilename(), fileobj);
					client.setObjectAcl(bucketname, file[j].getOriginalFilename(), CannedAccessControlList.PublicRead);	
					URL url = client.getUrl(bucketname, file[j].getOriginalFilename());
					
					//set and save new object with updated data
					updateDoc.setId(doclist.get(i).getId());
					updateDoc.setDocId(doclist.get(i).getDocId());
					updateDoc.setDocType(doclist.get(i).getDocType());
					updateDoc.setDocLink(url.toString());
					updateDoc.setVerified(false);
					docListDao.save(updateDoc);
				}
			}
		}
		
		dur.setStatus(CommonConstants.updatedSuccess);
		return dur;
	}
	
	//convert multipartfile to file
	public File convertToFile(MultipartFile file) {
		File output = new File(file.getOriginalFilename());
		try (FileOutputStream fos = new FileOutputStream(output)){
			fos.write(file.getBytes());
		}
		catch(IOException e) {
		}
		return output;
	}

}
