package com.boun.data.mongo.model;

import com.boun.data.common.enums.FileType;

import lombok.Data;

@Data
public class ImageInfo {
	private FileType type;
	private String imagePath;  
}
