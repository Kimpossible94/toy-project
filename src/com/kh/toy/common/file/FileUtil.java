package com.kh.toy.common.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.kh.toy.common.code.Config;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandlableException;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class FileUtil {

	// multipart 요청 도착하면 MultipartParser를 사용해서
	// 파일업로드 처리, 요청파라미터를 알아서 안담아주기 때문에 요청파라미터를 저장, FileDTO 생성
	// 여러개의 파일을 올려줄 수 있어야하므로, FileDTO가 여러개 이다.
	// 따라서, Map<String, List>를 반환해줘야한다.

	private static final int MAX_SIZE = 1024 * 1024 * 10;

	public MultipartParams fileUpload(HttpServletRequest request) {

		Map<String, List> res = new HashMap<String, List>();
		List<FileDTO> fileDTOs = new ArrayList<FileDTO>();

		try {
			MultipartParser parser = new MultipartParser(request, MAX_SIZE);
			parser.setEncoding("UTF-8");
			Part part = null;

			// readNextPart로 각각의 Part를 읽고 더이상 없으면 null 반환
			while ((part = parser.readNextPart()) != null) {
				
				//input type=file 요소가 존재하면 사용자가 파일을 첨부하지 않았더라도,
				//빈 FilePart 객체가 넘어온다. 단 파일을 첨부하지 않았기 때문에 getFileName 메서드에서 Null이 반환된다.
				
				if (part.isFile()) {
					FilePart filePart = (FilePart) part;
					
					if(filePart.getName() != null) {
						FileDTO fileDTO = createFileDTO(filePart);
						filePart.writeTo(new File(getSavePath() + fileDTO.getRenameFileName())); // 파일저장
						fileDTOs.add(fileDTO);
					}
				} else {
					// 파일이 아닐경우(파라미터일 경우)
					ParamPart paramPart = (ParamPart) part;
					setParameterMap(paramPart, res);
				}
			}

			res.put("com.kh.toy.files", fileDTOs);

		} catch (IOException e) {
			throw new HandlableException(ErrorCode.FAILED_FILE_UPLOAD_ERROR);
		}

		return new MultipartParams(res);

	}

	private void setParameterMap(ParamPart paramPart, Map<String, List> res) throws UnsupportedEncodingException {
		if (res.containsKey(paramPart.getName())) {
			res.get(paramPart.getName()).add(paramPart.getStringValue());
		} else {
			// 해당 파라미터명으로 처음 파라미터값이 저장되는 경우
			List<String> param = new ArrayList<String>();
			param.add(paramPart.getStringValue());
			res.put(paramPart.getName(), param);
		}

	}

	private String getSavePath() {

		String subPath = getSubPath();
		String savePath = Config.UPLOAD_PATH.DESC + subPath;

		File dir = new File(savePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		return savePath;
	}
	
	private String getSubPath() {
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH) + 1;
		int date = today.get(Calendar.DATE);
		return year + "\\" + month + "\\" + date + "\\";
	}
	

	private FileDTO createFileDTO(FilePart filePart) {
		FileDTO fileDTO = new FileDTO();		
		String renameFileName = UUID.randomUUID().toString();
		String savePath = getSavePath();

		fileDTO.setOriginFileName(filePart.getFileName());
		fileDTO.setRenameFileName(renameFileName);
		fileDTO.setSavePath(savePath);

		return fileDTO;
	}
}
