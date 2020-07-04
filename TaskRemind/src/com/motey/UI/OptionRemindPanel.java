package com.motey.UI;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextField;

import configuration.ConfigReader;
import configuration.ConfigWriter;

public class OptionRemindPanel extends OptionPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6777526159677866958L;
	private JTextField time_text;

	public OptionRemindPanel() {
		setPreferredSize(new Dimension(440, 260));
		setOpaque(false);
		setLayout(null);
		
		int time = ConfigReader.getAlertDialogAutoCloseTime();
		
		JLabel label = new JLabel("\u6301\u7EED\u65F6\u95F4\uFF1A");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label.setBounds(60, 29, 75, 18);
		add(label);
		
		time_text = new JTextField();
		time_text.setToolTipText("\u503C\u4E3A0\u6216\u672A\u586B\u5199\u65F6\u8868\u793A\u6C38\u4E0D\u5173\u95ED");
		time_text.setText(String.valueOf(time));
		time_text.setBounds(137, 27, 63, 24);
		add(time_text);
		time_text.setColumns(10);
		
		JLabel label_1 = new JLabel("\u79D2");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_1.setBounds(206, 30, 34, 18);
		add(label_1);
	}

	@Override
	public void save() {
		
		int time = 0;
		try{
			time = Integer.parseInt(time_text.getText());
			if(time < 0)time = 0;
			ConfigWriter.setAlertDialogAutoCloseTime(time);
			
		}catch(Exception e){
		}
		
		
	}
}
