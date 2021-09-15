package com.kh.toy.common.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipartParams {

	private Map<String, List> res = new HashMap<String, List>();

	public MultipartParams(Map<String, List> res) {
		this.res = res;
	}

	public String getParameter(String name) {
		if (name.equals("com.kh.toy.files")) {
			throw new RuntimeException("com.kh.toy.files는 사용할 수 없는 파라미터 명입니다.");
		}

		String param = (String) res.get(name).get(0);
		return param;
	}

	public String[] getParameterValues(String name) {
		List<String> paramsValues = res.get(name);
		return paramsValues.toArray(new String[paramsValues.size()]);
	}

	public List<FileDTO> getFileInfo() {
		List<FileDTO> fileDTOList = res.get("com.kh.toy.files");
		return fileDTOList;
	}

}
