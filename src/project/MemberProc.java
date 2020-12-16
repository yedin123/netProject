package project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import project.LoginUI.MyMouseListener;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;

public class MemberProc extends JFrame{

	JFrame frame;
	JTextField tfId;
	JTextField tfName;
	JTextField tfnName;
	JPasswordField pfPw;
	JTextField tfEmail;
	JTextField tfYear;
	JTextField tfMonth;
	JTextField tfDate;
	JButton btnInsert;
	JButton btnCancel;
	JButton btnD;
	JButton btnD2;
	Color all_bg = new Color(255, 242, 204);
    Color beige = new Color(255, 252, 239);
    Color cocoa = new Color(194,158,113);
    MyMouseListener listner = new MyMouseListener();
    
	public MemberProc() {
		initialize();
	}

	private void initialize() {
		
		frame = new JFrame();
		frame.getContentPane().setBackground(all_bg);
		frame.setBounds(100, 100, 450, 614);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//아이디, 중복확인
		JLabel lblNewLabel = new JLabel("\uC544\uC774\uB514 :");
		lblNewLabel.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(12, 10, 93, 68);
		frame.getContentPane().add(lblNewLabel);
		
		tfId = new JTextField();
		tfId.setBounds(107, 10, 222, 68);
		frame.getContentPane().add(tfId);
		tfId.setColumns(10);
		
		btnD = new JButton("\uC911\uBCF5\uD655\uC778");
		btnD.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btnD.setBackground(beige);
		btnD.setBounds(333, 10, 91, 68);
		frame.getContentPane().add(btnD);
		
		//비밀번호
		JLabel lblNewLabel_1 = new JLabel("\uBE44\uBC00\uBC88\uD638 :");
		lblNewLabel_1.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(12, 88, 93, 68);
		frame.getContentPane().add(lblNewLabel_1);
		
		pfPw = new JPasswordField();
		pfPw.setColumns(10);
		pfPw.setBounds(107, 88, 317, 68);
		frame.getContentPane().add(pfPw);
		
		//닉네임
		JLabel lblNewLabel_1_1 = new JLabel("\uB2C9\uB124\uC784 :");
		lblNewLabel_1_1.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setBounds(12, 166, 93, 68);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		tfnName = new JTextField();
		tfnName.setColumns(10);
		tfnName.setBounds(107, 166, 222, 68);
		frame.getContentPane().add(tfnName);
		
		btnD2 = new JButton("\uC911\uBCF5\uD655\uC778");
		btnD2.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btnD2.setBackground(beige);
		btnD2.setBounds(333, 166, 91, 68);
		frame.getContentPane().add(btnD2);
		
		//이름
		JLabel lblNewLabel_1_1_1 = new JLabel("\uC774\uB984 :");
		lblNewLabel_1_1_1.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setBounds(12, 241, 93, 68);
		frame.getContentPane().add(lblNewLabel_1_1_1);
		
		tfName = new JTextField();
		tfName.setColumns(10);
		tfName.setBounds(107, 244, 317, 68);
		frame.getContentPane().add(tfName);
		
		//이메일
		JLabel lblNewLabel_1_1_2 = new JLabel("email :");
		lblNewLabel_1_1_2.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lblNewLabel_1_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2.setBounds(12, 320, 93, 68);
		frame.getContentPane().add(lblNewLabel_1_1_2);
		
		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		tfEmail.setBounds(107, 322, 317, 68);
		frame.getContentPane().add(tfEmail);
		
		
		//생일
		JLabel lblNewLabel_1_1_2_1 = new JLabel("\uC0DD\uC77C :");
		lblNewLabel_1_1_2_1.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lblNewLabel_1_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2_1.setBounds(12, 398, 93, 68);
		frame.getContentPane().add(lblNewLabel_1_1_2_1);
		
		tfYear = new JTextField();
		tfYear.setBounds(107, 418, 96, 30);
		frame.getContentPane().add(tfYear);
		tfYear.setColumns(10);
		
		tfMonth = new JTextField();
		tfMonth.setColumns(10);
		tfMonth.setBounds(237, 418, 60, 30);
		frame.getContentPane().add(tfMonth);
		
		tfDate = new JTextField();
		tfDate.setColumns(10);
		tfDate.setBounds(333, 418, 60, 30);
		frame.getContentPane().add(tfDate);
		
		JLabel lblNewLabel_2 = new JLabel("/");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("굴림", Font.PLAIN, 15));
		lblNewLabel_2.setBounds(203, 418, 32, 30);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("/");
		lblNewLabel_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2_1.setFont(new Font("굴림", Font.PLAIN, 15));
		lblNewLabel_2_1.setBounds(297, 418, 32, 30);
		frame.getContentPane().add(lblNewLabel_2_1);
		
		btnInsert = new JButton("\uAC00\uC785");
		btnInsert.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btnInsert.setBackground(beige);
		btnInsert.setBounds(135, 499, 91, 45);
		frame.getContentPane().add(btnInsert);
		
		btnCancel = new JButton("\uCDE8\uC18C");
		btnCancel.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btnCancel.setBackground(beige);
		btnCancel.setBounds(238, 499, 91, 45);
		frame.getContentPane().add(btnCancel);
		
		
		btnD.setFocusPainted(false);
		btnD.addMouseListener(listner);
		
		btnD2.setFocusPainted(false);
		btnD2.addMouseListener(listner);
		
		btnInsert.setFocusPainted(false);
		btnInsert.addMouseListener(listner);
		
		btnCancel.setFocusPainted(false);
		btnCancel.addMouseListener(listner);
	}
	class MyMouseListener implements MouseListener{
		
        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO 자동 생성된 메소드 스텁

        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO 자동 생성된 메소드 스텁

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO 자동 생성된 메소드 스텁

        }

        @Override
         public void mouseEntered(MouseEvent e) {
            JButton b = (JButton)e.getSource();
            b.setBackground(cocoa);
            b.setBorderPainted(false);

        }

        @Override
        public void mouseExited(MouseEvent e) {
             JButton b = (JButton)e.getSource();
             b.setBackground(beige);
             b.setBorderPainted(true);
        }
	}
	
}
