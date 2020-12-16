package project;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class EditProfile extends JFrame implements MouseListener, ActionListener{

	JFrame frame;
	JTextField tf_usernName;
	JTextField tf_usermsg;
	String defaultusernName;
	String defaultuserMsg;
	
	Color bg = new Color(194, 163, 133);
	Color title_bg = new Color(194, 163, 133);
	Color yellow = new Color(255, 245, 217);
	Color cocoa = new Color(194, 158, 113);
	Color dark = new Color(153, 102, 51);
	Color delete = new Color(255, 80, 80);

	JButton btn_edit;
	JButton btn_cancel;
	JButton btn_deleteacct;
	/**
	 * Create the application.
	 */
	public EditProfile() {
		initialize();
		frame.setVisible(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(bg);
		frame.setTitle("프로필 수정");
		frame.setBounds(100, 100, 400, 600);
		frame.setResizable(false);//창 사이즈 조절 기능 false
		// frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//프로필 설정 텍스트
		JLabel lbl_profile = new JLabel("프로필 설정");
		lbl_profile.setForeground(Color.WHITE);
		lbl_profile.setOpaque(true);
		lbl_profile.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_profile.setBackground(dark);
		lbl_profile.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_profile.setBounds(0, 0, 384, 35);		
		frame.getContentPane().add(lbl_profile);
		
		// 프로필 이미지
		JLabel lbl_profileimg = new JLabel("");
		lbl_profileimg.setOpaque(true);
		lbl_profileimg.setBackground(yellow);
		lbl_profileimg.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_profileimg.setIcon(new ImageIcon(EditProfile.class.getResource("/project/friend.png")));
		lbl_profileimg.setBounds(152, 96, 80, 80);
		frame.getContentPane().add(lbl_profileimg);

		// 유저 닉네임 변경
		tf_usernName = new JTextField();
		tf_usernName.setHorizontalAlignment(SwingConstants.CENTER);
		tf_usernName.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		tf_usernName.setBounds(88, 223, 208, 25);
		frame.getContentPane().add(tf_usernName);
		tf_usernName.setColumns(10);
		tf_usernName.setText(defaultusernName);
		tf_usernName.setForeground(Color.LIGHT_GRAY);

		tf_usernName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (tf_usernName.getText().equalsIgnoreCase(defaultusernName)) {
					tf_usernName.setText("");
					tf_usernName.setForeground(Color.BLACK); // 기존 닉네임과 tf가 같지 않으면 텍스트 색을 검정으로 변경.
				}
			}

			@Override
			public void mouseExited(MouseEvent e) { // 닉네임 영역에 아무것도 입력하지 않고 마우스가 빠져나갔을 때
				if (tf_usernName.getText().length() < 1) {
					tf_usernName.setText(defaultusernName); // 기존 유저 닉네임으로 변경
					tf_usernName.setForeground(Color.LIGHT_GRAY); // 텍스트 컬러 회색으로 변경
				}
			}
		});

		// 유저 메세지 변경
		tf_usermsg = new JTextField();
		tf_usermsg.setHorizontalAlignment(SwingConstants.CENTER);
		tf_usermsg.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		tf_usermsg.setBounds(88, 263, 208, 25);
		frame.getContentPane().add(tf_usermsg);
		tf_usermsg.setColumns(10);
		tf_usermsg.setText(defaultuserMsg);
		tf_usermsg.setForeground(Color.LIGHT_GRAY);

		tf_usermsg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (tf_usermsg.getText().equalsIgnoreCase(defaultuserMsg)) {
					tf_usermsg.setText("");
					tf_usermsg.setForeground(Color.BLACK); // 기존 메세지과 tf가 같지 않으면 텍스트 색을 검정으로 변경.
				}
			}

			@Override
			public void mouseExited(MouseEvent e) { // 메세지 영역에 아무것도 입력하지 않고 마우스가 빠져나갔을 때
				if (tf_usermsg.getText().length() < 1) {
					tf_usermsg.setText(defaultuserMsg); // 기존 유저 메세지으로 변경
					tf_usermsg.setForeground(Color.LIGHT_GRAY); // 텍스트 컬러 회색으로 변경
				}
			}

		});

		// 변경 버튼
		btn_edit = new JButton("New button");
		btn_edit.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btn_edit.setText("변경");
		btn_edit.setBounds(92, 312, 200, 30);
		btn_edit.setBackground(yellow);
		frame.getContentPane().add(btn_edit);

		// 취소 버튼
		btn_cancel = new JButton("New button");
		btn_cancel.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btn_cancel.setText("취소");
		btn_cancel.setBounds(92, 349, 200, 30);
		btn_cancel.setBackground(dark);
		frame.getContentPane().add(btn_cancel);

		// 탈퇴 버튼
		btn_deleteacct = new JButton("New button");
		btn_deleteacct.setForeground(Color.WHITE);
		btn_deleteacct.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btn_deleteacct.setText("회원 탈퇴");
		btn_deleteacct.setBounds(92, 468, 200, 30);
		btn_deleteacct.setBackground(delete);
		frame.getContentPane().add(btn_deleteacct);
		


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
