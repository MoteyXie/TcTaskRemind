package com.motey.UI;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class LoginPanel extends JPanel {

	private static final long serialVersionUID = -8688092829888560642L;

	public UserInputPanel inputPanel;
	public LogingPanel logingPanel;

	public LoginPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(300, 170));
		add(panel, BorderLayout.NORTH);
		panel.setLayout(null);
		
		inputPanel = new UserInputPanel();
		logingPanel = new LogingPanel(inputPanel);
		
		add(inputPanel, BorderLayout.CENTER);
	}

	public void startLogingAnimation(){
		remove(inputPanel);
		add(logingPanel, BorderLayout.CENTER);
		updateUI();
		logingPanel.startAnimation();
		logingPanel.updateUI();
	}
	
	public void resetInputPanel(){
		remove(logingPanel);
		add(inputPanel, BorderLayout.CENTER);
		updateUI();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/background2.jpg"));

		//调整图像的分辨率以适应容器
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(getWidth()+2,getHeight(),Image.SCALE_FAST)); 
//		g.drawImage(imageIcon.getImage(), 0, 0, getWidth(),getHeight(), 0, 0, getWidth(),getHeight(), null);
		
		//防止边缘白边，坐标设置为-1
		imageIcon.paintIcon(this, g, -1, -1);
	}
	
}
