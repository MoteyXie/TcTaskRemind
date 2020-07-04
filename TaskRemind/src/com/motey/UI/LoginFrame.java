package com.motey.UI;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.motey.manager.LoginManager;
import com.motey.utils.MyWindowUtil;
import com.sun.awt.AWTUtilities;

import configuration.ConfigWriter;

public class LoginFrame extends JFrame {

	private static final long serialVersionUID = -2166825730704095257L;
	private JPanel contentPane;
	protected boolean isMoved;
	protected Point pre_point;
	protected Point end_point;
	private LoginPanel loginPanel;
	private LoginManager loginController;
	private Tray tray;
	private boolean isSuccess = false;
	private TopPanel topPanel;

	public LoginFrame(LoginManager loginController, final Tray tray) {
		
		this.tray = tray;
		this.loginController = loginController;
		loginController.setRelatedDialog(this);
		
		//windows风格
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		setUndecorated(true);
		setSize(550, 350);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setOpaque(false);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		topPanel = new TopPanel("", 550, 40, null, null);
		topPanel.setLayout(null);
		topPanel.setParent(this);
		topPanel.setBounds(0, 0, 550, 40);
		contentPane.add(topPanel);
		
		loginPanel = new LoginPanel();
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosed(WindowEvent e) {
				
				if(!isSuccess){
					tray.close();
				}
				
			}
		});
		
		loginPanel.setBounds(0, 0, getWidth(), getHeight());
		contentPane.add(loginPanel);
		
		MyWindowUtil.setDragable(this);
		
		//设置窗口圆角
		AWTUtilities.setWindowShape(this, new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5.0D, 5.0D));
		
		setActionListener();
		
		getRootPane().setDefaultButton(loginPanel.inputPanel.login_btn);
	}
	
	public void display(){
		
		setVisible(true);
		setExtendedState(JFrame.NORMAL);
		toFront();
	}
	
	public void loginSuccess(){
		isSuccess = true;
		tray.loginSuccess();
	}
	
	public void loginFailed(String errorMsg){
		loginController.stopLogin();
		loginPanel.resetInputPanel();
		topPanel.updateUI();
		JOptionPane.showMessageDialog(this, errorMsg, "登录失败", JOptionPane.ERROR_MESSAGE); 
	}
	
	public boolean isSuccess(){
		return isSuccess;
	}
	
	private void setActionListener() {
		
		final UserInputPanel inputPanel =  loginPanel.inputPanel;
		final LogingPanel logingPanel = loginPanel.logingPanel;
		
		//登录按钮监听
		inputPanel.login_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				loginPanel.startLogingAnimation();
				topPanel.updateUI();
				String name = inputPanel.getUserName();
				String password = inputPanel.getPassword();
				
				System.out.println("登录中...\nuser="+name+" , password="+password);
				loginController.Login(name, password);
				
				//如果不记住密码，就把密码清除
				if(!inputPanel.isRememberPassword()){
					password = "";
				}
				
				//记录用户信息
				ConfigWriter.writeLogginer(name, password, inputPanel.getUserIconPath());
				ConfigWriter.writeIsAutoLogin(inputPanel.isAutoLogin());
			}
		});
		
		//取消登录按钮监听
		logingPanel.cancel_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginController.stopLogin();
				loginPanel.resetInputPanel();
			}
		});
		
		//设置按钮监听
		inputPanel.linkOption_lb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				OptionFrame.getInstance();
			}
		});
		
	}
	
	public Tray getTray(){
		return tray;
	}

	public void paint(Graphics g) {
        super.paint(g);
    }
	
}
