package project;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Vector;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FriendSearch extends JFrame implements MouseListener, ActionListener {

	// 테이블 공통 속성
	int rowhight = 25;
	
	Vector row;
	Vector header;
	DefaultTableModel model;
	JLabel icon_exit;
	JLabel icon_search;
	JLabel icon_friend;
	JTable table;//검색 결과 테이블
	Color main_bg = new Color(255, 242, 204);
	Color menu_bg = new Color(255, 252, 239);
	Color friend_bg = new Color(255, 245, 217);
	Color title_bg = new Color(194, 163, 133);
	Color line = new Color(153, 102, 51);
	Color pd_bg = friend_bg;
	Color login_bg = new Color(255, 242, 204);
	
	JPopupMenu popupMenu;
	JMenuItem menuProfile;
	JMenuItem menuAddFr;
	JFrame frame;
	private JScrollPane scrollPane;
	String userName;
	String userMsg;
	JTextField tf_search;
	JLabel lblbtn_search;
	JLabel lbl_result;
	ApiExplorer api;
	
	public Vector getColumn() {
		Vector col = new Vector();
		col.add("아이디");
		col.add("닉네임");
		col.add("한마디");

		return col;
	}

	public FriendSearch() throws IOException {
		initialize();
	}

	public void initialize() throws IOException {

		frame = new JFrame();
		frame.setTitle("메인화면");
		frame.setBounds(100, 100, 400, 600);
	    frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// 메뉴 패널
		JPanel panel_menu = new JPanel();
		panel_menu.setBackground(menu_bg);
		panel_menu.setBounds(0, 0, 71, 460);
		frame.getContentPane().add(panel_menu);
		panel_menu.setLayout(null);

		/* 친구 목록 아이콘 */
		icon_friend = new JLabel("");
		icon_friend.setToolTipText("친구목록");
		panel_menu.add(icon_friend);
		icon_friend.setHorizontalAlignment(SwingConstants.CENTER);
		icon_friend.setLocation(5, 10);
		icon_friend.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/friend(small).png")));
		icon_friend.setSize(61, 64);
		icon_friend.addMouseListener(new MouseAdapter() {

			public void mouseEntered(MouseEvent arg0) {
				icon_friend.setIcon(new ImageIcon(FriendList.class.getResource("/project/friend(small_dark).png")));
			}

			public void mouseExited(MouseEvent arg0) {
				icon_friend.setIcon(new ImageIcon(FriendList.class.getResource("/project/friend(small).png")));
			}
		});
		panel_menu.add(icon_friend);

		/* 친구 찾기 아이콘 */
		icon_search = new JLabel("");
		icon_search.setToolTipText("친구찾기");
		panel_menu.add(icon_search);
		icon_search.setHorizontalAlignment(SwingConstants.CENTER);
		icon_search.setBounds(2, 63, 67, 68);
		icon_search.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/search(small_dark).png")));
		// 친구찾기 아이콘 클릭하면 친구 찾기 창으로 이동하기
		icon_search.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {

			}

		});
		panel_menu.add(icon_search);

		/* 로그아웃 아이콘 */
		icon_exit = new JLabel("");
		icon_exit.setToolTipText("로그아웃");
		panel_menu.add(icon_exit);
		icon_exit.setHorizontalAlignment(SwingConstants.CENTER);
		icon_exit.setBounds(5, 386, 61, 64);
		icon_exit.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/logout(small).png")));
		panel_menu.add(icon_exit);

		// 로그아웃 아이콘에 마우스 호버했을때 small_dark로 바뀌도록 하기
		icon_exit.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent arg0) {
				icon_exit.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/logout(small_dark).png")));
			}

			public void mouseExited(MouseEvent arg0) {
				icon_exit.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/logout(small).png")));
			}
		});

		

		// 공공데이터

		api = new ApiExplorer();
		String info1 = "경기도 성남시 수정구 복정동";
		String info2 = api.rainFall + "%";
		String info3 = api.humidity + "%";
	    String info4 = api.sky+"";
    
		JPanel panel_search = new JPanel();
		panel_search.setBackground(friend_bg);
		panel_search.setOpaque(true);
		panel_search.setBounds(70, 34, 324, 101);
		frame.getContentPane().add(panel_search);
		panel_search.setLayout(null);//공공데이터 끝

		tf_search = new JTextField();
		tf_search.setBounds(43, 49, 189, 21);
		panel_search.add(tf_search);
		tf_search.setColumns(10);

		lblbtn_search = new JLabel("");
		lblbtn_search.setHorizontalAlignment(SwingConstants.CENTER);
		lblbtn_search.setIcon(new ImageIcon(FriendSearch.class.getResource("/project/search(verysmall_dark).png")));
		lblbtn_search.setBounds(244, 42, 35, 35);
		panel_search.add(lblbtn_search);

		JLabel lbl_explain = new JLabel("ID, 닉네임으로 검색하세요");
		lbl_explain.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_explain.setBounds(43, 23, 210, 15);
		panel_search.add(lbl_explain);

		JPanel panel_result = new JPanel();
		panel_result.setBackground(friend_bg);
		panel_result.setOpaque(true);
		panel_result.setBounds(70, 167, 324, 293);
		frame.getContentPane().add(panel_result);
		panel_result.setLayout(null);
		
		lbl_result = new JLabel();
		lbl_result.setHorizontalAlignment(SwingConstants.CENTER);
	    lbl_result.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
	    lbl_result.setBounds(17, 131, 290, 33);
	    lbl_result.setText("검색어를 입력하세요");
	    panel_result.add(lbl_result);

		JLabel lbl_Search = new JLabel("친구 검색");
		lbl_Search.setForeground(Color.BLACK);
		lbl_Search.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Search.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_Search.setOpaque(true);
		lbl_Search.setBackground(title_bg);
		lbl_Search.setBounds(70, 0, 324, 35);
		frame.getContentPane().add(lbl_Search);

		JLabel lbl_searchResult = new JLabel("검색 결과");
		lbl_searchResult.setHorizontalAlignment(SwingConstants.CENTER);

		lbl_searchResult.setForeground(Color.BLACK);
		lbl_searchResult.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
		lbl_searchResult.setOpaque(true);
		lbl_searchResult.setBackground(title_bg);
		lbl_searchResult.setBounds(70, 133, 324, 35);
		frame.getContentPane().add(lbl_searchResult);
		
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
	
	public void setTable() {
		
		table = new JTable(model);
		table.setBounds(12, 10, 290, 273);
		table.setBorder(null);
		table.setFillsViewportHeight(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(rowhight);
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);

		JScrollPane panel_table = new JScrollPane(table);
		panel_table.setBackground(friend_bg);
		panel_table.setBounds(71, 167, 313, 293);
		frame.getContentPane().add(panel_table);
		table.getColumn("아이디");
		table.getColumn("닉네임");
		table.getColumn("한마디");	
		
		//table.getColumn("온라인").setPreferredWidth(5);
		//table.getColumn("닉네임").setPreferredWidth(30);
		//table.getColumn("한마디").setPreferredWidth(200);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		int r = table.getSelectedRow();
        String id = (String) table.getValueAt(r, 0);
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
