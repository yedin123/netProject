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
	
	private ArrayList<Handler> waitUserList; // ���ǻ����

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
		// ������ �Է¹��� �������Ľ� -> ��� �����������
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
					
					// optionpane �۲� ����
					UIManager.put("OptionPane.messageFont", new Font("���ʷҵ���", Font.PLAIN, 12));
					UIManager.put("OptionPane.buttonFont", new Font("���ʷҵ���", Font.PLAIN, 12));
					
					/* REGISTER */
					if (line[0].compareTo(Protocol.REGISTER) == 0) {//ȸ������
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
						System.out.println(su + "ȸ������[DB]");
	
					}
					/* IDSEARCHCHECK */
					else if (line[0].compareTo(Protocol.IDSEARCHCHECK) == 0) // ȸ������ ID �ߺ�üũ
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
						if (count == 0) // �ߺ��ȵǼ� ���԰���
						{
							out.println(Protocol.IDSEARCHCHECK_OK + "|" + "MESSAGE");
							out.flush();
						} else {
							out.println(Protocol.IDSEARCHCHECK_NO + "|" + "MESSAGE");
							out.flush();
						}
					} 
					else if (line[0].compareTo(Protocol.NSEARCHCHECK) == 0) // ȸ������ �г��� �ߺ�üũ
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
						if (count == 0) // �ߺ��ȵǼ� ���԰���
						{
							out.println(Protocol.NSEARCHCHECK_OK + "|" + "MESSAGE");
							out.flush();
						} else {
							out.println(Protocol.NSEARCHCHECK_NO + "|" + "MESSAGE");
							out.flush();
						}
					}
					/* ENTERLOGIN */
					else if (line[0].compareTo(Protocol.ENTERLOGIN) == 0) {//�α���
	
						boolean con = true; // ������ �α��εǾ��ִ��� �ȵǾ��ִ��� ����
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
								// �α��� �Ǿ�����
								updateUserInfo(dto);
		
								sql = "update user set online=1, recent=?, IP=?, port=? " + "where id = '" + userContent[0]
										+ "'";// �ش� id�� �࿡
		
								pstmt = conn.prepareStatement(sql);
								pstmt.setString(1, dto.getTime());
								pstmt.setString(2, dto.getIp());
								pstmt.setString(3, dto.getPort());
		
								int su = pstmt.executeUpdate();
		
								waitUserList.add(this); // online �ο��� �߰�
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
										+ "|���� �����Ͽ����ϴ�.| " + userline);
								out.flush();
		
								sql = "select id from friend where friendID = '" + dto.getId() + "'";
								rs = stmt.executeQuery(sql);
		
								while (rs.next()) {// ���� ģ���� �� ����� table ������Ʈ
									String id = rs.getString(1);
									for (int i = 0; i < waitUserList.size(); i++) {
											if ((waitUserList.get(i).dto.getId()).compareTo(id) == 0) {// ���� ģ���� �� �����
																									// waitList���� ã�´�
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
											waitUserList.get(i).out.println(Protocol.FRIENDLOGIN + "|" + friendLine);// ������Ʈ��
																														// ģ�����
																														// ����
											waitUserList.get(i).out.flush();
										}
									}
								}
								
								System.out.println("[online �ο���] :" + waitUserList.size());
								System.out.println(dto.toString());
							} else if (count == 0) {
								out.println(Protocol.ENTERLOGIN_NO + "|" + "�������� �ʴ� ���̵� �Դϴ�.");
								out.flush();
							} else {
								out.println(Protocol.ENTERLOGIN_NO + "|" + "��й�ȣ�� �ٽ� �Է����ּ���.");
								out.flush();
							}
						} else {// con = false�� ���
							out.println(Protocol.ENTERLOGIN_NO + "|" + "�̹� �α��� ���Դϴ�.");
							out.flush();
						}
					
					}
					/* Log out */
					else if (line[0].compareTo(Protocol.EXITWAITROOM) == 0) {//�α׾ƿ�
					
						// �����ð� �ұ��
						String sql = "update user set online= 0 " + "where nName = '" + line[1] + "'";// �ش� �г����� �࿡
	
						pstmt = conn.prepareStatement(sql);
						int su = pstmt.executeUpdate();
	
						int index;
						for (int i = 0; i < waitUserList.size(); i++) {// ���� ��� waitList���� ����
							if ((waitUserList.get(i).dto.getnName()).compareTo(line[1]) == 0) {
								System.out.println("[����] :" + waitUserList.get(i).dto.getnName());
								index = i;
								waitUserList.remove(index);
							}
						}
	
						String userline = "";
	
						stmt = conn.createStatement();
	
						sql = "select id from friend where friendID = '" + dto.getId() + "'";
						rs = stmt.executeQuery(sql);
	
						while (rs.next()) {// ���� ģ���� �� ����� table ������Ʈ
							String id = rs.getString(1);
							for (int i = 0; i < waitUserList.size(); i++) {
								if ((waitUserList.get(i).dto.getId()).compareTo(id) == 0) {// ���� ģ���� �� ����� waitList���� ã�´�
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
									waitUserList.get(i).out.println(Protocol.EXIT + "|" + userline);// ������Ʈ�� ģ����� ����
									waitUserList.get(i).out.flush();
								}
							}
						}
					}
	
					/* ADD FRIEND */
					else if (line[0].compareTo(Protocol.ADDFRIEND) == 0) {//ģ���߰�
						String userContent[] = line[1].split(",");// userContent[0]: �ڱ� �г���, userContent[1]: ģ�� ���̵�
						String id="";
						String sql = "Select id from user where nName= '"+userContent[0]+"'";//�ڱ� ���̵� ������
						rs = stmt.executeQuery(sql);
						while (rs.next()) {
							id = rs.getString(1);
						}
						
						sql = "insert into friend(" + "id, friendID) " + "values(?,?)";//ģ��DB�� �߰�
						pstmt = conn.prepareStatement(sql);
	
						pstmt.setString(1, id);
						pstmt.setString(2, userContent[1]);
						
						try {
	                        pstmt.executeUpdate();
	                        JOptionPane.showMessageDialog(null, "ģ���߰� �Ϸ�");
	                    } catch (SQLIntegrityConstraintViolationException e) {
	                        JOptionPane.showMessageDialog(null, "�̹� �߰��� ģ���Դϴ�");
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
	
						out.println(Protocol.ADDFRIEND + "|" + friendLine);//�ڽ��� ģ����� ����
						out.flush();
	
					} else if (line[0].compareTo(Protocol.TODAYMSG) == 0) {
						String userContent[] = line[1].split(",");// userContent[0]: �ڱ� ���̵�, userContent[1]: ������ �� ����,
																	// userContent[2]: �ٲ� �г���
						String sql = "update user set msg=?, nName=? " + "where id = '" + userContent[0] + "'";// �ش� id�� �࿡
	
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, userContent[1]);
						pstmt.setString(2, userContent[2]);
						pstmt.executeUpdate();
	
						sql = "select id from friend where friendID = '" + dto.getId() + "'";
						rs = stmt.executeQuery(sql);
	
						while (rs.next()) {// ���� ģ���� �� ����� table ������Ʈ
							String id = rs.getString(1);
							for (int i = 0; i < waitUserList.size(); i++) {
								if ((waitUserList.get(i).dto.getId()).compareTo(id) == 0) {// ���� ģ���� �� ����� waitList���� ã�´�
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
									waitUserList.get(i).out.println(Protocol.TODAYMSG + "|" + friendLine);// ������Ʈ�� ģ����� ����
									waitUserList.get(i).out.flush();
								}
							}
						}
	
					} else if (line[0].compareTo(Protocol.SEARCHFRIEND) == 0) {//ģ�� �˻�
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
	
							else if (count == 0) {//�˻� ��� ���� ��
								result = " , , :";
								out.println(Protocol.SEARCHFRIEND_NO + "|" + result);
							}
							
						} catch (Exception e) {
							System.out.println(e);
						}
	
					} else if (line[0].compareTo(Protocol.VIEWFRIENDPROFILE) == 0) {// ģ�� ������ ����
						// ���� : id, �̸�, �α��� ����, �������ӽð�
						// id ����
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
	
					} else if (line[0].compareTo(Protocol.CHATREQUEST) == 0) {//ä�� ��û
						String info[] = line[1].split(",");
						System.out.println("info[0]: " + info[0] + " info[1]: " + info[1]);
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[1]) == 0) {// ��û���� client���� ����
								waitUserList.get(i).out.println(Protocol.CHATREQUEST + "|" + line[1]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.CHATREQUEST_OK) == 0) {//ä�� ����
						String info[] = line[1].split(",");
						
						String sql = "insert into chatRoom(" + "requester, accepter) " + "values(?,?)";
						pstmt = conn.prepareStatement(sql);
	
						pstmt.setString(1, info[0]);
						pstmt.setString(2, info[1]);
						
						try {
	                        pstmt.executeUpdate();
	                    } catch (SQLIntegrityConstraintViolationException e) {
	                        JOptionPane.showMessageDialog(null, "ä�ù� ���� ����.");
	                    }
						
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[0]) == 0) {// ��û���� client���� ����
								waitUserList.get(i).out.println(Protocol.CHATREQUEST_OK + "|" + line[1]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.CHATREQUEST_NO) == 0) {//ä�� ����
						String info[] = line[1].split(",");
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[0]) == 0) {// ��û���� client���� ����
								waitUserList.get(i).out.println(Protocol.CHATREQUEST_NO + "|" + line[1]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.CHATEXIT) == 0) {//������ ä�ù��� ������ ��
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
	                        JOptionPane.showMessageDialog(null, "ä�ù� ���� ����.");
	                    }
						
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[1]) == 0) {
								waitUserList.get(i).out.println(Protocol.CHATEXIT + "|" + info[0]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.SENDMESSAGE) == 0) {//�޼��� ����
						String info[] = line[1].split(",");
						for (int i = 0; i < waitUserList.size(); i++) {
							if ((waitUserList.get(i).dto.getnName()).compareTo(info[0]) == 0) {
								waitUserList.get(i).out.println(Protocol.SENDMESSAGE_ACK + "|" + info[1]);
								waitUserList.get(i).out.flush();
							}
						}
					} else if (line[0].compareTo(Protocol.DELETEMEMBER) == 0) {//ȸ��Ż��
						// line[1]: Ż���� ����� �г���
						
						String id="";
						String sql = "Select id from user where nName= '"+line[1]+"'";//id������
						rs = stmt.executeQuery(sql);
						
						while (rs.next()) {
							id = rs.getString(1);
						}
						
						int index;
						
						for (int i = 0; i < waitUserList.size(); i++) {// Ż���� ��� waitList���� ����
							if ((waitUserList.get(i).dto.getnName()).compareTo(line[1]) == 0) {
								System.out.println("[����] :" + waitUserList.get(i).dto.getnName());
								index = i;
								waitUserList.remove(index);
							}
						}
	
						stmt = conn.createStatement();
	
						sql = "select id from friend where friendID = '" + id + "'";
						rs = stmt.executeQuery(sql);
						
						String fr_id="";
						
						while(rs.next()) {//���� ģ���� �� ����� ���̵� ����
							String temp = rs.getString(1);
							fr_id = fr_id + temp;
							fr_id = fr_id +",";
						}
						
						//friend ���̺��� ����
						sql = "delete from friend where id=? or friendID = ?";
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, id);
						pstmt.setString(2, id);
						pstmt.executeUpdate();
						
						String userline = "";
						
						sql = "delete from user where id= ?";//user ���̺��� ����
						pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, id);
						pstmt.executeUpdate();
						
						String info[] = fr_id.split(",");
						for(int i=0; i <info.length; i++) {//���� ģ���� �� ����� table ������Ʈ
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
									waitUserList.get(j).out.println(Protocol.EXIT + "|" + userline);// ������Ʈ�� ģ����� ����
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

	public String getAccessTime() {//�α��� �ð�
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
