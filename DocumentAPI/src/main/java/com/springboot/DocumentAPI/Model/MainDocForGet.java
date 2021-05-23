package com.springboot.DocumentAPI.Model;

import java.util.List;

import com.springboot.DocumentAPI.Entities.DocumentList;

import lombok.Data;

@Data
public class MainDocForGet {
	private String entityId;
	private List<DocumentList> data;
}
