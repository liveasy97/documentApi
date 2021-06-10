package com.springboot.DocumentAPI.Model;

import java.util.List;

import lombok.Data;

@Data
public class UpdateEntityDoc {
	private List<DocData> documents;
}
