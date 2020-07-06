package com.motey.UI;

import java.awt.Dimension;

import javax.swing.JCheckBox;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import configuration.ConfigReader;
import configuration.ConfigWriter;

/**
 * 基础设置面板
 * @author Motey
 *
 */
public class OptionGeneralPanel extends OptionPanel {

	private static final long serialVersionUID = 5760189606690414280L;
	private JTextField interval_text;
	private JCheckBox autoStartup_cb;
	private JCheckBox timingRefrech_ck;
	private JCheckBox relatedRichClient_cb;
	private JTextField richClientPath_text;

	public OptionGeneralPanel() {
		
		setPreferredSize(new Dimension(440, 260));
		setOpaque(false);
		setLayout(null);
		
		boolean startupFlag = ConfigReader.isStartup();
		int interval = ConfigReader.getRefreshInterval();
		boolean isRelatedRichClient = ConfigReader.isRelatedRichClient();
		String richClientPath = ConfigReader.getRichClientPath();
		boolean refreshFlag = false;
		if(interval > 0){
			refreshFlag = true;
		}
		
		autoStartup_cb = new JCheckBox("开机时自动启动");
		autoStartup_cb.setSelected(startupFlag);
		autoStartup_cb.setOpaque(false);
		autoStartup_cb.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		autoStartup_cb.setBounds(60, 29, 133, 27);
		add(autoStartup_cb);
		
		timingRefrech_ck = new JCheckBox("定时刷新数据");
		timingRefrech_ck.setSelected(refreshFlag);
		timingRefrech_ck.setOpaque(false);
		timingRefrech_ck.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		timingRefrech_ck.setBounds(60, 76, 133, 27);
		add(timingRefrech_ck);
		
		JLabel lb1 = new JLabel("每隔");
		lb1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lb1.setBounds(190, 80, 37, 18);
		add(lb1);
		
		interval_text = new JTextField();
		interval_text.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		interval_text.setHorizontalAlignment(SwingConstants.CENTER);
		interval_text.setText(String.valueOf(interval));
		interval_text.setBounds(228, 78, 37, 24);
		add(interval_text);
		interval_text.setColumns(10);
		
		JLabel lb2 = new JLabel("分钟");
		lb2.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lb2.setBounds(270, 80, 37, 18);
		add(lb2);

		relatedRichClient_cb = new JCheckBox("关联胖客户端");
		relatedRichClient_cb.setOpaque(false);
		relatedRichClient_cb.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		relatedRichClient_cb.setBounds(60, 127, 133, 27);
		relatedRichClient_cb.setSelected(isRelatedRichClient);
		
		JLabel lb3 = new JLabel("路径");
		lb3.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lb3.setBounds(190, 131, 37, 18);
		add(lb3);
		
		richClientPath_text = new JTextField();
		richClientPath_text.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		richClientPath_text.setHorizontalAlignment(SwingConstants.LEFT);
		richClientPath_text.setBounds(228, 127, 180, 27);
		richClientPath_text.setText(richClientPath);
		add(relatedRichClient_cb);
		add(richClientPath_text);

	}

	@Override
	public void save() {
		try {
			ConfigWriter.setStartup(autoStartup_cb.isSelected());
			int interval = 0;
			if(timingRefrech_ck.isSelected()){
				interval = Integer.parseInt(interval_text.getText());
			}
			ConfigWriter.writeRefreshInteval(interval);
			ConfigWriter.writeIsRelatedRichClient(relatedRichClient_cb.isSelected());
			ConfigWriter.writeRichClientPath(richClientPath_text.getText());
		}catch(Exception e) {
			MessageDialog md = new MessageDialog(e.getMessage(), MessageDialog.WARNING, this);
			md.setVisible(true);
		}
	}
}
