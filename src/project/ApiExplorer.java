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
/*���� ���� ��ȸ*/
public class ApiExplorer {
	Object rainFall;
	Object humidity;
	Object sky = "";
	public ApiExplorer() throws IOException{
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "= Service Key"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*��������ȣ*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("8", "UTF-8")); /*�� ������ ��� ��*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*�ڷ�����(JSON)*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20201215", "UTF-8")); /*��ȸ ��¥. 2020�� 11�� 24��*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0500", "UTF-8")); /*��ȸ �ð� 5��*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("62", "UTF-8")); /*���� ���� x ��ǥ*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("124", "UTF-8")); /*���� ���� y ��ǥ*/
        urlBuilder.append("&" + URLEncoder.encode("filetype","UTF-8") + "=" + URLEncoder.encode("SHRT", "UTF-8")); /*���� ���� y ��ǥ*/
        
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();//����
        conn.setRequestMethod("GET");//GET ������� ����
        conn.setRequestProperty("Content-type", "application/json");
        //System.out.println("[������ ������ ������ ���� ]");
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {//�����ݽ� �ڵ尡 200���� 300���̸�
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));//�о�´�
        } else {//�ƴϸ�
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));//������ �о�´�
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();//����� ���ڿ��� ����
        
        /*Parsing*/
        JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(result);//���ڿ� ������ ��üȭ
			JSONObject parseResponse = (JSONObject) object.get("response");//response Ű�� ������ �Ľ�
			JSONObject parseBody = (JSONObject) parseResponse.get("body");//response�κ��� body ã��
			JSONObject parseItems = (JSONObject)parseBody.get("items");//body�κ��� items ã��
			JSONArray parseItem = (JSONArray) parseItems.get("item");//items�κ��� itemã��
			
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
					day = fcstDate.toString();//��¥ ���߱�
				}
				if(!time.contentEquals(fcstTime.toString())) {//�ð� ���߱�
					time = fcstTime.toString();
				}
				//�Ľ��� ������ ���
				switch(category) {
				case "POP":
					//System.out.print("����Ȯ��: ");
					rainFall = fcstValue;
					//System.out.println(fcstValue + "%");
					break;
				case "REH": 
					//System.out.print("����: ");
					humidity = fcstValue;
					//System.out.println(fcstValue + "%");
					break;
				case "SKY":
					//System.out.print("�ϴû���: ");
					if(fcstValue.equals("1")) {
						sky = "����";
						//System.out.println("����");
					}
					else if(fcstValue.equals("3")) {
						sky = "���� ����";
						//System.out.println("���� ����");
					}
					else if (fcstValue.equals("4")) {
						sky = "�帲";
						//System.out.println("�帲");
					}
					break;

				}
				
			}
			
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
    
}