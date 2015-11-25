package com.boun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.common.Constants;
import com.boun.data.common.enums.FileType;
import com.boun.http.request.UploadImageRequest;

public final class ImageUtil {
	
	public static String saveImage(String entityName, UploadImageRequest request){
		
		File folder = new File(getImageFolder(entityName));
		folder.mkdirs();
		
		File file = new File(getFilename(folder.getAbsolutePath(), request.getEntityId(), request.getFileType()));
		
		FileOutputStream output = null;
		try{
			byte[] image = Base64.decodeBase64(request.getBase64Image());
			
			output = new FileOutputStream(file);
			IOUtils.write(image, output);
			
		}catch(Throwable e){
			throw new PinkElephantRuntimeException(400, ErrorCode.ERROR_WHILE_SAVING_IMAGE, e.getMessage(), "");
		}finally{
			if(output != null){
				try{
					output.close();	
				}catch(Throwable e){}
			}
		}

		return file.getAbsolutePath();
	}
	
	public static String getImage(String imagePath){
		if(imagePath == null){
			return null;
		}
		
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(imagePath);
			
		    byte[] image = IOUtils.toByteArray(inputStream);
		    
		    return Base64.encodeBase64String(image);
		    
		}catch(Throwable e){
			
		} finally {
			if(inputStream != null){
				try{
					inputStream.close();	
				}catch(Throwable e){}
			}
		}
		return null;
	}
	
	private static String getImageFolder(String entityName){
		StringBuffer folder = new StringBuffer();
		folder.append(Constants.IMAGE_BASE_FILE_PATH).append("/");
		folder.append(entityName);
		
		return folder.toString();
	}
	
	private static String getFilename(String folder, String groupId, FileType fileType){
		StringBuffer filename = new StringBuffer();
		filename.append(folder).append("/");
		filename.append(groupId).append(".");
		filename.append(fileType.name());
		
		return filename.toString();
	}

}
