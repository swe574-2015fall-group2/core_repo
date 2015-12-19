package com.boun.http.request;

import javax.validation.constraints.NotNull;

import com.boun.data.common.enums.FileType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UploadImageRequest extends BaseRequest{

	@NotNull
	private String entityId;
	
	@NotNull
	private FileType fileType;
	
	@NotNull
	private String base64Image;
}
