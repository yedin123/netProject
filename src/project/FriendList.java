package project;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

public class FriendList extends JFrame implements MouseListener, ActionListener {
	public FriendList() {
	}

	Vector row1;
	Vector row2;
	Vector header;
	DefaultTableModel online;
	DefaultTableModel offline;
	JTable jTable_online;
	JTable jTable_offline;
	JLabel icon_exit;
	JLabel icon_search;
	JLabel lbl_edit;
	JLabel lbl_usernName;
	Color login_bg = new Color(255, 242, 204);
	Color BGY = new Color(255, 242, 204); // 배경의 노란색
	JFrame frame;
	private JScrollPane scrollPane;
	String usernName;
	String userMsg;
	JLabel lbl_usermsg;
	JMenuItem menuChat;
	JMenuItem menuProfile1;
	JMenuItem menuProfile2;
	ApiExplorer api;
	
	public Vector getColumn() {
		Vector col = new Vector();
		col.add("온라인");
		col.add("닉네임");
		col.add("한마디");

		return col;
	}
	
	 public static void main(String[] args) {
	 new FriendList();

	 }
	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	 
	public void initialize() throws IOException {
	      
		Color main_bg = new Color(255, 242, 204);
		Color menu_bg = new Color(255, 252, 239);
		Color friend_bg = new Color(255, 245, 217);
		Color me_bg = new Color(194, 163, 133);
		Color line = new Color(153, 102, 51);
		Color pd_bg = friend_bg;

		frame = new JFrame();
		frame.setTitle("메인화면");
		frame.setBounds(100, 100, 400, 600);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel_menu = new JPanel();
		panel_menu.setBackground(menu_bg);
		panel_menu.setBounds(0, 0, 71, 460);
		frame.getContentPane().add(panel_menu);
		panel_menu.setLayout(null);

		JLabel icon_friend = new JLabel("");
		icon_friend.setToolTipText("친구목록");
		panel_menu.add(icon_friend);
		icon_friend.setHorizontalAlignment(SwingConstants.CENTER);
		icon_friend.setLocation(5, 10);
		icon_friend.setIcon(new ImageIcon(FriendList.class.getResource("/project/friend(small).png")));
		icon_friend.setSize(61, 64);
		panel_menu.add(icon_friend);

		icon_search = new JLabel("");
		icon_search.setToolTipText("친구찾기");
		panel_menu.add(icon_search);
		icon_search.setHorizontalAlignment(SwingConstants.CENTER);
		icon_search.setBounds(2, 63, 67, 68);
		icon_search.setIcon(new ImageIcon(FriendList.class.getResource("/project/search(small).png")));
		panel_menu.add(icon_search);

		icon_exit = new JLabel("");
		icon_exit.setToolTipText("로그아웃");
		panel_menu.add(icon_exit);
		icon_exit.setHorizontalAlignment(SwingConstants.CENTER);
		icon_exit.setBounds(5, 386, 61, 64);
		icon_exit.setIcon(new ImageIcon(FriendList.class.getResource("/project/logout(small).png")));
		panel_menu.add(icon_exit);
		
		//icon_exit에 마우스 호버했을때 small_dark로 바뀌게 하는 코드	
		icon_exit.addMouseListener(new MouseAdapter() {
			
			public void mouseEntered(MouseEvent arg0) {
				icon_exit.setIcon(new ImageIcon(FriendList.class.getResource("/project/logout(small_dark).png")));
			}

			
			public void mouseExited(MouseEvent arg0) {
				icon_exit.setIcon(new ImageIcon(FriendList.class.getResource("/project/logout(small).png")));
			}
		});
		

		JPanel panel_user = new JPanel();
		panel_user.setBackground(me_bg);
		panel_user.setBounds(71, 0, 313, 65);
		frame.getContentPane().add(panel_user);
		panel_user.setLayout(null);

		lbl_usernName = new JLabel(usernName);
		lbl_usernName.setFont(new Font("함초롬돋움", Font.BOLD, 18));
		lbl_usernName.setForeground(SystemColor.window);
		lbl_usernName.setBounds(12, 15, 119, 27);
		panel_user.add(lbl_usernName);

		lbl_usermsg = new JLabel(userMsg);
		lbl_usermsg.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_usermsg.setForeground(SystemColor.window);
		lbl_usermsg.setBounds(12, 40, 220, 15);
		panel_user.add(lbl_usermsg);

		lbl_edit = new JLabel("\uD504\uB85C\uD544\uC124\uC815");
		lbl_edit.setBackground(SystemColor.desktop);
		lbl_edit.setForeground(SystemColor.text);
		lbl_edit.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_edit.setIcon(new ImageIcon(FriendList.class.getResource("/project/edit.png")));
		lbl_edit.setBounds(212, 18, 89, 27);
		panel_user.add(lbl_edit);

		// 테이블 공통 속성
		int rowhight = 25;
		
		JPopupMenu popupMenu1 = new JPopupMenu();
		menuChat = new JMenuItem("채팅 하기");
		menuProfile1 = new JMenuItem("프로필 보기");
		menuProfile1.setFont(new Font("함초롱돋움", Font.PLAIN, 12));
		popupMenu1.add(menuProfile1);
		popupMenu1.add(menuChat);
		
		JPopupMenu popupMenu2 = new JPopupMenu();
		menuProfile2 = new JMenuItem("프로필 보기");
		menuProfile2.setFont(new Font("함초롱돋움", Font.PLAIN, 12));
		popupMenu2.add(menuProfile2);

		// 온라인 테이블		

		jTable_online = new JTable(online);
		jTable_online.setBorder(null);
		jTable_online.setFillsViewportHeight(true);

		jTable_online.setBackground(friend_bg);
		jTable_online.getTableHeader().setReorderingAllowed(false);
		jTable_online.getTableHeader().setBackground(menu_bg);
		jTable_online.setRowHeight(rowhight);
		jTable_online.setShowVerticalLines(false);
		jTable_online.setShowHorizontalLines(false);

		JScrollPane panel_onlinefriendList = new JScrollPane(jTable_online);
		panel_onlinefriendList.setBackground(friend_bg);
		panel_onlinefriendList.setBounds(71, 64, 313, 199);
		frame.getContentPane().add(panel_onlinefriendList);
		
		jTable_online.getColumn("온라인").setPreferredWidth(15);
		jTable_online.getColumn("닉네임").setPreferredWidth(35);
		jTable_online.getColumn("한마디").setPreferredWidth(180);
		jTable_online.setComponentPopupMenu(popupMenu1);

		// 오프라인 테이블

		jTable_offline = new JTable(offline);
		jTable_offline.setBorder(null);
		jTable_offline.setFillsViewportHeight(true);

		/* 헤더부분 */
		jTable_offline.setBackground(SystemColor.scrollbar);
		jTable_offline.getTableHeader().setReorderingAllowed(false);
		jTable_offline.getTableHeader().setVisible(false);// 오프라인에서는 헤더가 안보이도록
		jTable_offline.setTableHeader(null);

		jTable_offline.setRowHeight(rowhight);
		jTable_offline.setShowVerticalLines(false);
		jTable_offline.setShowHorizontalLines(false);
		// jTable.set

		JScrollPane panel_offlinefriendList = new JScrollPane(jTable_offline);
		panel_offlinefriendList.setBackground(friend_bg);
		panel_offlinefriendList.setBounds(71, 261, 313, 199);
		frame.getContentPane().add(panel_offlinefriendList);

		jTable_offline.getColumn("온라인").setPreferredWidth(15);
		jTable_offline.getColumn("닉네임").setPreferredWidth(35);
		jTable_offline.getColumn("한마디").setPreferredWidth(180);
		jTable_offline.setComponentPopupMenu(popupMenu2);
		
		// 공공데이터
		// 공공데이터

		api = new ApiExplorer();
		String info1 = "경기도 성남시 수정구 복정동";
		String info2 = api.rainFall + "%";
		String info3 = api.humidity + "%";
	    String info4 = api.sky+"";
    
		JPanel panel_publicData = new JPanel();
		panel_publicData.setBackground(Color.WHITE);
		panel_publicData.setBounds(0, 460, 394, 111);
		frame.getContentPane().add(panel_publicData);
		panel_publicData.setLayout(null);
		JLabel lbl_village = new JLabel(info1);
		lbl_village.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/mapmarker.png")));
		lbl_village.setHorizontalAlignment(SwingConstants.LEFT);
		lbl_village.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_village.setBounds(118, 80, 196, 15);
		panel_publicData.add(lbl_village);
		JLabel lbl_data_pop = new JLabel(info2);
		lbl_data_pop.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_data_pop.setFont(new Font("함초롬돋움", Font.PLAIN, 16));
		lbl_data_pop.setBounds(204, 35, 96, 30);
		panel_publicData.add(lbl_data_pop);
		JLabel lbl_data_hum = new JLabel(info3);
		lbl_data_hum.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_data_hum.setFont(new Font("함초롬돋움", Font.PLAIN, 16));
		lbl_data_hum.setBounds(295, 35, 84, 30);
		panel_publicData.add(lbl_data_hum);
		
		JLabel lbl_weathericon = new JLabel();
		
		lbl_weathericon.setHorizontalAlignment(SwingConstants.CENTER);
		
		lbl_weathericon.setBounds(12, 8, 96, 88);
		
		
		/* 날씨 이미지 표시 */
		if(api.sky.equals("맑음")) {
			lbl_weathericon.setToolTipText("맑음");
			lbl_weathericon.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/clear_small.png")));
		}
		else if(api.sky.equals("구름많음")) {
			lbl_weathericon.setToolTipText("구름많음");
			lbl_weathericon.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/sunny_small.png")));
		}
		else if(api.sky.equals("흐림")) {
			lbl_weathericon.setToolTipText("흐림");
			lbl_weathericon.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/cloudy_small.png")));
		}
		else lbl_weathericon.setText("정보가 없습니다");
		panel_publicData.add(lbl_weathericon);
		
		
		
		JLabel lbl_pop = new JLabel("강수확률");
		lbl_pop.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_pop.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_pop.setBounds(204, 15, 92, 15);
		panel_publicData.add(lbl_pop);
		
		JLabel lbl_hum = new JLabel("습도");
		lbl_hum.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_hum.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_hum.setBounds(308, 15, 57, 15);
		panel_publicData.add(lbl_hum);
		
		JLabel lbl_prov = new JLabel("제공: 기상청");
		lbl_prov.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_prov.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_prov.setBounds(242, 80, 129, 15);
		panel_publicData.add(lbl_prov);
		
		JLabel lbl_sky = new JLabel("날씨");
		lbl_sky.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_sky.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_sky.setBounds(132, 15, 57, 15);
		panel_publicData.add(lbl_sky);
		JLabel lbl_data_sky = new JLabel(info4);
		lbl_data_sky.setBounds(108, 35, 106, 30);
		panel_publicData.add(lbl_data_sky);
		lbl_data_sky.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_data_sky.setFont(new Font("함초롬돋움", Font.PLAIN, 16));
		
		JPanel panel_line = new JPanel();
		panel_line.setBackground(main_bg);
		panel_line.setBounds(108, 70, 286, 1);
		panel_publicData.add(panel_line);
	
		//공공데이터 끝
		frame.setVisible(false);

	}
	
	public void setTodayMsg(String newText) {
        lbl_usermsg.setText(newText);
    }
	public void setTodaynName(String newText) {
		lbl_usernName.setText(newText);
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
