package com.motey.UI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LogingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel userIcon;
	public JButton cancel_btn;
	private UserInputPanel inputPanel = null;

	/**
	 * Create the panel.
	 */
	public LogingPanel(UserInputPanel inputPanel) {
		
		this.inputPanel = inputPanel;
		
		setBackground(new Color(245, 245, 245));

		setLayout(null);
		userIcon = new JLabel("");
		
		userIcon.setBounds(76, 26, 90, 90);
		add(userIcon);
		
		cancel_btn = new JButton("取消");
		
		cancel_btn.setBackground(new Color(135, 206, 250));
		cancel_btn.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		cancel_btn.setBounds(180, 131, 206, 41);
		add(cancel_btn);
		
	}
	
	public void startAnimation(){
		
		new Thread(){
			
			public void run(){
				
				int stepcount = 100;
				int stepTime = 3;
				
				int x0 = 76;
				int y0 = 26;
				
				int x = 230 -x0;
				int y = 13 - y0;
				
				userIcon.setIcon(inputPanel.getUserIcon());
				for(int i = 1; i <= stepcount; i++){
					
					userIcon.setBounds(x0+i*x/stepcount, y0+i*y/stepcount, 90, 90);
					try {
						Thread.sleep(stepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}.start();
		
	}

}
