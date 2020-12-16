package project;

import java.awt.Font;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Handler extends Thread {

	private Scanner in;
	private PrintWriter out;
	private Socket socket;
	private Connection conn;
	private PreparedStatement pstmt;
	private Statement stmt;
	private ResultSet rs;
	
	private ArrayList<Handler> waitUserList; // 대기실사용자

	private DTO dto;
	private String online;
	private String time = getAccessTime();
	private String ip;
	private String port;

	public Handler(Socket socket, ArrayList<Handler> waitUserList, Connection conn)
			throws IOException {

		System.out.println("ready");
		this.socket = socket;
		this.waitUserList = waitUserList;
		this.conn = conn;
		dto = new DTO();

		ip = socket.getInetAddress().getHostAddress();
		port = Integer.toString(socket.getPort());
	}

	public void run() {
		// 데이터 입력받음 데이터파싱 -> 결과 실행해줘야함
		try {

			String[] line = null;
			in = new Scanner(socket.getInputStream());
			out = new PrintWriter(socket.getOutputStream(), true);
			
			while (true) {
				
				if(in.hasNextLine()) {
				line = in.nextLine().split("\\|");
					if (line == null) {
						break;
					}
					
					// optionpane 글꼴 설정
					UIManager.put("OptionPane.messageFont", new Font("함초롬돋움", Font.PLAIN, 12));
					UIManager.put("OptionPane.buttonFont", new Font("함초롬돋움", Font.PLAIN, 12));
					
					/* REGISTER */
					if (line[0].compareTo(Protocol.REGISTER) == 0) {//회원가입
						String userContent[] = line[1].split("%");
	
						String sql = "insert into user(" + "online, id,pw,nName,name,email,birth) "
								+ "values(0,?,?,?,?,?,?)";
						pstmt = conn.prepareStatement(sql);
	
						pstmt.setString(1, userContent[0]);
						pstmt.setString(2, userContent[1]);
						pstmt.setString(3, userContent[2]);
						pstmt.setString(4, userContent[3]);
						pstmt.setString(5, userContent[4]);
						pstmt.setString(6, userContent[5]);
	
						int su = pstmt.executeUpdate();
						System.out.println(su + "회원가입[DB]");
	
					}
					/* IDSEARCHCHECK */
					else if (line[0].compareTo(Protocol.IDSEARCHCHECK) == 0) // 회원가입 ID 중복체크
					{
						String sql = "select * from user where id = '" + line[1] + "'";
						pstmt = conn.prepareStatement(sql);
						rs = pstmt.executeQuery(sql);
						String name = null;
						int count = 0;
						while (rs.next()) {
							name = rs.getString("id");
							if (name.compareTo(line[1]) == 0) {
								count++;
							}
						}
						System.out.println(count);
						if (count == 0) // 중복안되서 가입가능
						{
							out.println(Protocol.IDSEARCHCHECK_OK + "|" + "MESSAGE");
							out.flush();
						} else {
							out.println(Protocol.IDSEARCHCHECK_NO + "|" + "MESSAGE");
							out.flush();
						}
					} 
					else if (line[0].compareTo(Protocol.NSEARCHCHECK) == 0) // 회원가입 닉네임 중복체크
					{
						String sql = "select * from user where nName = '" + line[1] + "'";
						pstmt = conn.prepareStatement(sql);
						rs = pstmt.executeQuery(sql);
						String name = null;
						int count = 0;
						while (rs.next()) {
							name = rs.getString("nName");
							if (name.compareTo(line[1]) == 0) {
								count++;
							}
						}
						System.out.println(count);
						if (count == 0) // 중복안되서 가입가능
						{
							out.println(Protocol.NSEARCHCHECK_OK + "|" + "MESSAGE");
							out.flush();
						} else {
							out.println(Protocol.NSEARCHCHECK_NO + "|" + "MESSAGE");
							out.flush();
						}
					}
					/* ENTERLOGIN */
					else if (line[0].compareTo(Protocol.ENTERLOGIN) == 0) {//로그인
	
						boolean con = true; // 기존에 로그인되어있는지 안되어있는지 변수
						System.out.println("login");
						String userContent[] = line[1].split("%");
	
						System.out.println(userContent[0] + "/" + userContent[1]);
	
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getId()).compareTo(userContent[0]) == 0) {
								con = false;
							}
						}
						if (con) {
							int count =0;
							
							String sql = "select pw from user where id = '"+userContent[0]+"'";
							pstmt = conn.prepareStatement(sql);
							rs = pstmt.executeQuery(sql);
							String pw="";
							while (rs.next()) {
								pw = rs.getString(1);
								count++;
							}
							
							if(userContent[1].compareTo(pw)==0) {
							
								sql = "select * from user where id = '" + userContent[0] + "' and pw = '"
										+ userContent[1] + "'";
		
								pstmt = conn.prepareStatement(sql);
								ResultSet rs = pstmt.executeQuery(sql);
		
								while (rs.next()) {
									dto.setId(rs.getString("id"));
									dto.setPw(rs.getString("pw"));
									dto.setnName(rs.getString("nName"));
									dto.setName(rs.getString("name"));
									dto.setBirth(rs.getString("birth"));
									dto.setEmail(rs.getString("email"));
									dto.setMsg(rs.getString("msg"));
									dto.setOnline(rs.getString("online"));
									dto.setIp(rs.getString("ip"));
									dto.setTime(rs.getString("recent"));
									dto.setPort(rs.getString("port"));
								}
								// 로그인 되었을때
								updateUserInfo(dto);
		
								sql = "update user set online=1, recent=?, IP=?, port=? " + "where id = '" + userContent[0]
										+ "'";// 해당 id의 행에
		
								pstmt = conn.prepareStatement(sql);
								pstmt.setString(1, dto.getTime());
								pstmt.setString(2, dto.getIp());
								pstmt.setString(3, dto.getPort());
		
								int su = pstmt.executeUpdate();
		
								waitUserList.add(this); // online 인원수 추가
								String userline = "";
		
								stmt = conn.createStatement();
								sql = "select online, nName, msg from user where id in (select friendID from friend where id =\""
										+ dto.getId() + "\")";
								rs = stmt.executeQuery(sql);
								int count1 = 0;
									while (rs.next()) {
									String online = rs.getString(1);
									userline += online + ",";
									String nName = rs.getString(2);
									userline += nName + ",";
									String msg = rs.getString(3);
									userline += msg + ":";
									count1++;
								}
								if (count1 == 0)
									userline = " , , :";
								System.out.println(userline);
								out.println(Protocol.ENTERLOGIN_OK + "|" + dto.getnName() + "," + dto.getMsg()
										+ "|님이 입장하였습니다.| " + userline);
								out.flush();
		
								sql = "select id from friend where friendID = '" + dto.getId() + "'";
								rs = stmt.executeQuery(sql);
		
								while (rs.next()) {// 나를 친구로 둔 사람의 table 업데이트
									String id = rs.getString(1);
									for (int i = 0; i < waitUserList.size(); i++) {
											if ((waitUserList.get(i).dto.getId()).compareTo(id) == 0) {// 나를 친구로 둔 사람을
																									// waitList에서 찾는다
											String friendLine = "";
											sql = "select online, nName, msg from user where id in (select friendID from friend where id =\""
													+ waitUserList.get(i).dto.getId() + "\")";
											rs = stmt.executeQuery(sql);
											while (rs.next()) {
												String online = rs.getString(1);
												friendLine += online + ",";
												String nName = rs.getString(2);
												friendLine += nName + ",";
												String msg = rs.getString(3);
												friendLine += msg + ":";
											}
											waitUserList.get(i).out.println(Protocol.FRIENDLOGIN + "|" + friendLine);// 업데이트된
																														// 친구목록
																														// 전송
											waitUserList.get(i).out.flush();
										}
									}
								}
								
								System.out.println("[online 인원수] :" + waitUserList.size());
								System.out.println(dto.toString());
							} else if (count == 0) {
								out.println(Protocol.ENTERLOGIN_NO + "|" + "존재하지 않는 아이디 입니다.");
								out.flush();
							} else {
								out.println(Protocol.ENTERLOGIN_NO + "|" + "비밀번호를 다시 입력해주세요.");
								out.flush();
							}
						} else {// con = false인 경우
							out.println(Protocol.ENTERLOGIN_NO + "|" + "이미 로그인 중입니다.");
							out.flush();
						}
					
					}
					/* Log out */
					else if (line[0].compareTo(Protocol.EXITWAITROOM) == 0) {//로그아웃
					
						// 나간시간 할까말까
						String sql = "update user set online= 0 " + "where nName = '" + line[1] + "'";// 해당 닉네임의 행에
	
						pstmt = conn.prepareStatement(sql);
						int su = pstmt.executeUpdate();
	
						int index;
						for (int i = 0; i < waitUserList.size(); i++) {// 나간 사람 waitList에서 제거
							if ((waitUserList.get(i).dto.getnName()).compareTo(line[1]) == 0) {
								System.out.println("[퇴장] :" + waitUserList.get(i).dto.getnName());
								index = i;
								waitUserList.remove(index);
							}
						}
	
						String userline = "";
	
						stmt = conn.createStatement();
	
						sql = "select id from friend where friendID = '" + dto.getId() + "'";
						rs = stmt.executeQuery(sql);
	
						while (rs.next()) {// 나를 친구로 둔 사람의 table 업데이트
							String id = rs.getString(1);
							for (int i = 0; i < waitUserList.size(); i++) {
								if ((waitUserList.get(i).dto.getId()).compareTo(id) == 0) {// 나를 친구로 둔 사람을 waitList에서 찾는다
									sql = "select online, nName, msg from user where id in (select friendID from friend where id =\""
											+ waitUserList.get(i).dto.getId() + "\")";
									rs = stmt.executeQuery(sql);
									while (rs.next()) {
										String online = rs.getString(1);
										userline += online + ",";
										String nName = rs.getString(2);
										userline += nName + ",";
										String msg = rs.getString(3);
										userline += msg + ":";
									}
									waitUserList.get(i).out.println(Protocol.EXIT + "|" + userline);// 업데이트된 친구목록 전송
									waitUserList.get(i).out.flush();
								}
							}
						}
					}
	
					/* ADD FRIEND */
					else if (line[0].compareTo(Protocol.ADDFRIEND) == 0) {//친구추가
						String userContent[] = line[1].split(",");// userContent[0]: 자기 닉네임, userContent[1]: 친구 아이디
						String id="";
						String sql = "Select id from user where nName= '"+userContent[0]+"'";//자기 아이디 가져옴
						rs = stmt.executeQuery(sql);
						while (rs.next()) {
							id = rs.getString(1);
						}
						
						sql = "insert into friend(" + "id, friendID) " + "values(?,?)";//친구DB에 추가
						pstmt = conn.prepareStatement(sql);
	
						pstmt.setString(1, id);
						pstmt.setString(2, userContent[1]);
						
						try {
	                        pstmt.executeUpdate();
	                        JOptionPane.showMessageDialog(null, "친구추가 완료");
	                    } catch (SQLIntegrityConstraintViolationException e) {
	                        JOptionPane.showMessageDialog(null, "이미 추가된 친구입니다");
	                    }
	
						String friendLine = "";
	
						stmt = conn.createStatement();
						sql = "select online, nName, msg from user where id in (select friendID from friend where id =\""
								+ id + "\")";
						rs = stmt.executeQuery(sql);
						int count1 = 0;
						while (rs.next()) {
							String online = rs.getString(1);
							friendLine += online + ",";
							String nName = rs.getString(2);
							friendLine += nName + ",";
							String msg = rs.getString(3);
							friendLine += msg + ":";
							count1++;
						}
						if (count1 == 0)
							friendLine = " , , :";
	
						out.println(Protocol.ADDFRIEND + "|" + friendLine);//자신의 친구목록 전송
						out.flush();
	
					} else if (line[0].compareTo(Protocol.TODAYMSG) == 0) {
						String userContent[] = line[1].split(",");// userContent[0]: 자기 아이디, userContent[1]: 오늘의 한 마디,
																	// userContent[2]: 바뀐 닉네임
						String sql = "update user set msg=?, nName=? " + "where id = '" + userContent[0] + "'";// 해당 id의 행에
	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, userContent[1]);
						pstmt.setString(2, userContent[2]);
						pstmt.executeUpdate();
	
						sql = "select id from friend where friendID = '" + dto.getId() + "'";
						rs = stmt.executeQuery(sql);
	
						while (rs.next()) {// 나를 친구로 둔 사람의 table 업데이트
							String id = rs.getString(1);
							for (int i = 0; i < waitUserList.size(); i++) {
								if ((waitUserList.get(i).dto.getId()).compareTo(id) == 0) {// 나를 친구로 둔 사람을 waitList에서 찾는다
									String friendLine = "";
									sql = "select online, nName, msg from user where id in (select friendID from friend where id =\""
											+ waitUserList.get(i).dto.getId() + "\")";
									rs = stmt.executeQuery(sql);
									while (rs.next()) {
										String online = rs.getString(1);
										friendLine += online + ",";
										String nName = rs.getString(2);
										friendLine += nName + ",";
										String msg = rs.getString(3);
										friendLine += msg + ":";
									}
									waitUserList.get(i).out.println(Protocol.TODAYMSG + "|" + friendLine);// 업데이트된 친구목록 전송
									waitUserList.get(i).out.flush();
								}
							}
						}
	
					} else if (line[0].compareTo(Protocol.SEARCHFRIEND) == 0) {//친구 검색
						try {
							String result = "";
							
							String sql = "select id, nName, msg from user where id like '%" + line[1] + "%'"
									+ " or nName like '%" + line[1] + "%'";
							rs = stmt.executeQuery(sql);
							result = "";
							int count = 0;
							while (rs.next()) {
	
								String id = rs.getString(1);
								result += id + ",";
								String nName = rs.getString(2);
								result += nName + ",";
								String msg = rs.getString(3);
								result += msg + ":";
								count++;
							}
	
							if (count > 0) {
								out.println(Protocol.SEARCHFRIEND + "|" + result);
							}
	
							else if (count == 0) {//검색 결과 없을 때
								result = " , , :";
								out.println(Protocol.SEARCHFRIEND_NO + "|" + result);
							}
							
						} catch (Exception e) {
							System.out.println(e);
						}
	
					} else if (line[0].compareTo(Protocol.VIEWFRIENDPROFILE) == 0) {// 친구 프로필 보기
						// 정보 : id, 이름, 로그인 여부, 최종접속시간
						// id 추출
						String id = "";
						String result = "";
						String sql = "Select id from user where nName= '" + line[1] + "'";
						rs = stmt.executeQuery(sql);
						while (rs.next()) {
							id = rs.getString(1);
						}
						result += id + ",";
	
						sql = "select name, online, recent from user where id = '" + id + "'";
						rs = stmt.executeQuery(sql);
	
						while (rs.next()) {
							String name = rs.getString(1);
							result += name + ",";
							String online = rs.getString(2);
							result += online + ",";
							String recent = rs.getString(3);
							result += recent;
						}
						System.out.println("result : " + result);
						out.println(Protocol.VIEWFRIENDPROFILE + "|" + result);
						out.flush();
	
					} else if (line[0].compareTo(Protocol.CHATREQUEST) == 0) {//채팅 요청
						String info[] = line[1].split(",");
						System.out.println("info[0]: " + info[0] + " info[1]: " + info[1]);
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[1]) == 0) {// 요청받은 client에게 보냄
								waitUserList.get(i).out.println(Protocol.CHATREQUEST + "|" + line[1]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.CHATREQUEST_OK) == 0) {//채팅 승인
						String info[] = line[1].split(",");
						
						String sql = "insert into chatRoom(" + "requester, accepter) " + "values(?,?)";
						pstmt = conn.prepareStatement(sql);
	
						pstmt.setString(1, info[0]);
						pstmt.setString(2, info[1]);
						
						try {
	                        pstmt.executeUpdate();
	                    } catch (SQLIntegrityConstraintViolationException e) {
	                        JOptionPane.showMessageDialog(null, "채팅방 개설 실패.");
	                    }
						
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[0]) == 0) {// 요청받은 client에게 보냄
								waitUserList.get(i).out.println(Protocol.CHATREQUEST_OK + "|" + line[1]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.CHATREQUEST_NO) == 0) {//채팅 거절
						String info[] = line[1].split(",");
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[0]) == 0) {// 요청받은 client에게 보냄
								waitUserList.get(i).out.println(Protocol.CHATREQUEST_NO + "|" + line[1]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.CHATEXIT) == 0) {//상대방이 채팅방을 나갔을 때
						String info[] = line[1].split(",");
						
						String sql = "delete from chatRoom where (requester = ? and accepter = ?) or (requester = ? and accepter = ?)";
						pstmt = conn.prepareStatement(sql);
	
						pstmt.setString(1, info[1]);
						pstmt.setString(2, info[0]);
						pstmt.setString(3, info[0]);
						pstmt.setString(4, info[1]);
						
						try {
	                        pstmt.executeUpdate();
	                    } catch (SQLIntegrityConstraintViolationException e) {
	                        JOptionPane.showMessageDialog(null, "채팅방 삭제 실패.");
	                    }
						
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[1]) == 0) {
								waitUserList.get(i).out.println(Protocol.CHATEXIT + "|" + info[0]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.SENDMESSAGE) == 0) {//메세지 전송
						String info[] = line[1].split(",");
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[0]) == 0) {
								waitUserList.get(i).out.println(Protocol.SENDMESSAGE_ACK + "|" + info[1]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.DELETEMEMBER) == 0) {//회원탈퇴
						// line[1]: 탈퇴할 사람의 닉네임
						
						String id="";
						String sql = "Select id from user where nName= '"+line[1]+"'";//id가져옴
						rs = stmt.executeQuery(sql);
						
						while (rs.next()) {
							id = rs.getString(1);
						}
						
						int index;
						
						for (int i = 0; i < waitUserList.size(); i++) {// 탈퇴한 사람 waitList에서 제거
							if ((waitUserList.get(i).dto.getnName()).compareTo(line[1]) == 0) {
								System.out.println("[퇴장] :" + waitUserList.get(i).dto.getnName());
								index = i;
								waitUserList.remove(index);
							}
						}
	
						stmt = conn.createStatement();
	
						sql = "select id from friend where friendID = '" + id + "'";
						rs = stmt.executeQuery(sql);
						
						String fr_id="";
						
						while(rs.next()) {//나를 친구로 둔 사람들 아이디 얻어옴
							String temp = rs.getString(1);
							fr_id = fr_id + temp;
							fr_id = fr_id +",";
						}
						
						//friend 테이블에서 삭제
						sql = "delete from friend where id=? or friendID = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, id);
						pstmt.setString(2, id);
						pstmt.executeUpdate();
						
						String userline = "";
						
						sql = "delete from user where id= ?";//user 테이블에서 삭제
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, id);
						pstmt.executeUpdate();
						
						String info[] = fr_id.split(",");
						for(int i=0; i <info.length; i++) {//나를 친구로 둔 사람의 table 업데이트
							for(int j=0; j< waitUserList.size(); j++) {
								if ((waitUserList.get(j).dto.getId()).compareTo(info[i]) == 0) {
									sql = "select online, nName, msg from user where id in (select friendID from friend where id =\""
											+ waitUserList.get(j).dto.getId() + "\")";
									rs = stmt.executeQuery(sql);
									while (rs.next()) {
										String online = rs.getString(1);
										userline += online + ",";
										String nName = rs.getString(2);
										userline += nName + ",";
										String msg = rs.getString(3);
										userline += msg + ":";
									}
									waitUserList.get(j).out.println(Protocol.EXIT + "|" + userline);// 업데이트된 친구목록 전송
									waitUserList.get(j).out.flush();
								}
							}
						}
					}
				}
			} // while

			in.close();
			out.close();
			socket.close();

		} catch (IOException io) {
			io.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAccessTime() {//로그인 시간
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String accessTime = format.format(time);
		return accessTime;
	}

	public DTO updateUserInfo(DTO dto) {

		time = getAccessTime();
		// deliver to dto
		dto.setOnline(online);
		dto.setTime(time);
		dto.setIp(ip);
		dto.setPort(port);
		return dto;
	}

}
