package com.motey.UI;

import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.motey.model.MyProperty;
import com.motey.model.TaskModel;
import com.motey.utils.SoaTaskUtil;

import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class ActionDoTaskDialog extends ActionDialog{

	private static final long serialVersionUID = -5828469130490920872L;
	
	private JPanel contentPane;

	private JTextArea textArea_comment;

	private JButton btn_complete;

	private JButton btn_close;

	private String taskStatus;

	public ActionDoTaskDialog(MainListFrame parent, TaskModel taskModel) {
		super(parent, taskModel);
		
		setSize(450, 210);
		setLocationRelativeTo(parent);
		TopPanel4Dialog topPanel = new TopPanel4Dialog("执行任务", 450, 35, Color.darkGray, taskModel.getIcon());
		topPanel.setTitleForeground(Color.WHITE);
		topPanel.setFontSize(16);
		topPanel.setParent(this);
		topPanel.setDragable();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		contentPane.add(topPanel);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_comment = new JPanel();
		panel_comment.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel_comment.setBounds(14, 48, 422, 110);
		contentPane.add(panel_comment);
		panel_comment.setLayout(null);
		
		JLabel lbl_comment = new JLabel("注释：");
		lbl_comment.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lbl_comment.setBounds(14, 13, 45, 18);
		panel_comment.add(lbl_comment);
		
		textArea_comment = new JTextArea();
		textArea_comment.setLineWrap(true);
		textArea_comment.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		textArea_comment.setBounds(60, 12, 348, 88);
		panel_comment.add(textArea_comment);
		
		JPanel panel_button = new JPanel();
		panel_button.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel_button.setBounds(14, 163, 422, 41);
		contentPane.add(panel_button);
		
		btn_complete = new JButton("完成");
		btn_complete.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btn_complete.addActionListener(this);
		panel_button.add(btn_complete);
		
		btn_close = new JButton("关闭");
		btn_close.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btn_close.addActionListener(this);
		panel_button.add(btn_close);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object eventObject = e.getSource();
		
		if(eventObject.equals(btn_complete)){
			
			SoaTaskUtil.doTaskComplete(taskModel.getTaskObject(), textArea_comment.getText());
			
			try {
				taskStatus = MyProperty.getStringProperty(taskModel.getTaskObject(), "fnd0Status");
				if("错误".equals(taskStatus) || "Error".equals(taskStatus)){
					isSuccess = false;
					result = MyProperty.getStringProperty(taskModel.getTaskObject(), "fnd0TaskExecutionErrors");
				}else{
					isSuccess = true;
					result = taskStatus;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
//			try {
//				parent.refreshTaskList();
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
			dispose();
			
		}else if(eventObject.equals(btn_close)){
			
			dispose();
			
		}
	}
	

}
