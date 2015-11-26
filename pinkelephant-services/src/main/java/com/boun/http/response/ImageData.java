package com.boun.http.response;

import com.boun.data.common.enums.FileType;

import lombok.Data;

@Data
public class ImageData {

	FileType type;
	String base64Image;
}
