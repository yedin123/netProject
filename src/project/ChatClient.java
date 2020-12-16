package project;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Point;

public class ChatClient extends JFrame implements ActionListener, Runnable, ListSelectionListener, MouseListener {

	static String serverAddress;// 서버 IP 주소
	static int portNumber;// 포트번호
	private ArrayList<String> FList;

	Socket socket;
	Scanner in;
	PrintWriter out;
	LoginUI loginUI;
	MemberProc memProc;
	FriendSearch frSearch;
	FriendList frList;
	EditProfile editpf;
	ChattingWindow chatting;
	String id = "";
	String userName;
	int pageCount = 0;
	int searchCount = 0;

	private boolean condition_Id = false; // ID 중복체크 변수
	private boolean condition_nName = false; // 닉네임 중복체크 변수

	/* severInfo.dat 파일에서 IP주소와 포트넘버를 읽어오는 메소드 */
	public static void info() {
		String fileName = "serverinfo.dat";
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName));
			serverAddress = inputStream.readUTF();
			portNumber = inputStream.readInt();
			inputStream.close();

		} catch (Exception e) {// 예외 발생시 기본값 할당
			serverAddress = "localhost";
			portNumber = 59001;
		}
	}

	/* Client 생성자 */
	public ChatClient() throws IOException {
		info();// IP, port number 정보 읽어옴
		network();// 서버와 연결
		loginUI = new LoginUI();// 로그인창 생성
		loginUI.frame.setVisible(true);
		memProc = new MemberProc();/// 회원가입창
		frList = new FriendList();// 친구목록창
		frSearch = new FriendSearch();// 친구검색창
		editpf = new EditProfile();
		event();

	}

	/* 소켓 프로그래밍 */
	public void network() {
		try {
			socket = new Socket("localhost", 59001);// 소켓 생성
			in = new Scanner(socket.getInputStream());// 소켓 입력 스트림 생성
			out = new PrintWriter(socket.getOutputStream(), true);// 소켓 출력 스트림 생성
		} catch (IOException e) {
			System.out.println("서버와 연결되지 않았습니다.");
			e.printStackTrace();
			System.exit(0);
		}
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		// optionpane 글꼴 설정
		UIManager.put("OptionPane.messageFont", new Font("함초롬돋움", Font.PLAIN, 12));
		UIManager.put("OptionPane.buttonFont", new Font("함초롬돋움", Font.PLAIN, 12));

		String line[] = null;
		while (true) {
			try {
				// 서버가 보낸 정보를 line 스트링에 저장
				line = in.nextLine().split("\\|");// '|'으로 나누어 line[0]에 프로토콜 정보가, line[1]에 나머지 정보가 담김
				if (line == null) {
					in.close();
					out.close();
					socket.close();
					System.exit(0);
				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_OK) == 0) { // 회원가입 ID 중복 안됨
					condition_Id = true;
					JOptionPane.showMessageDialog(this, "사용가능");
				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_NO) == 0) { // 회원가입 ID 중복 됨
					JOptionPane.showMessageDialog(this, "사용 불가능");
				} else if (line[0].compareTo(Protocol.NSEARCHCHECK_OK) == 0) { // 회원가입 ID 중복 안됨
					condition_nName = true;
					JOptionPane.showMessageDialog(this, "사용가능");
				} else if (line[0].compareTo(Protocol.NSEARCHCHECK_NO) == 0) { // 회원가입 ID 중복 됨
					JOptionPane.showMessageDialog(this, "사용 불가능");
				} else if (line[0].compareTo(Protocol.IDSEARCH_OK) == 0) { // ID 찾기 기존에 있음
					JOptionPane.showMessageDialog(this, line[1]);
					this.setVisible(true);
				} else if (line[0].compareTo(Protocol.IDSEARCH_NO) == 0) { // ID가 없음
					JOptionPane.showMessageDialog(this, line[1]);
					this.setVisible(true);
				} else if (line[0].compareTo(Protocol.ENTERLOGIN_OK) == 0) {// 로그인 성공
					this.setVisible(false);
					loginUI.frame.setVisible(false);// 로그인 화면 끔
					String info[] = line[1].split(",");// 로그인 정보

					userName = info[0];
					frList.usernName = info[0];
					frList.userMsg = info[1];
					JTableRefresh(line[3]);
					

					editpf.defaultusernName = info[0];
					editpf.defaultuserMsg = info[1];

					frList.initialize();
					frList.icon_exit.addMouseListener(this);
					frList.icon_search.addMouseListener(this);
					frList.lbl_edit.addMouseListener(this);
					frList.frame.setVisible(true);
					frList.menuChat.addActionListener(this);
					frList.menuProfile1.addActionListener(this);
					frList.menuProfile2.addActionListener(this);

				} else if (line[0].compareTo(Protocol.FRIENDLOGIN) == 0) {// 친구가 로그인
					System.out.println(line[1]);
					JTableRefresh(line[1]);
					refresh();
				} else if (line[0].compareTo(Protocol.ENTERLOGIN_NO) == 0) {// 로그인 실패
					JOptionPane.showMessageDialog(this, line[1]);
					System.out.println("로그인실패");
				} else if (line[0].compareTo(Protocol.EXIT) == 0) { // 로그아웃
					JTableRefresh(line[1]);
					refresh();
				} else if (line[0].compareTo(Protocol.TODAYMSG) == 0) { // 오늘의 한 마디 수정
					JTableRefresh(line[1]);
					refresh();
				} else if (line[0].compareTo(Protocol.ADDFRIEND) == 0) {// 친구추가
					JTableRefresh(line[1]);
					refresh();
				} else if (line[0].compareTo(Protocol.SEARCHFRIEND) == 0) {// 검색화면
					JTableRefresh2(line[1]);// id, nName, msg
					if (searchCount == 0) {
						frSearch.setTable();
						searchCount++;
						frSearch.table.addMouseListener(new MouseAdapter() {
							@Override
							public void mousePressed(MouseEvent e) {
								JTable table = (JTable) e.getSource();
								Point p = e.getPoint();
								int row = table.rowAtPoint(p);
								Object value = table.getValueAt(row, 0);
								Object friendnName = table.getValueAt(row, 1);
								if (e.getClickCount() == 2) {//친구추가 버튼
									if (friendnName.equals(userName)) {//나 자신을 친구추가했을떄
										JOptionPane.showMessageDialog(null, "나 자신은 영원한 인생의 친구입니다");
									} else {
										int result = JOptionPane.showConfirmDialog(null, "친구추가 하시겠습니까?", "친구추가",
												JOptionPane.YES_NO_OPTION);
										if (result == JOptionPane.OK_OPTION) {
											out.println(Protocol.ADDFRIEND + "|" + userName + "," + value);
											out.flush();
										}

									}

								}
							}
						});
					}frSearch.table.setModel(frSearch.model);
				} else if (line[0].compareTo(Protocol.SEARCHFRIEND_NO) == 0) {// 친구 검색 실패
					JOptionPane.showMessageDialog(null, "검색 결과가 없습니다");
				} else if (line[0].compareTo(Protocol.VIEWFRIENDPROFILE) == 0) {//프로필
					System.out.println("line[1] : " + line[1]);
					String info[] = line[1].split(",");// 친구 정보. id, 이름, 로그인 여부, 최종접속시간
					String state = "";
					if (info[2].equalsIgnoreCase("1")) {
						state = "온라인";
					} else {
						state = "오프라인";
					}
					JOptionPane.showMessageDialog(this,
							"id: " + info[0] + "\n이름: " + info[1] + "\n접속상태: " + state + "\n최종 접속시간: " + info[3]);
				} else if (line[0].compareTo(Protocol.CHATREQUEST) == 0) {// 채팅 요청받음
					String info[] = line[1].split(",");
					int result = JOptionPane.showConfirmDialog(null, info[0] + "님과 채팅을  하시겠습니까?", "채팅",
							JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.OK_OPTION) {//채팅 요청 승인
						out.println(Protocol.CHATREQUEST_OK + "|" + line[1]);
						out.flush();
						chatting = new ChattingWindow(userName, info[0]);
						chatting.btn_send.addActionListener(this);
						chatting.frame.addWindowListener(new java.awt.event.WindowAdapter() {
							@Override
							public void windowClosing(java.awt.event.WindowEvent windowEvent) {
								if (JOptionPane.showConfirmDialog(chatting.frame, "대화를 종료하시겠습니까?", "대화 종료",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
									out.println(Protocol.CHATEXIT + "|" + chatting.me + "," + chatting.opponent);
									out.flush();
									chatting.frame.dispose();
								}
							}
						});
						chatting.frame.setVisible(true);
					} else {//채팅 요청 거절
						out.println(Protocol.CHATREQUEST_NO + "|" + line[1]);
						out.flush();
					}

				} else if (line[0].compareTo(Protocol.CHATREQUEST_OK) == 0) {// 채팅 승인
					String info[] = line[1].split(",");
					chatting = new ChattingWindow(userName, info[1]);
					chatting.btn_send.addActionListener(this);
					chatting.frame.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(java.awt.event.WindowEvent windowEvent) {
							if (JOptionPane.showConfirmDialog(chatting.frame, "대화를 종료하시겠습니까?", "대화 종료",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
								out.println(Protocol.CHATEXIT + "|" + chatting.me + "," + chatting.opponent);
								out.flush();
								chatting.frame.dispose();
							}
						}
					});
					chatting.frame.setVisible(true);
				} else if (line[0].compareTo(Protocol.CHATREQUEST_NO) == 0) {// 채팅 거절
					JOptionPane.showMessageDialog(this, "채팅이 거절되었습니다.");
				} else if (line[0].compareTo(Protocol.CHATEXIT) == 0) {// 상대방이 채팅방을 떠났을 때
					chatting.frame.dispose();
					JOptionPane.showMessageDialog(this, line[1] + "님이 채팅을 나가셨습니다.");
				} else if (line[0].compareTo(Protocol.SENDMESSAGE_ACK) == 0) {// 상대방에게 채팅 받음
					chatting.addOpponentLog(line[1]);
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

	public void JTableRefresh(String line) {//friendList table 갱신

        String text[] = line.split(":");
        Vector data1 = new Vector();
        Vector data2 = new Vector();
        for (int i = 0; i < text.length; i++) {
            System.out.println("@" + text[i]);
        }

        Icon onlineIcon = new ImageIcon(getClass().getResource("/project/onlineicon.png"));
        Icon offlineIcon = new ImageIcon(getClass().getResource("/project/offlineicon.png"));

        for (int i = 0; i < text.length; i++) {
            String userInfo[] = text[i].split(",");
            if (userInfo[0].contains("1")) {
                Vector row = new Vector();
                row.add(onlineIcon);
                row.add(userInfo[1]);
                row.add(userInfo[2]);

                data1.add(row);
            } else if(userInfo[0].contains("0")){
                Vector row = new Vector();
                row.add(offlineIcon);
                row.add(userInfo[1]);
                row.add(userInfo[2]);

                data2.add(row);
            }

        }
        frList.row1 = data1;
        frList.row2 = data2;
        frList.header = frList.getColumn();
        frList.online = new DefaultTableModel(frList.row1, frList.header)
        {
            @Override
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        frList.offline = new DefaultTableModel(frList.row2, frList.header)
        {
            @Override
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };


    }

	public void JTableRefresh2(String line) {// friendSearch table 갱신
		String text[] = line.split(":");// text[0] = id,nName,msg
		Vector data = new Vector();
		for (int i = 0; i < text.length; i++) {
			String userInfo[] = text[i].split(",");
			Vector row = new Vector();
			row.add(userInfo[0]);// online
			row.add(userInfo[1]);// nName
			row.add(userInfo[2]);// msg

			data.add(row);

		}
		frSearch.row = data;
		frSearch.header = frSearch.getColumn();
		frSearch.model = new DefaultTableModel(frSearch.row, frSearch.header);
	}

	public void event() {//버튼 클릭 이벤트
		loginUI.btnLogIn.addActionListener(this);
		loginUI.btnSignIn.addActionListener(this);
		memProc.btnInsert.addActionListener(this);
		memProc.btnCancel.addActionListener(this);
		memProc.btnD.addActionListener(this);
		memProc.btnD2.addActionListener(this);
		editpf.btn_cancel.addActionListener(this);
		editpf.btn_deleteacct.addActionListener(this);
		editpf.btn_edit.addActionListener(this);

	}

	public static void main(String[] args) throws Exception {

		new ChatClient();// 객체 생성
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginUI.btnSignIn) { // 회원가입 버튼
			loginUI.frame.setVisible(false);
			memProc.frame.setVisible(true);
		} else if (e.getSource() == loginUI.btnLogIn) { // 로그인 버튼

			String id = loginUI.tfID.getText();
			String pw = loginUI.tfPW.getText();
			this.id = id;
			if (id.length() == 0 || pw.length() == 0) {// 빈칸이 있을시
				JOptionPane.showMessageDialog(this, "빈칸을 입력해주세요");
			} else {
				String dpwd = "";
				for (int i = 0; i < pw.length(); i++) {//비밀번호 암호화
					char ch = pw.charAt(i);
					ch = (char) (ch + 1);
					dpwd = dpwd + ch;
				}
				String line = id + "%" + dpwd;
				out.println(Protocol.ENTERLOGIN + "|" + line);
				out.flush();
			}
			loginUI.tfID.setText("");
			loginUI.tfPW.setText("");

		} else if (e.getSource() == memProc.btnInsert) { // 회원가입 화면 - 가입버튼 누르면

			String id = memProc.tfId.getText();
			String pwd = memProc.pfPw.getText();
			String nName = memProc.tfnName.getText();
			String name = memProc.tfName.getText();
			String email = memProc.tfEmail.getText();
			String birth1 = memProc.tfYear.getText();
			String birth2 = memProc.tfMonth.getText();
			String birth3 = memProc.tfDate.getText();
			String birth = birth1 + birth2 + birth3;

			if (name.length() == 0 || id.length() == 0 || pwd.length() == 0 || nName.length() == 0 || name.length() == 0
					|| email.length() == 0 || birth.length() == 0) {//빈칸
				JOptionPane.showMessageDialog(this, "빈칸을 입력해주세요");
			} else if (condition_Id && condition_nName) { // 중복확인

				String cpwd = "";
				for (int i = 0; i < pwd.length(); i++) {
					char ch = pwd.charAt(i);
					ch = (char) (ch + 1);
					cpwd = cpwd + ch;
				}

				String line = "";
				line += (id + "%" + cpwd + "%" + nName + "%" + name + "%" + email + "%" + birth);
				System.out.println(line);

				out.println(Protocol.REGISTER + "|" + line);
				out.flush();
				JOptionPane.showMessageDialog(this, "회원가입 완료");
				memProc.frame.setVisible(false);
				loginUI.frame.setVisible(true);
				memProc.tfId.setText("");
				memProc.pfPw.setText("");
				memProc.tfnName.setText("");
				memProc.tfName.setText("");
				memProc.tfEmail.setText("");
				memProc.tfYear.setText("");
				memProc.tfMonth.setText("");
				memProc.tfDate.setText("");

				condition_Id = false;
				condition_nName = false;
			} else if (!condition_Id) {
				JOptionPane.showMessageDialog(this, "ID 중복확인 해주세요");
			} else if (!condition_nName) {
				JOptionPane.showMessageDialog(this, "닉네임 중복확인 해주세요");
			}
		} else if (e.getSource() == memProc.btnCancel) {// 회원가입 화면 - 취소 버튼
			memProc.tfId.setText("");
			memProc.pfPw.setText("");
			memProc.tfnName.setText("");
			memProc.tfName.setText("");
			memProc.tfEmail.setText("");
			memProc.tfYear.setText("");
			memProc.tfMonth.setText("");
			memProc.tfDate.setText("");
			loginUI.frame.setVisible(true);
			memProc.frame.setVisible(false);
		} else if (e.getSource() == memProc.btnD) {// 회원가입 화면 - 아이디 중복
			// IDSEARCHCHECK
			String id = memProc.tfId.getText();
			out.println(Protocol.IDSEARCHCHECK + "|" + id);
			out.flush();

		} else if (e.getSource() == memProc.btnD2) {// 회원가입 화면 - 닉네임 중복

			String nName = memProc.tfnName.getText();
			out.println(Protocol.NSEARCHCHECK + "|" + nName);
			out.flush();

		} else if (e.getSource() == editpf.btn_edit) {// 프로필수정 화면 - 수정버튼
			String nName = editpf.tf_usernName.getText();
			String nMsg = editpf.tf_usermsg.getText();

			if (!(userName.compareTo(nName) == 0)) {//자신의 닉네임과 다를 때만 전송
				out.println(Protocol.NSEARCHCHECK + "|" + nName);
				out.flush();
			}

			if (userName.compareTo(nName) == 0 || condition_nName) {//현재 닉네임이거나 중복되지 않을 때
				out.println(Protocol.TODAYMSG + "|" + this.id + "," + nMsg + "," + nName);
				out.flush();
				System.out.println("Today's message entered.");
				frList.setTodayMsg(nMsg);
				frList.setTodaynName(nName);
				condition_nName = false;
			} else {
				JOptionPane.showMessageDialog(this, "중복된 닉네임 입니다.");
			}

		} else if (e.getSource() == editpf.btn_cancel) {// 프로필수정 화면 - 취소버튼
			editpf.frame.setVisible(false);
		} else if (e.getSource() == editpf.btn_deleteacct) {// 프로필수정 화면 - 탈퇴버튼
			int result = JOptionPane.showConfirmDialog(null, "탈퇴 하시겠습까?", "?", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				out.println(Protocol.DELETEMEMBER + "|" + userName);
				out.flush();
				frList.frame.setVisible(false);
				frSearch.frame.setVisible(false);
				loginUI.frame.setVisible(true);
				editpf.frame.setVisible(false);
				JOptionPane.showMessageDialog(this, "탈퇴 되었습니다");
			}
		} else if (e.getSource() == frList.menuProfile1) {// 온라인 친구 프로필 보기
			int row = frList.jTable_online.getSelectedRow();
			Object friendnName = frList.jTable_online.getValueAt(row, 1);
			System.out.println("nName : " + friendnName);
			out.println(Protocol.VIEWFRIENDPROFILE + "|" + friendnName);
			out.flush();
			// JOptionPane.showMessageDialog(this, "프로필");
		} else if (e.getSource() == frList.menuProfile2) { // 오프라인 친구 프로필 보기
			int row = frList.jTable_offline.getSelectedRow();
			Object friendnName = frList.jTable_offline.getValueAt(row, 1);
			System.out.println("nName : " + friendnName);
			out.println(Protocol.VIEWFRIENDPROFILE + "|" + friendnName);
			out.flush();
		} else if (e.getSource() == frList.menuChat) {// 채팅하기
			int row = frList.jTable_online.getSelectedRow();
			Object value = frList.jTable_online.getValueAt(row, 1);
			out.println(Protocol.CHATREQUEST + "|" + userName + "," + value);
			out.flush();
			JOptionPane.showMessageDialog(this, "채팅을 승인받는중입니다.");

		} else if (e.getSource() == frList.menuProfile1 || e.getSource() == frList.menuProfile2) {// 프로필 보기
			JOptionPane.showMessageDialog(this, "프로필");
		} else if (e.getSource() == chatting.btn_send) {// 메세지 전송
			String message = chatting.ta_mytextarea.getText();
			chatting.addLog(message);
			out.println(Protocol.SENDMESSAGE + "|" + chatting.opponent + "," + userName + " : " + message);
			out.flush();
			chatting.ta_mytextarea.setText("");
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == frList.icon_exit || e.getSource() == frSearch.icon_exit) { // 로그아웃 버튼 눌렀을 때
			int result = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습까?", "로그아웃", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				out.println(Protocol.EXITWAITROOM + "|" + userName);
				out.flush();
				frList.frame.setVisible(false);
				frSearch.frame.setVisible(false);
				// new ChatClient();
				loginUI.frame.setVisible(true);
				JOptionPane.showMessageDialog(this, "로그아웃 되었습니다");
				return;

			}

		} else if (e.getSource() == frList.icon_search) {// search창으로 이동
			if (pageCount == 0) {
				frSearch.icon_friend.addMouseListener(this);
				frSearch.lblbtn_search.addMouseListener(this);
				frSearch.icon_exit.addMouseListener(this);
			}
			frSearch.frame.setVisible(true);
			frList.frame.setVisible(false);
			pageCount++;
		} else if (e.getSource() == frSearch.icon_friend) {// 친구목록창으로 이동

			// frSearch 초기화
			frSearch.lbl_result.setText("검색어를 입력하세요");
			frSearch.lbl_result.setVisible(true);
			frSearch.tf_search.setText("");
			// frSearch.table.setVisible(false);

			frSearch.frame.setVisible(false);

			frList.frame.setVisible(true);

		} else if (e.getSource() == frList.lbl_edit) {// 오늘의 한 마디 수정
			editpf.frame.setVisible(true);

		} else if (e.getSource() == frSearch.lblbtn_search) { // 친구검색에서 검색버튼 눌렀을때
			String searchKey = frSearch.tf_search.getText();
			if (searchKey.trim().isEmpty()) { // 빈칸으로 검색했을때
				JOptionPane.showMessageDialog(this, "검색어를 입력하세요.");
			} else {
				out.println(Protocol.SEARCHFRIEND + "|" + searchKey);
			}

		} 

	}// mouseClicked() end

	public void refresh() {//friendList 갱신 함수
		frList.jTable_online.setModel(frList.online);
		frList.jTable_offline.setModel(frList.offline);

		frList.jTable_online.getColumn("온라인").setPreferredWidth(15);
        frList.jTable_online.getColumn("닉네임").setPreferredWidth(35);
        frList.jTable_online.getColumn("한마디").setPreferredWidth(180);

        frList.jTable_offline.getColumn("온라인").setPreferredWidth(15);
        frList.jTable_offline.getColumn("닉네임").setPreferredWidth(35);
        frList.jTable_offline.getColumn("한마디").setPreferredWidth(180);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}