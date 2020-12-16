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
		frame.setTitle("������ ����");
		frame.setBounds(100, 100, 400, 600);
		frame.setResizable(false);//â ������ ���� ��� false
		// frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//������ ���� �ؽ�Ʈ
		JLabel lbl_profile = new JLabel("������ ����");
		lbl_profile.setForeground(Color.WHITE);
		lbl_profile.setOpaque(true);
		lbl_profile.setFont(new Font("���ʷҵ���", Font.PLAIN, 12));
		lbl_profile.setBackground(dark);
		lbl_profile.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_profile.setBounds(0, 0, 384, 35);		
		frame.getContentPane().add(lbl_profile);
		
		// ������ �̹���
		JLabel lbl_profileimg = new JLabel("");
		lbl_profileimg.setOpaque(true);
		lbl_profileimg.setBackground(yellow);
		lbl_profileimg.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_profileimg.setIcon(new ImageIcon(EditProfile.class.getResource("/project/friend.png")));
		lbl_profileimg.setBounds(152, 96, 80, 80);
		frame.getContentPane().add(lbl_profileimg);

		// ���� �г��� ����
		tf_usernName = new JTextField();
		tf_usernName.setHorizontalAlignment(SwingConstants.CENTER);
		tf_usernName.setFont(new Font("���ʷҵ���", Font.PLAIN, 12));
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
					tf_usernName.setForeground(Color.BLACK); // ���� �г��Ӱ� tf�� ���� ������ �ؽ�Ʈ ���� �������� ����.
				}
			}

			@Override
			public void mouseExited(MouseEvent e) { // �г��� ������ �ƹ��͵� �Է����� �ʰ� ���콺�� ���������� ��
				if (tf_usernName.getText().length() < 1) {
					tf_usernName.setText(defaultusernName); // ���� ���� �г������� ����
					tf_usernName.setForeground(Color.LIGHT_GRAY); // �ؽ�Ʈ �÷� ȸ������ ����
				}
			}
		});

		// ���� �޼��� ����
		tf_usermsg = new JTextField();
		tf_usermsg.setHorizontalAlignment(SwingConstants.CENTER);
		tf_usermsg.setFont(new Font("���ʷҵ���", Font.PLAIN, 12));
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
					tf_usermsg.setForeground(Color.BLACK); // ���� �޼����� tf�� ���� ������ �ؽ�Ʈ ���� �������� ����.
				}
			}

			@Override
			public void mouseExited(MouseEvent e) { // �޼��� ������ �ƹ��͵� �Է����� �ʰ� ���콺�� ���������� ��
				if (tf_usermsg.getText().length() < 1) {
					tf_usermsg.setText(defaultuserMsg); // ���� ���� �޼������� ����
					tf_usermsg.setForeground(Color.LIGHT_GRAY); // �ؽ�Ʈ �÷� ȸ������ ����
				}
			}

		});

		// ���� ��ư
		btn_edit = new JButton("New button");
		btn_edit.setFont(new Font("���ʷҵ���", Font.PLAIN, 12));
		btn_edit.setText("����");
		btn_edit.setBounds(92, 312, 200, 30);
		btn_edit.setBackground(yellow);
		frame.getContentPane().add(btn_edit);

		// ��� ��ư
		btn_cancel = new JButton("New button");
		btn_cancel.setFont(new Font("���ʷҵ���", Font.PLAIN, 12));
		btn_cancel.setText("���");
		btn_cancel.setBounds(92, 349, 200, 30);
		btn_cancel.setBackground(dark);
		frame.getContentPane().add(btn_cancel);

		// Ż�� ��ư
		btn_deleteacct = new JButton("New button");
		btn_deleteacct.setForeground(Color.WHITE);
		btn_deleteacct.setFont(new Font("���ʷҵ���", Font.PLAIN, 12));
		btn_deleteacct.setText("ȸ�� Ż��");
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
