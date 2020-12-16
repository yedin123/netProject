package project;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
/*동네 예보 조회*/
public class ApiExplorer {
	Object rainFall;
	Object humidity;
	Object sky = "";
	public ApiExplorer() throws IOException{
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "= Service Key"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("8", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*자료형식(JSON)*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20201215", "UTF-8")); /*조회 날짜. 2020년 11월 24일*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0500", "UTF-8")); /*조회 시간 5시*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("62", "UTF-8")); /*예보 지점 x 좌표*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("124", "UTF-8")); /*예보 지점 y 좌표*/
        urlBuilder.append("&" + URLEncoder.encode("filetype","UTF-8") + "=" + URLEncoder.encode("SHRT", "UTF-8")); /*예보 지점 y 좌표*/
        
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();//연결
        conn.setRequestMethod("GET");//GET 방식으로 전송
        conn.setRequestProperty("Content-type", "application/json");
        //System.out.println("[성남시 수정구 복정동 날씨 ]");
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {//리스펀스 코드가 200에서 300사이면
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));//읽어온다
        } else {//아니면
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));//에러를 읽어온다
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();//결과를 문자열로 저장
        
        /*Parsing*/
        JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(result);//문자열 데이터 객체화
			JSONObject parseResponse = (JSONObject) object.get("response");//response 키로 데이터 파싱
			JSONObject parseBody = (JSONObject) parseResponse.get("body");//response로부터 body 찾기
			JSONObject parseItems = (JSONObject)parseBody.get("items");//body로부터 items 찾기
			JSONArray parseItem = (JSONArray) parseItems.get("item");//items로부터 item찾기
			
			String category;
			JSONObject weather;
			String day = "";
			String time = "";
			
			for (int i = 0; i < parseItem.size(); i++) {
				weather = (JSONObject) parseItem.get(i);
				Object fcstValue = weather.get("fcstValue");
				Object fcstDate = weather.get("fcstDate");
				Object fcstTime = weather.get("fcstTime");
				
				category = (String)weather.get("category");
				
				if(!day.equals(fcstDate.toString())) {
					day = fcstDate.toString();//날짜 맞추기
				}
				if(!time.contentEquals(fcstTime.toString())) {//시간 맞추기
					time = fcstTime.toString();
				}
				//파싱한 데이터 출력
				switch(category) {
				case "POP":
					//System.out.print("강수확률: ");
					rainFall = fcstValue;
					//System.out.println(fcstValue + "%");
					break;
				case "REH": 
					//System.out.print("습도: ");
					humidity = fcstValue;
					//System.out.println(fcstValue + "%");
					break;
				case "SKY":
					//System.out.print("하늘상태: ");
					if(fcstValue.equals("1")) {
						sky = "맑음";
						//System.out.println("맑음");
					}
					else if(fcstValue.equals("3")) {
						sky = "구름 많음";
						//System.out.println("구름 많음");
					}
					else if (fcstValue.equals("4")) {
						sky = "흐림";
						//System.out.println("흐림");
					}
					break;

				}
				
			}
			
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
    
}