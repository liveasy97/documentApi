package com.springboot.DocumentAPI.Model;

import java.util.List;

import lombok.Data;

@Data
public class MainDocForPost {
	private String entityId;
	private List<DocData> data;
}
