package com.boun.http.request;

import com.boun.data.common.enums.FileType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UploadImageRequest extends BaseRequest{

	private String entityId;
	private FileType fileType;
	private String base64Image;
}
