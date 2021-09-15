package test.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;
import com.kh.toy.member.model.dto.User;

@WebServlet("/test/http/*")
public class HttpConnectorTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HttpConnectorTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uriArr = request.getRequestURI().split("/");
		
		switch (uriArr[uriArr.length-1]) {
		//get
		case "test-kakao":
			testKakaoAPI();
			break;
		//post
		case "test-naver":
			testNaverAPI();
			break;
		default: throw new PageNotFoundException();
		}
		
	}
	
	private void testNaverAPI() throws ServletException, IOException {
		User user = User.builder().userId("pclass").password("1234").email("ab@Wad").tell("0101001").build();
		System.out.println(user);
		
		
		Gson gson = new Gson();
		
		
		//넘겨줄 body안의 내용 작성
		
		//Map에 String : Object이런 식으로 들어가야한다.
		// 문제는 body안의 keywordsGroups이다.
		Map<String,Object> body = new LinkedHashMap<String, Object>();
		//body에 넘겨줘야할 keywordGroups을 위해 타입과 제네릭을 아래처럼 작성
		Map<String,Object> keywordGroup = new LinkedHashMap<String,Object>();
		List<Map<String,Object>> keywordGroups = new ArrayList<Map<String,Object>>();
		List<String> keywords = new ArrayList<String>();
		
		keywords.add("GIT");
		keywords.add("GITHUB");
		
		keywordGroup.put("groupName", "자바");
		keywordGroup.put("keywords", keywords);
		
		keywordGroups.add(keywordGroup);
		
		keywordGroup = new LinkedHashMap<String,Object>();
		keywords = new ArrayList<String>();
		
		keywords.add("DJango");
		keywords.add("flutter");
		
		keywordGroup.put("groupName", "파이썬");
		keywordGroup.put("keywords", keywords);
		
		keywordGroups.add(keywordGroup);
		
		body.put("startDate", "2021-01-01");
		body.put("endDate", "2021-04-30");
		body.put("timeUnit", "month");
		body.put("keywordGroups", keywordGroups);
		
		String[] age = {"3","4","5","6"};
		
		body.put("age", age);
		
		//위에서 만들어진 body를 json형태로 변환
		String jsonDatas = gson.toJson(body).toString();
		System.out.println(jsonDatas);
		
		
		HttpConnector conn = new HttpConnector();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Naver-Client-Id", "01atEg112Z0MqrsNJHhh");
		headers.put("X-Naver-Client-Secret", "hJzk8V2S7i");
		headers.put("Content-Type", "application/json; charset=UTF-8");
		
		String responseBody = conn.post("https://openapi.naver.com/v1/datalab/search", headers, jsonDatas);
		System.out.println(responseBody);
		
	}

	private void testKakaoAPI() throws ServletException, IOException {
		HttpConnector conn = new HttpConnector();
		Map<String, String> params = new HashMap<String, String>();
		params.put("query", "자바");
		String queryString = conn.urlEncodedForm(RequestParams.builder().param("qeury", "자바").build());
		
		String url = "https://dapi.kakao.com/v3/search/book?"+queryString;
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "KakaoAK 3710091caa35a0588538dea25d8a23e1");
		
		JsonObject datas = conn.getAsJson(url, headers).getAsJsonObject();
		
		for (JsonElement e : datas.getAsJsonArray("documents")) {
			String author = e.getAsJsonObject().get("authors").getAsString();
			String title = e.getAsJsonObject().get("title").getAsString();
			System.out.println("작가 : " + author);
			System.out.println("제목 : " + title);
		}
		
		//json object는 Map(gson에서만든)으로 넘어온다.
		//json array는 ArrayList로 넘어온다.
		//json String은 String으로 넘어온다.
		//json Number는 double로 넘어온다.
		//json null은 null로 넘어온다.
		//json boolean은 boolean으로 넘어온다.
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
