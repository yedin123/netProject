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

	static String serverAddress;// ���� IP �ּ�
	static int portNumber;// ��Ʈ��ȣ
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

	private boolean condition_Id = false; // ID �ߺ�üũ ����
	private boolean condition_nName = false; // �г��� �ߺ�üũ ����

	/* severInfo.dat ���Ͽ��� IP�ּҿ� ��Ʈ�ѹ��� �о���� �޼ҵ� */
	public static void info() {
		String fileName = "serverinfo.dat";
		try {
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName));
			serverAddress = inputStream.readUTF();
			portNumber = inputStream.readInt();
			inputStream.close();

		} catch (Exception e) {// ���� �߻��� �⺻�� �Ҵ�
			serverAddress = "localhost";
			portNumber = 59001;
		}
	}

	/* Client ������ */
	public ChatClient() throws IOException {
		info();// IP, port number ���� �о��
		network();// ������ ����
		loginUI = new LoginUI();// �α���â ����
		loginUI.frame.setVisible(true);
		memProc = new MemberProc();/// ȸ������â
		frList = new FriendList();// ģ�����â
		frSearch = new FriendSearch();// ģ���˻�â
		editpf = new EditProfile();
		event();

	}

	/* ���� ���α׷��� */
	public void network() {
		try {
			socket = new Socket("localhost", 59001);// ���� ����
			in = new Scanner(socket.getInputStream());// ���� �Է� ��Ʈ�� ����
			out = new PrintWriter(socket.getOutputStream(), true);// ���� ��� ��Ʈ�� ����
		} catch (IOException e) {
			System.out.println("������ ������� �ʾҽ��ϴ�.");
			e.printStackTrace();
			System.exit(0);
		}
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		// optionpane �۲� ����
		UIManager.put("OptionPane.messageFont", new Font("���ʷҵ���", Font.PLAIN, 12));
		UIManager.put("OptionPane.buttonFont", new Font("���ʷҵ���", Font.PLAIN, 12));

		String line[] = null;
		while (true) {
			try {
				// ������ ���� ������ line ��Ʈ���� ����
				line = in.nextLine().split("\\|");// '|'���� ������ line[0]�� �������� ������, line[1]�� ������ ������ ���
				if (line == null) {
					in.close();
					out.close();
					socket.close();
					System.exit(0);
				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_OK) == 0) { // ȸ������ ID �ߺ� �ȵ�
					condition_Id = true;
					JOptionPane.showMessageDialog(this, "��밡��");
				} else if (line[0].compareTo(Protocol.IDSEARCHCHECK_NO) == 0) { // ȸ������ ID �ߺ� ��
					JOptionPane.showMessageDialog(this, "��� �Ұ���");
				} else if (line[0].compareTo(Protocol.NSEARCHCHECK_OK) == 0) { // ȸ������ ID �ߺ� �ȵ�
					condition_nName = true;
					JOptionPane.showMessageDialog(this, "��밡��");
				} else if (line[0].compareTo(Protocol.NSEARCHCHECK_NO) == 0) { // ȸ������ ID �ߺ� ��
					JOptionPane.showMessageDialog(this, "��� �Ұ���");
				} else if (line[0].compareTo(Protocol.IDSEARCH_OK) == 0) { // ID ã�� ������ ����
					JOptionPane.showMessageDialog(this, line[1]);
					this.setVisible(true);
				} else if (line[0].compareTo(Protocol.IDSEARCH_NO) == 0) { // ID�� ����
					JOptionPane.showMessageDialog(this, line[1]);
					this.setVisible(true);
				} else if (line[0].compareTo(Protocol.ENTERLOGIN_OK) == 0) {// �α��� ����
					this.setVisible(false);
					loginUI.frame.setVisible(false);// �α��� ȭ�� ��
					String info[] = line[1].split(",");// �α��� ����

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

				} else if (line[0].compareTo(Protocol.FRIENDLOGIN) == 0) {// ģ���� �α���
					System.out.println(line[1]);
					JTableRefresh(line[1]);
					refresh();
				} else if (line[0].compareTo(Protocol.ENTERLOGIN_NO) == 0) {// �α��� ����
					JOptionPane.showMessageDialog(this, line[1]);
					System.out.println("�α��ν���");
				} else if (line[0].compareTo(Protocol.EXIT) == 0) { // �α׾ƿ�
					JTableRefresh(line[1]);
					refresh();
				} else if (line[0].compareTo(Protocol.TODAYMSG) == 0) { // ������ �� ���� ����
					JTableRefresh(line[1]);
					refresh();
				} else if (line[0].compareTo(Protocol.ADDFRIEND) == 0) {// ģ���߰�
					JTableRefresh(line[1]);
					refresh();
				} else if (line[0].compareTo(Protocol.SEARCHFRIEND) == 0) {// �˻�ȭ��
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
								if (e.getClickCount() == 2) {//ģ���߰� ��ư
									if (friendnName.equals(userName)) {//�� �ڽ��� ģ���߰�������
										JOptionPane.showMessageDialog(null, "�� �ڽ��� ������ �λ��� ģ���Դϴ�");
									} else {
										int result = JOptionPane.showConfirmDialog(null, "ģ���߰� �Ͻðڽ��ϱ�?", "ģ���߰�",
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
				} else if (line[0].compareTo(Protocol.SEARCHFRIEND_NO) == 0) {// ģ�� �˻� ����
					JOptionPane.showMessageDialog(null, "�˻� ����� �����ϴ�");
				} else if (line[0].compareTo(Protocol.VIEWFRIENDPROFILE) == 0) {//������
					System.out.println("line[1] : " + line[1]);
					String info[] = line[1].split(",");// ģ�� ����. id, �̸�, �α��� ����, �������ӽð�
					String state = "";
					if (info[2].equalsIgnoreCase("1")) {
						state = "�¶���";
					} else {
						state = "��������";
					}
					JOptionPane.showMessageDialog(this,
							"id: " + info[0] + "\n�̸�: " + info[1] + "\n���ӻ���: " + state + "\n���� ���ӽð�: " + info[3]);
				} else if (line[0].compareTo(Protocol.CHATREQUEST) == 0) {// ä�� ��û����
					String info[] = line[1].split(",");
					int result = JOptionPane.showConfirmDialog(null, info[0] + "�԰� ä����  �Ͻðڽ��ϱ�?", "ä��",
							JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.OK_OPTION) {//ä�� ��û ����
						out.println(Protocol.CHATREQUEST_OK + "|" + line[1]);
						out.flush();
						chatting = new ChattingWindow(userName, info[0]);
						chatting.btn_send.addActionListener(this);
						chatting.frame.addWindowListener(new java.awt.event.WindowAdapter() {
							@Override
							public void windowClosing(java.awt.event.WindowEvent windowEvent) {
								if (JOptionPane.showConfirmDialog(chatting.frame, "��ȭ�� �����Ͻðڽ��ϱ�?", "��ȭ ����",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
									out.println(Protocol.CHATEXIT + "|" + chatting.me + "," + chatting.opponent);
									out.flush();
									chatting.frame.dispose();
								}
							}
						});
						chatting.frame.setVisible(true);
					} else {//ä�� ��û ����
						out.println(Protocol.CHATREQUEST_NO + "|" + line[1]);
						out.flush();
					}

				} else if (line[0].compareTo(Protocol.CHATREQUEST_OK) == 0) {// ä�� ����
					String info[] = line[1].split(",");
					chatting = new ChattingWindow(userName, info[1]);
					chatting.btn_send.addActionListener(this);
					chatting.frame.addWindowListener(new java.awt.event.WindowAdapter() {
						@Override
						public void windowClosing(java.awt.event.WindowEvent windowEvent) {
							if (JOptionPane.showConfirmDialog(chatting.frame, "��ȭ�� �����Ͻðڽ��ϱ�?", "��ȭ ����",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
								out.println(Protocol.CHATEXIT + "|" + chatting.me + "," + chatting.opponent);
								out.flush();
								chatting.frame.dispose();
							}
						}
					});
					chatting.frame.setVisible(true);
				} else if (line[0].compareTo(Protocol.CHATREQUEST_NO) == 0) {// ä�� ����
					JOptionPane.showMessageDialog(this, "ä���� �����Ǿ����ϴ�.");
				} else if (line[0].compareTo(Protocol.CHATEXIT) == 0) {// ������ ä�ù��� ������ ��
					chatting.frame.dispose();
					JOptionPane.showMessageDialog(this, line[1] + "���� ä���� �����̽��ϴ�.");
				} else if (line[0].compareTo(Protocol.SENDMESSAGE_ACK) == 0) {// ���濡�� ä�� ����
					chatting.addOpponentLog(line[1]);
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

	public void JTableRefresh(String line) {//friendList table ����

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

	public void JTableRefresh2(String line) {// friendSearch table ����
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

	public void event() {//��ư Ŭ�� �̺�Ʈ
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

		new ChatClient();// ��ü ����
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginUI.btnSignIn) { // ȸ������ ��ư
			loginUI.frame.setVisible(false);
			memProc.frame.setVisible(true);
		} else if (e.getSource() == loginUI.btnLogIn) { // �α��� ��ư

			String id = loginUI.tfID.getText();
			String pw = loginUI.tfPW.getText();
			this.id = id;
			if (id.length() == 0 || pw.length() == 0) {// ��ĭ�� ������
				JOptionPane.showMessageDialog(this, "��ĭ�� �Է����ּ���");
			} else {
				String dpwd = "";
				for (int i = 0; i < pw.length(); i++) {//��й�ȣ ��ȣȭ
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

		} else if (e.getSource() == memProc.btnInsert) { // ȸ������ ȭ�� - ���Թ�ư ������

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
					|| email.length() == 0 || birth.length() == 0) {//��ĭ
				JOptionPane.showMessageDialog(this, "��ĭ�� �Է����ּ���");
			} else if (condition_Id && condition_nName) { // �ߺ�Ȯ��

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
				JOptionPane.showMessageDialog(this, "ȸ������ �Ϸ�");
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
				JOptionPane.showMessageDialog(this, "ID �ߺ�Ȯ�� ���ּ���");
			} else if (!condition_nName) {
				JOptionPane.showMessageDialog(this, "�г��� �ߺ�Ȯ�� ���ּ���");
			}
		} else if (e.getSource() == memProc.btnCancel) {// ȸ������ ȭ�� - ��� ��ư
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
		} else if (e.getSource() == memProc.btnD) {// ȸ������ ȭ�� - ���̵� �ߺ�
			// IDSEARCHCHECK
			String id = memProc.tfId.getText();
			out.println(Protocol.IDSEARCHCHECK + "|" + id);
			out.flush();

		} else if (e.getSource() == memProc.btnD2) {// ȸ������ ȭ�� - �г��� �ߺ�

			String nName = memProc.tfnName.getText();
			out.println(Protocol.NSEARCHCHECK + "|" + nName);
			out.flush();

		} else if (e.getSource() == editpf.btn_edit) {// �����ʼ��� ȭ�� - ������ư
			String nName = editpf.tf_usernName.getText();
			String nMsg = editpf.tf_usermsg.getText();

			if (!(userName.compareTo(nName) == 0)) {//�ڽ��� �г��Ӱ� �ٸ� ���� ����
				out.println(Protocol.NSEARCHCHECK + "|" + nName);
				out.flush();
			}

			if (userName.compareTo(nName) == 0 || condition_nName) {//���� �г����̰ų� �ߺ����� ���� ��
				out.println(Protocol.TODAYMSG + "|" + this.id + "," + nMsg + "," + nName);
				out.flush();
				System.out.println("Today's message entered.");
				frList.setTodayMsg(nMsg);
				frList.setTodaynName(nName);
				condition_nName = false;
			} else {
				JOptionPane.showMessageDialog(this, "�ߺ��� �г��� �Դϴ�.");
			}

		} else if (e.getSource() == editpf.btn_cancel) {// �����ʼ��� ȭ�� - ��ҹ�ư
			editpf.frame.setVisible(false);
		} else if (e.getSource() == editpf.btn_deleteacct) {// �����ʼ��� ȭ�� - Ż���ư
			int result = JOptionPane.showConfirmDialog(null, "Ż�� �Ͻðڽ���?", "?", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				out.println(Protocol.DELETEMEMBER + "|" + userName);
				out.flush();
				frList.frame.setVisible(false);
				frSearch.frame.setVisible(false);
				loginUI.frame.setVisible(true);
				editpf.frame.setVisible(false);
				JOptionPane.showMessageDialog(this, "Ż�� �Ǿ����ϴ�");
			}
		} else if (e.getSource() == frList.menuProfile1) {// �¶��� ģ�� ������ ����
			int row = frList.jTable_online.getSelectedRow();
			Object friendnName = frList.jTable_online.getValueAt(row, 1);
			System.out.println("nName : " + friendnName);
			out.println(Protocol.VIEWFRIENDPROFILE + "|" + friendnName);
			out.flush();
			// JOptionPane.showMessageDialog(this, "������");
		} else if (e.getSource() == frList.menuProfile2) { // �������� ģ�� ������ ����
			int row = frList.jTable_offline.getSelectedRow();
			Object friendnName = frList.jTable_offline.getValueAt(row, 1);
			System.out.println("nName : " + friendnName);
			out.println(Protocol.VIEWFRIENDPROFILE + "|" + friendnName);
			out.flush();
		} else if (e.getSource() == frList.menuChat) {// ä���ϱ�
			int row = frList.jTable_online.getSelectedRow();
			Object value = frList.jTable_online.getValueAt(row, 1);
			out.println(Protocol.CHATREQUEST + "|" + userName + "," + value);
			out.flush();
			JOptionPane.showMessageDialog(this, "ä���� ���ι޴����Դϴ�.");

		} else if (e.getSource() == frList.menuProfile1 || e.getSource() == frList.menuProfile2) {// ������ ����
			JOptionPane.showMessageDialog(this, "������");
		} else if (e.getSource() == chatting.btn_send) {// �޼��� ����
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
		if (e.getSource() == frList.icon_exit || e.getSource() == frSearch.icon_exit) { // �α׾ƿ� ��ư ������ ��
			int result = JOptionPane.showConfirmDialog(null, "�α׾ƿ� �Ͻðڽ���?", "�α׾ƿ�", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				out.println(Protocol.EXITWAITROOM + "|" + userName);
				out.flush();
				frList.frame.setVisible(false);
				frSearch.frame.setVisible(false);
				// new ChatClient();
				loginUI.frame.setVisible(true);
				JOptionPane.showMessageDialog(this, "�α׾ƿ� �Ǿ����ϴ�");
				return;

			}

		} else if (e.getSource() == frList.icon_search) {// searchâ���� �̵�
			if (pageCount == 0) {
				frSearch.icon_friend.addMouseListener(this);
				frSearch.lblbtn_search.addMouseListener(this);
				frSearch.icon_exit.addMouseListener(this);
			}
			frSearch.frame.setVisible(true);
			frList.frame.setVisible(false);
			pageCount++;
		} else if (e.getSource() == frSearch.icon_friend) {// ģ�����â���� �̵�

			// frSearch �ʱ�ȭ
			frSearch.lbl_result.setText("�˻�� �Է��ϼ���");
			frSearch.lbl_result.setVisible(true);
			frSearch.tf_search.setText("");
			// frSearch.table.setVisible(false);

			frSearch.frame.setVisible(false);

			frList.frame.setVisible(true);

		} else if (e.getSource() == frList.lbl_edit) {// ������ �� ���� ����
			editpf.frame.setVisible(true);

		} else if (e.getSource() == frSearch.lblbtn_search) { // ģ���˻����� �˻���ư ��������
			String searchKey = frSearch.tf_search.getText();
			if (searchKey.trim().isEmpty()) { // ��ĭ���� �˻�������
				JOptionPane.showMessageDialog(this, "�˻�� �Է��ϼ���.");
			} else {
				out.println(Protocol.SEARCHFRIEND + "|" + searchKey);
			}

		} 

	}// mouseClicked() end

	public void refresh() {//friendList ���� �Լ�
		frList.jTable_online.setModel(frList.online);
		frList.jTable_offline.setModel(frList.offline);

		frList.jTable_online.getColumn("�¶���").setPreferredWidth(15);
        frList.jTable_online.getColumn("�г���").setPreferredWidth(35);
        frList.jTable_online.getColumn("�Ѹ���").setPreferredWidth(180);

        frList.jTable_offline.getColumn("�¶���").setPreferredWidth(15);
        frList.jTable_offline.getColumn("�г���").setPreferredWidth(35);
        frList.jTable_offline.getColumn("�Ѹ���").setPreferredWidth(180);
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