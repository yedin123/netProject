package project;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
	private ServerSocket ss; // 서버 소켓
	private ArrayList<Handler> WaitUserList; // 대기실 사용자
	
	private Connection conn;
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost/test?&serverTimezone=UTC&useSSL=false";
	private String user = "root";
	private String password = "12345";
	
	public ChatServer() {

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password); // DB 연결

			ss = new ServerSocket(59001);
			System.out.println("서버준비완료");

			WaitUserList = new ArrayList<Handler>(); // 대기실 사용자
			while (true) {
				Socket socket = ss.accept();
				Handler handler = new Handler(socket, WaitUserList, conn);// 스레드 생성
				handler.start();
			} // while
		} catch (IOException io) {
			io.printStackTrace();		
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		new ChatServer();
	}
}