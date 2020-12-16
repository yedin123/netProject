package project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.StyleConstants;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class ChattingWindow extends JFrame implements MouseListener, ActionListener {

	JFrame frame;
	JTextArea ta_all_opp;// 전체 textarea
	JTextArea ta_mytextarea;// 나의 textarea
	JTextArea ta_all_me;
	Color info_bg = new Color(194, 163, 133);
	Color all_bg = new Color(255, 242, 204);
	Color send_bg = new Color(194, 163, 133);
	Color my_bg = Color.WHITE;
	static String me;
	static String opponent;
	JButton btn_send;

	/**
	 * Create the application.
	 */
	public ChattingWindow(String me, String oppnent) {
		this.me = me;
		this.opponent = oppnent;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// optionpane 글꼴 설정
		UIManager.put("OptionPane.messageFont", new Font("함초롬돋움", Font.PLAIN, 12));
		UIManager.put("OptionPane.buttonFont", new Font("함초롬돋움", Font.PLAIN, 12));

		frame = new JFrame();
		frame.setTitle(opponent + " 님과의 1:1 대화창");
		frame.setBounds(100, 100, 400, 600);
		frame.setResizable(false);//창 사이즈 조절 기능 false
		frame.setBackground(all_bg);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setBackground(all_bg);
		frame.getContentPane().setLayout(null);

		JPanel panel_info = new JPanel();
		panel_info.setBounds(0, 0, 384, 50);
		panel_info.setBackground(info_bg);
		frame.getContentPane().add(panel_info);
		panel_info.setLayout(null);

		JLabel lbl_usericon = new JLabel("icon");
		lbl_usericon.setBounds(12, 5, 44, 35);
		panel_info.add(lbl_usericon);

		JLabel lbl_yourname = new JLabel(opponent);
		lbl_yourname.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_yourname.setBounds(68, 5, 154, 35);
		panel_info.add(lbl_yourname);

		//전체대화창의 왼쪽(상대)
		JScrollPane scrollPane_all_opp = new JScrollPane();
		scrollPane_all_opp.setBorder(null);
		scrollPane_all_opp.setBounds(10, 57, 181, 395);
		scrollPane_all_opp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); // 자동스크롤
		frame.getContentPane().add(scrollPane_all_opp);

		ta_all_opp = new JTextArea();
		ta_all_opp.setBorder(null);
		ta_all_opp.setBackground(all_bg);
		ta_all_opp.setEditable(false);
		scrollPane_all_opp.setViewportView(ta_all_opp);// scrollPane에 add
		ta_all_opp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT); //왼쪽정렬
		ta_all_opp.setLineWrap(true);// 자동줄바꿈
		// ta_all.setPreferredSize(new Dimension(380, 200));//textarea 사이즈 고정

		
		//전체대화창의 오른쪽(나)
		JScrollPane scrollPane_all_me = new JScrollPane();
		scrollPane_all_me.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_all_me.setBorder(null);
		scrollPane_all_me.setBounds(191, 57, 181, 395);
		frame.getContentPane().add(scrollPane_all_me);
		
		ta_all_me = new JTextArea();
		ta_all_me.setLineWrap(true);
		ta_all_me.setEditable(false);
		ta_all_me.setBorder(null);
		ta_all_me.setBackground(new Color(255, 242, 204));
		
		
		scrollPane_all_me.setViewportView(ta_all_me);
		
		
		//보내는 내용 패널
		JPanel panel_msg = new JPanel();
		panel_msg.setBounds(0, 460, 384, 100);
		panel_msg.setBackground(Color.WHITE);
		frame.getContentPane().add(panel_msg);
		panel_msg.setLayout(null);

		btn_send = new JButton("전송");
		btn_send.setBackground(send_bg);
		btn_send.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btn_send.setBounds(304, 10, 69, 56);
		panel_msg.add(btn_send);

		JScrollPane scrollPane_my = new JScrollPane();
		scrollPane_my.setBorder(null);
		scrollPane_my.setBounds(12, 10, 280, 80);
		scrollPane_my.setViewportBorder(null);// 테두리제거
		scrollPane_my.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED); // 자동스크롤
		panel_msg.add(scrollPane_my);

		ta_mytextarea = new JTextArea();
		scrollPane_my.setViewportView(ta_mytextarea);
		ta_mytextarea.setBackground(my_bg);
		ta_mytextarea.setBorder(null);// textare 테두리 제거. https://hsp1116.tistory.com/1
		ta_mytextarea.setMinimumSize(new Dimension(10, 24));
		ta_mytextarea.setAlignmentX(Component.LEFT_ALIGNMENT);

		ta_mytextarea.setLineWrap(true);// 자동줄바꿈
		

	}

	void addLog(String log)

	{

		// 클라이언트에서 받은 이름을 앞에 붙여주기.
		// +추가적기능) 받은 이름이 나 라면 이름 떼고 해당 텍스트 우측정렬(카톡형식)
		
		// StyleConstants.setAlignment(log, StyleConstants.ALIGN_RIGHT);
		//String.format("%100s", log) 
		//log = me + " : " + log;
		ta_all_me.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); //텍스트 오른쪽부터쓰기
		ta_all_me.append(log + "\n"); // 로그 내용을 JTextArea 위에 붙여주고	
		ta_all_opp.append("\n"); //반대쪽 채팅도 한줄 띄워줌.
		ta_all_me.setCaretPosition(ta_all_me.getDocument().getLength()); // 맨아래로 스크롤한다.

	}

	//상대가 보냈을때
	void addOpponentLog(String log) {
		ta_all_opp.append(log + "\n"); // 로그 내용을 JTextArea 위에 붙여줌
		ta_all_me.append("\n"); //반대쪽 채팅도 한줄 띄워줌.
		ta_all_opp.setCaretPosition(ta_all_opp.getDocument().getLength()); // 맨아래로 스크롤한다.
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
