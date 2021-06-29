package com.callor.book.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.stereotype.Service;

import com.callor.book.config.NaverSecret;
import com.callor.book.model.BookDTO;
import com.callor.book.service.NaverService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NaverServiceImplV1 implements NaverService<BookDTO>{
	
	/*
	 * naver에 요청하기
	 * BookURL + "?query=" + 검색문자열
	 * 
	 */
	public String queryURL(String search) {
		
		// 검색하고자 하는 문자열을 UTF-8로 인코딩
		String searchUTF8 = null;
		try {
			 searchUTF8 = URLEncoder.encode(search,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringBuilder queryURL = new StringBuilder();
		queryURL.append(NaverSecret.NURL.BOOK); // queryString += BookURL
		
		String queryString = String.format("?query=%s",searchUTF8);
		queryURL.append(queryString);
		queryString = String.format("&display=%d", 20);
		queryURL.append(queryString);
		log.debug("queryURL {} ", queryURL.toString());
		return queryURL.toString();
	}

	/*
	 * queryURL을 naver에 전송하고 naver로 부터 결과를 받는 method
	 */
	@Override
	public String getJsonString(String queryURL) {

		// API를 통하여 다른 서버에 Request를 보낼때 사용할 객체
		URL url = null;
		
		// Http 프로콜을 통하여 다른 서버에 연결할때 사용할 객체
		HttpURLConnection httpConn = null;
		
		try {
			// queryURL 주소를 Request 정보로 변환
			url = new URL(queryURL);
			
			// 생성된 URL 정보를 사용하여 다른 서버에 연결
			httpConn = (HttpURLConnection) url.openConnection();
			
			// naver 가 어떤 응답을 할것인지를 미리 확인하는
			// 코드를 요청한다
			int httpStatusCode = httpConn.getResponseCode();

			// naver로 부터 데이터를 수신할 객체
			InputStreamReader is = null; 
			if(httpStatusCode == 200) {
				is = new InputStreamReader(httpConn.getInputStream());
			} else {
				is = new InputStreamReader(httpConn.getErrorStream());
			}

			// is를 buffer 에 연결
			BufferedReader buffer = null;
			buffer = new BufferedReader(is);
			
			/*
			 * StringBuilder, StringBuffer
			 * 
			 * String 형의 데이터를 += 처럼 
			 * 사용할때 발생하는 메모리 leak, 성능저하 
			 * 문제를 해결하기위하여 탄생된 클래스
			 * 
			 * String 형의 데이터를 += 하면
			 * 예) 다음과 같은 코드를 반복하면
			 * 		String str = "대한민국"
			 * 		str += "Korea"
			 * 		str += "Republic"
			 * 
			 * 내부적으로는 str 변수를 생성, 제거, 생성, 제거, 생성
			 * 하는 코드가 반복적으로 수행된다
			 * 
			 * 이러한 현상이 반복되면 메모리에 문제가 발생할수 있다
			 * 
			 * 그러한 문제를 해결하기 위하여 탄생한 클래스다
			 * 
			 * 겉으로 보기에는 두 클래스의 역할, 사용법이 똑같다
			 * 
			 * StringBuilder는 Sinlge Thread에서 최적화 되어 있다
			 * StringBuffer는 Multi Thread에서 safe 하다
			 */
			StringBuffer sBuffer = new StringBuffer();

			// 가져온 데이터를 읽어서 변수에 담기
			while(true) {
				String reader = buffer.readLine();
				if(reader == null) {
					break;
				}
				sBuffer.append(reader);
			}
			return sBuffer.toString();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		return null;
	}


	@Override
	public List<BookDTO> getNaverList(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}
}