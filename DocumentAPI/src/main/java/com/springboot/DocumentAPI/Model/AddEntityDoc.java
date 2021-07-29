package com.springboot.DocumentAPI.Model;

import java.util.List;

import lombok.Data;

@Data
public class AddEntityDoc {
	private String entityId;
	private List<DocData> documents;
}
