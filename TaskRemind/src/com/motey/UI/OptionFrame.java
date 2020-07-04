package com.motey.UI;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.motey.utils.ImageUtil;
import com.motey.utils.MyWindowUtil;
import com.sun.awt.AWTUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JLabel;

public class OptionFrame extends JFrame{

	private static final long serialVersionUID = 8344554401603928795L;
	
	private static OptionFrame instance = new OptionFrame();
	private JPanel contentPane;

	private JButton save_btn;

	private JButton apply_btn;

	private JButton cancel_btn;
	private JPanel menuPanel;
	
	/**
	 * 菜单栏名称和对应的设置面板类，设置面板类必须继承自OptionPanel
	 */
	private String[][] menuNames = {{"常规", "com.motey.UI.OptionGeneralPanel"},
									{"连接", "com.motey.UI.OptionLinkPanel"},
									{"提醒", "com.motey.UI.OptionRemindPanel"}};
	
	/**
	 * 存放上面定义好的菜单名所生成的实例
	 */
	private JLabel[] menus;

	/**
	 * 存放上面定义好的设置面板所生成的实例
	 */
	public OptionPanel[] optionPanels;

	public static OptionFrame getInstance(){
		
		if(instance.isDisplayable()){
			
			instance.setExtendedState(JFrame.NORMAL);//还原
			instance.toFront();
			instance.setVisible(true);
			
		}else{
			instance = new OptionFrame();
			instance.setVisible(true);
		}
		return instance;
	}
	
	
	private OptionFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//windows风格
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		setSize(550, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(true);		
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 240));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ImageIcon icon = ImageUtil.getImageIcon("/icons/tcdesktop_16.png", 30, 30);
		TopPanel topPanel = new TopPanel("用户设置", 550, 40, TopPanel.DEFAULT_BACKGROUND, icon);
		topPanel.setLayout(null);
		topPanel.setParent(this);
		topPanel.setBounds(0, 0, 550, 40);
		contentPane.add(topPanel);
		
		 
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setOpaque(false);
		buttonPanel.setBounds(110, 300, 442, 50);
		
		ButtonListener buttonListener = new ButtonListener();
		
		save_btn = new JButton("保存");
		save_btn.setActionCommand("保存");
		save_btn.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		save_btn.addActionListener(buttonListener);
		
		apply_btn = new JButton("应用");
		apply_btn.setActionCommand("应用");
		apply_btn.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		apply_btn.addActionListener(buttonListener);
		
		cancel_btn = new JButton("取消");
		cancel_btn.setActionCommand("取消");
		cancel_btn.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		cancel_btn.addActionListener(buttonListener);
		
		buttonPanel.add(save_btn);
		buttonPanel.add(apply_btn);
		buttonPanel.add(cancel_btn);
		
		contentPane.add(buttonPanel);
		
		topPanel.setDragable(this);
		
		menuPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) menuPanel.getLayout();
		flowLayout.setAlignOnBaseline(true);
		menuPanel.setBorder(null);
		menuPanel.setBounds(0, 40, 110, 310);
		initMenuComponent();
		contentPane.add(menuPanel);
		
		//设置窗口圆角
		AWTUtilities.setWindowShape(this, new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5.0D, 5.0D));
	
	}

	private void initMenuComponent() {
		
		//初始化控件数组
		menus = new JLabel[menuNames.length];
		optionPanels = new OptionPanel[menuNames.length];
		
		//遍历定义好的菜单设置项，逐个生成实例
		for(int i = 0; i < menuNames.length; i++){
			
			try {
				String menuName = menuNames[i][0];
				String className = menuNames[i][1];
				
				//实例化菜单控件
				JLabel menuLabel = new JLabel("     "+menuName);
				menuLabel.setPreferredSize(new Dimension(120, 40));
				menuLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
				
				//为菜单控件配置监听器
				menuLabel.addMouseListener(new MenuClickedListener());
				
				//将菜单加入到主面板
				menuPanel.add(menuLabel);
				
				//实例化设置面板
				OptionPanel optionPanel = (OptionPanel) Class.forName(className).newInstance();
				optionPanel.setBounds(110, 40, 440, 260);
				optionPanel.setName(menuName);

				menus[i] = menuLabel;
				optionPanels[i] = optionPanel;
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		//设置打开时默认显示的面板
		contentPane.add(optionPanels[0]);
		
		//为所有菜单控件配置背景色监听
		MyWindowUtil.setClickedChangeBackgroundListener(menus, contentPane.getBackground());
	}

	public void paint(Graphics g) {
        super.paint(g);
    }
	
	/**
	 * 按钮监听器
	 * @author Motey
	 *
	 */
	class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String command = e.getActionCommand();
			if("应用".equals(command)){
				saveAll();
			}else if("保存".equals(command)){
				saveAll();
				dispose();
			}else if("取消".equals(command)){
				dispose();
			}
			
		}
		
		public void saveAll(){
			for (OptionPanel op : optionPanels) {
				if(op != null)op.save();
			}
		}
		
	}
	
	/**
	 * 菜单栏监听器
	 * @author Motey
	 *
	 */
	class MenuClickedListener extends MouseAdapter{
		
		@Override
		public void mouseClicked(MouseEvent e){
			
			JComponent event = (JComponent) e.getComponent();
			JPanel eventPanel = null;
			
			for(int i = 0; i < menus.length; i++){
				if(event.equals(menus[i])){
					eventPanel = optionPanels[i];
					break;
				}
			}
			if(eventPanel == null)return;
			
			for(int i = 0; i < optionPanels.length; i++){
				try{
					contentPane.remove(optionPanels[i]);
				}catch (Exception e1) { }
			}
			contentPane.add(eventPanel);
			contentPane.updateUI();
		}
	}
}


