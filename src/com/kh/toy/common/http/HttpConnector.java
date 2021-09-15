package com.kh.toy.common.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandlableException;

public class HttpConnector {

	//멀티쓰레드에 safe하기때문에 올려둠
	private static Gson gson = new Gson();
	
	public String get(String url) {
		
		String responseBody = "";
		
		try {
			HttpURLConnection conn = getConnection(url, "GET");
			responseBody = getResponseBody(conn);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new HandlableException(ErrorCode.HTTP_CONNEECT_ERROR, e);
		}
		
		return responseBody;
	}
	
	public JsonElement getAsJson(String url, Map<String, String> headers) {
		
		String responseBody = "";
		JsonElement datas = null;
		
		try {
			HttpURLConnection conn = getConnection(url, "GET");
			setHeaders(headers, conn);//외부로 부터 전달받은 Http 헤더를 적용
			responseBody = getResponseBody(conn);
			datas = gson.fromJson(responseBody, JsonObject.class);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new HandlableException(ErrorCode.HTTP_CONNEECT_ERROR, e);
		}
		
		return datas;
	}
	
	public String post(String url,  Map<String, String> headers, String body) {
		String responseBody = "";
		
		try {
			HttpURLConnection conn = getConnection(url, "POST");
			setHeaders(headers, conn);
			setBody(body, conn);
			responseBody = getResponseBody(conn);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return responseBody.toString();
		
	}
	
	public String urlEncodedForm(RequestParams requestParams) {
		String res = "";
		Map<String, String> params = requestParams.getParams();
		
		try {
			for (String key : params.keySet()) {
				res += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(params.get(key), "UTF-8");
			}
			if(res.length() > 0) {
				res = res.substring(1);
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return res;
	}
	
	private HttpURLConnection getConnection(String url, String method) throws IOException {
		URL u = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)u.openConnection();
		conn.setRequestMethod(method);
		
		//POST 방식을 경우 HttpURLConnection의 출력스트림 사용여부가 true로 지정이 되어있어야한다.
		//	넘어온 방식이 POST라면 DoOutput의 값을 true로 바꿔주어 출력스트림도 사용할거다라고 명시해주어야함.
		if(method.equals("POST")) {
			conn.setDoOutput(true);
		}
		
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		
		return conn;
	}
	
	private String getResponseBody(HttpURLConnection conn) throws IOException {

		StringBuffer responseBody = new StringBuffer();
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));){
			
			String line = null;
			
			while((line = br.readLine()) != null) {
				responseBody.append(line);
			}
		}
		
		return responseBody.toString();
	}
	
	private void setHeaders(Map<String, String> headers, HttpURLConnection conn) {
		
		for (String key : headers.keySet()) {
			conn.setRequestProperty(key, headers.get(key));
		}
		
	}
	
	private void setBody(String body, HttpURLConnection conn) throws IOException {
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));){
			bw.write(body);
			bw.flush();
		}
	}
		
}
