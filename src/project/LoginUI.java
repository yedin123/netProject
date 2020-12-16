package project;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import java.awt.Point;

public class LoginUI extends JFrame {

	JFrame frame;
	JTextField tfID;
	TextField tfPW;
	JButton btnLogIn;
	JButton btnSignIn;
	Color login_bg = new Color(255, 242, 204);
    Color beige = new Color(255, 252, 239);
    Color cocoa = new Color(194,158,113);
    
	public LoginUI() {
		initialize();
		
	}

	private void initialize() {

		MyMouseListener listner = new MyMouseListener();
		frame = new JFrame();
		frame.getContentPane().setBackground(login_bg);
		frame.setTitle("로그인");
		frame.setBounds(100, 100, 400, 600);
		frame.setResizable(false);//창 사이즈 조절 기능 false
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblLogin = new JLabel("로그인");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setFont(new Font("함초롬돋움", Font.PLAIN, 18));
		lblLogin.setBounds(131, 10, 132, 54);
		frame.getContentPane().add(lblLogin);

		tfID = new JTextField();
		tfID.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		tfID.setText("아이디");
		tfID.setForeground(Color.LIGHT_GRAY);

		tfID.setBounds(93, 290, 208, 25);
		frame.getContentPane().add(tfID);
		tfID.setColumns(10);

		tfID.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (tfID.getText().equalsIgnoreCase("아이디")) {
					tfID.setText("");
					tfID.setForeground(Color.BLACK); // "아이디"와 tf가 같지 않으면 텍스트 색을 검정으로 변경.
				}
			}

			@Override
			public void mouseExited(MouseEvent e) { // 메세지 영역에 아무것도 입력하지 않고 마우스가 빠져나갔을 때
				if (tfID.getText().length() < 1) {
					tfID.setText("아이디"); // 아이디 텍스트로 변경
					tfID.setForeground(Color.LIGHT_GRAY); // 텍스트 컬러 회색으로 변경
				}
			}

		});
		if (tfID.getText().equalsIgnoreCase("")) {
			tfID.setText("아이디");
		}

		tfPW = new TextField();
		tfPW.setFont(new Font("함초롬돋움", Font.PLAIN, 12));

		tfPW.setText("비밀번호");
		tfPW.setForeground(Color.LIGHT_GRAY);
		tfPW.setColumns(10);
		tfPW.setBounds(93, 322, 208, 25);
		tfPW.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (tfPW.getText().equalsIgnoreCase("비밀번호")) {
					tfPW.setForeground(Color.BLACK);
					tfPW.setText("");
					tfPW.setEchoChar('●');
				}
			}
			
			public void mouseExited(MouseEvent e) { // 메세지 영역에 아무것도 입력하지 않고 마우스가 빠져나갔을 때
				if (tfPW.getText().length() < 1) {
					tfPW.setText("비밀번호"); // 아이디 텍스트로 변경
					tfPW.setForeground(Color.LIGHT_GRAY); // 텍스트 컬러 회색으로 변경
				}
			}

		});
		if (tfPW.getText().equalsIgnoreCase("")) {
			tfPW.setText("비밀번호");
		}
		frame.getContentPane().add(tfPW);

		btnLogIn = new JButton("로그인");
		btnLogIn.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btnLogIn.setBounds(97, 373, 200, 30);
		frame.getContentPane().add(btnLogIn);
		
		btnLogIn.setFocusPainted(false);
		btnLogIn.setBackground(beige);
		btnLogIn.addMouseListener(listner);
		
		
		btnSignIn = new JButton("회원가입");
		btnSignIn.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		btnSignIn.setBounds(97, 414, 200, 30);
		
		btnSignIn.setFocusPainted(false);
		btnSignIn.setBackground(beige);
		btnSignIn.addMouseListener(listner);
		frame.getContentPane().add(btnSignIn);
		frame.setVisible(true);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginUI.class.getResource("cocoatalk4(small).png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(85, 74, 259, 195);
		frame.getContentPane().add(lblNewLabel);
		frame.setVisible(false);

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