package com.springboot.DocumentAPI.Model;

import lombok.Data;

@Data
public class DocData {
	private String documentType;
	private byte[] data;
	private boolean verified;
}
