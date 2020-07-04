package com.motey.UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.JLabel;
import javax.swing.JTextField;

import configuration.ConfigReader;
import configuration.ConfigWriter;

import javax.swing.JButton;
import java.awt.Color;

/**
 * 连接设置
 * @author Motey
 *
 */
public class OptionLinkPanel extends OptionPanel {

	private static final long serialVersionUID = 373642551110050255L;
	private JTextField serverIP;
	private JTextField serverHost;
	private JLabel lblTestInfo;

	public OptionLinkPanel() {
		
		setPreferredSize(new Dimension(440, 260));
		setOpaque(false);
		setLayout(null);
		
		String[] linkMsg = ConfigReader.getLink();
		
		JLabel label = new JLabel("服务器地址：");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label.setBounds(60, 34, 90, 18);
		add(label);
		
		serverIP = new JTextField();
		serverIP.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		serverIP.setBounds(150, 32, 173, 24);
		serverIP.setText(linkMsg[0]);
		add(serverIP);
		serverIP.setColumns(10);
		
		JLabel label_1 = new JLabel("端口号：");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_1.setBounds(89, 79, 69, 18);
		add(label_1);
		
		serverHost = new JTextField();
		serverHost.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		serverHost.setColumns(10);
		serverHost.setBounds(150, 77, 62, 24);
		serverHost.setText(linkMsg[1]);
		add(serverHost);
		
		JButton linkTest_btn = new JButton("网络测试");
		linkTest_btn.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		linkTest_btn.setBounds(150, 126, 113, 27);
		add(linkTest_btn);
		
		lblTestInfo = new JLabel("");
		lblTestInfo.setForeground(Color.RED);
		lblTestInfo.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lblTestInfo.setBounds(150, 166, 173, 18);
		add(lblTestInfo);
		
		linkTest_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//超时应该在3钞以上     
				int timeOut =  3000 ;
				// 当返回值是true时，说明host是可用的，false则不可
				try {
					boolean status = InetAddress.getByName(serverIP.getText()).isReachable(timeOut);
					lblTestInfo.setText(status ? "连接正常":"连接失败");
				} catch (Exception e1) {
					e1.printStackTrace();
				} 
		        
			}
		});

	}

	@Override
	public void save() {
		ConfigWriter.writeLink(serverIP.getText(), serverHost.getText());
		
	}
}
