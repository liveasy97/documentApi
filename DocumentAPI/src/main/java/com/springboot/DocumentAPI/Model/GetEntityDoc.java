package com.springboot.DocumentAPI.Model;

import java.util.List;

import com.springboot.DocumentAPI.Entities.DocumentData;

import lombok.Data;

@Data
public class GetEntityDoc {
	private String entityId;
	private List<DocumentData> documents;
}
