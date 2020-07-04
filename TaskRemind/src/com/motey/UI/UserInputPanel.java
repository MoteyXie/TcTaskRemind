package com.motey.UI;

import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import configuration.ConfigReader;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class UserInputPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8150961483811878535L;
	
	public static final String DEFAULT_ICON_PATH = "/images/teamcenter_app_90.gif";

	public JComboBox<String> userName_cb;
	
	public JPasswordField password_text;

	public JCheckBox rememberPassword_ck;

	public JCheckBox autoLogin_ck;

	public JLabel linkOption_lb;

	public JButton login_btn;

	private JLabel userIconComponent;

	private ImageIcon userIcon;

	private String iconPath;

	public UserInputPanel() {
		setBackground(new Color(245, 245, 245));

		setLayout(null);
		userIconComponent = new JLabel("");
		userIconComponent.setBounds(76, 26, 90, 90);
		
		add(userIconComponent);
		
		userName_cb = new JComboBox<>();
		userName_cb.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		userName_cb.setEditable(true);
		userName_cb.setBounds(180, 26, 206, 33);
		
		String[] users = ConfigReader.getLoginerNames();
		userName_cb.setModel(new DefaultComboBoxModel<>(users));
		userName_cb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeUserMessage();
			}
		});
		
		add(userName_cb);
		
		password_text = new JPasswordField();
		password_text.setBounds(180, 59, 206, 33);
		add(password_text);
		
		rememberPassword_ck = new JCheckBox("记住密码");
		rememberPassword_ck.setOpaque(false);
		rememberPassword_ck.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		rememberPassword_ck.setBounds(176, 95, 90, 27);
		changeUserMessage();
		
		add(rememberPassword_ck);
		
		autoLogin_ck = new JCheckBox("自动登录");
		autoLogin_ck.setOpaque(false);
		autoLogin_ck.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		autoLogin_ck.setBounds(303, 95, 90, 27);
		
		boolean flag = ConfigReader.isAutoLogin();
		autoLogin_ck.setSelected(flag);
		add(autoLogin_ck);
		
		linkOption_lb = new JLabel("连接设置");
		linkOption_lb.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		linkOption_lb.setForeground(new Color(0, 102, 204));
		linkOption_lb.setBounds(400, 32, 80, 18);
		add(linkOption_lb);
		
		login_btn = new JButton("登录");
		
		login_btn.setBackground(new Color(135, 206, 250));
		login_btn.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		login_btn.setBounds(180, 131, 206, 41);
		add(login_btn);
		
	}
	
	private void changeUserMessage() {
		String name = userName_cb.getSelectedItem().toString();
		String password = ConfigReader.getPassword(name);
		iconPath = ConfigReader.getIconPath(name);
		password_text.setText(password);
		
		if(iconPath == null || iconPath.length() == 0){
			iconPath = DEFAULT_ICON_PATH;
		}
		
		userIcon = new ImageIcon(getClass().getResource(iconPath));
		
		if(userIcon == null){
			iconPath = DEFAULT_ICON_PATH;
			userIcon = new ImageIcon(getClass().getResource(iconPath));
		}
		
		userIcon.setImage(userIcon.getImage().getScaledInstance(90,90,Image.SCALE_FAST)); 
		userIconComponent.setIcon(userIcon);
		
		rememberPassword_ck.setSelected(password != null && password.length() > 0);
	}

	public String getUserName(){
		return userName_cb.getSelectedItem().toString();
	}
	
	public String getPassword(){
		return String.valueOf(password_text.getPassword());
	}
	
	public String getUserIconPath(){
		return iconPath;
	}
	
	public ImageIcon getUserIcon(){
		return userIcon;
	}
	
	public boolean isRememberPassword(){
		return rememberPassword_ck.isSelected();
	}
	
	public boolean isAutoLogin(){
		return autoLogin_ck.isSelected();
	}
}
