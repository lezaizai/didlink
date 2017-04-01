package com.didlink.rest.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import com.didlink.db.MediaDao;

@Path("/fileservice")
public class FileServiceImpl implements Controller {

	public static final String UPLOAD_FILE_SERVER = "C:/temp/upload/";

	@GET
	@Path("/download/text")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadTextFile() {

		// set file (and path) to be download
		File file = new File("C:/temp/download/Sample.txt");

		ResponseBuilder responseBuilder = Response.ok((Object) file);
		responseBuilder.header("Content-Disposition", "attachment; filename=\"MyTextFile.txt\"");
		return responseBuilder.build();
	}

	@POST
	@Path("/upload/text")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadTextFile(MultipartFormDataInput multipartFormDataInput) {

		// local variables
		MultivaluedMap<String, String> multivaluedMap = null;
		String fileName = null;
		InputStream inputStream = null;
		String uploadFilePath = null;
		MediaDao mediaDao = new MediaDao();

		try {
			Map<String, List<InputPart>> map = multipartFormDataInput.getFormDataMap();
			List<InputPart> lstInputPart = map.get("uploadedFile");

			for(InputPart inputPart : lstInputPart){

				// get filename to be uploaded
				multivaluedMap = inputPart.getHeaders();
				fileName = getFileName(multivaluedMap);

				if(null != fileName && !"".equalsIgnoreCase(fileName)){

					// write & upload file to UPLOAD_FILE_SERVER
					inputStream = inputPart.getBody(InputStream.class,null);
					uploadFilePath = writeToFileServer(inputStream, fileName);

					// close the stream
					inputStream.close();
				}

				mediaDao.saveMedia();
			}
		} catch(IOException ioe){
			ioe.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			// release resources, if any
		}
		return Response.ok("File uploaded successfully at " + uploadFilePath).build();
	}

	/**
	 * 
	 * @param inputStream
	 * @param fileName
	 * @throws IOException 
	 */
	private String writeToFileServer(InputStream inputStream, String fileName) throws IOException {

		OutputStream outputStream = null;
		String qualifiedUploadFilePath = UPLOAD_FILE_SERVER + fileName;

		try {
			File out = new File(qualifiedUploadFilePath);
			if (!out.getParentFile().exists()) {
				out.getParentFile().mkdirs();
			}

			outputStream = new FileOutputStream(out);
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.flush();
		} 
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		finally{
			//release resource, if any
			outputStream.close();
		}
		return qualifiedUploadFilePath;
	}

	/**
	 * 	
	 * @param multivaluedMap
	 * @return
	 */
	private String getFileName(MultivaluedMap<String, String> multivaluedMap) {

		String[] contentDisposition = multivaluedMap.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {

			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String exactFileName = name[1].trim().replaceAll("\"", "");
				return exactFileName;
			}
		}
		return "UnknownFile";
	}
}