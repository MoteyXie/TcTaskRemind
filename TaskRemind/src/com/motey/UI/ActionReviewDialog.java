package com.motey.UI;

import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.motey.model.MyProperty;
import com.motey.model.TaskModel;
import com.motey.utils.ImageUtil;
import com.motey.utils.SoaTaskUtil;

import javax.swing.JRadioButton;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;

public class ActionReviewDialog extends ActionDialog{

	private static final long serialVersionUID = -5828469130490920872L;
	
	public static final ImageIcon APPROVED_ICON = ImageUtil.getImageIcon("/icons/approved_32.png", 20, 20);
	public static final ImageIcon REJECTED_ICON = ImageUtil.getImageIcon("/icons/rejected_16.png", 20, 20);
	public static final ImageIcon NODECISION_ICON = ImageUtil.getImageIcon("/icons/nodecision_32.png", 20, 20);
	private JPanel contentPane;

	private JRadioButton rb_approved;

	private JRadioButton rb_rejected;

	private JRadioButton rb_nodecision;

	private JTextArea textArea_comment;

	private JButton btn_enter;

	private JButton btn_cancel;

	private JLabel icon_decision;

	private String taskStatus;

	public ActionReviewDialog(MainListFrame parent, TaskModel taskModel) {
		super(parent, taskModel);
	
		setSize(450, 250);
		setLocationRelativeTo(parent);
		TopPanel4Dialog topPanel = new TopPanel4Dialog("审核任务", 450, 35, Color.darkGray, taskModel.getIcon());
		topPanel.setTitleForeground(Color.WHITE);
		topPanel.setFontSize(16);
		topPanel.setParent(this);
		topPanel.setDragable();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		contentPane.add(topPanel);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_decision = new JPanel();
		panel_decision.setBorder(UIManager.getBorder("TitledBorder.border"));
		FlowLayout fl_panel_decision = (FlowLayout) panel_decision.getLayout();
		fl_panel_decision.setAlignment(FlowLayout.LEFT);
		panel_decision.setBounds(14, 72, 129, 113);
		contentPane.add(panel_decision);
		
		icon_decision = new JLabel("");
		icon_decision.setIcon(NODECISION_ICON);
		icon_decision.setBounds(14, 41, 72, 18);
		contentPane.add(icon_decision);
		
		rb_approved = new JRadioButton("批准");
		rb_approved.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		rb_approved.addActionListener(this);
		panel_decision.add(rb_approved);
		
		rb_rejected = new JRadioButton("拒绝");
		rb_rejected.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		rb_rejected.addActionListener(this);
		panel_decision.add(rb_rejected);
		
		rb_nodecision = new JRadioButton("不作决定");
		rb_nodecision.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		rb_nodecision.addActionListener(this);
		panel_decision.add(rb_nodecision);
		rb_nodecision.setSelected(true);
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(rb_approved);
		bg.add(rb_rejected);
		bg.add(rb_nodecision);
		
		JPanel panel_comment = new JPanel();
		panel_comment.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel_comment.setBounds(157, 72, 279, 113);
		contentPane.add(panel_comment);
		panel_comment.setLayout(null);
		
		JLabel lbl_comment = new JLabel("注释：");
		lbl_comment.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lbl_comment.setBounds(14, 13, 45, 18);
		panel_comment.add(lbl_comment);
		
		textArea_comment = new JTextArea();
		textArea_comment.setLineWrap(true);
		textArea_comment.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		textArea_comment.setBounds(60, 12, 205, 88);
		panel_comment.add(textArea_comment);
		
		JPanel panel_button = new JPanel();
		panel_button.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel_button.setBounds(14, 196, 422, 41);
		contentPane.add(panel_button);
		
		btn_enter = new JButton("确定");
		btn_enter.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btn_enter.addActionListener(this);
		panel_button.add(btn_enter);
		
		btn_cancel = new JButton("取消");
		btn_cancel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		btn_cancel.addActionListener(this);
		panel_button.add(btn_cancel);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object eventObject = e.getSource();
		
		if(eventObject.equals(btn_enter)){
			
			if(rb_approved.isSelected()){
				SoaTaskUtil.reviewTaskApproved(taskModel.getTaskObject(), textArea_comment.getText());
			}else if(rb_rejected.isSelected()){
				SoaTaskUtil.reviewTaskRejected(taskModel.getTaskObject(), textArea_comment.getText());
			}
			
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
			
			dispose();
			
		}else if(eventObject.equals(btn_cancel)){
			
			dispose();
			
		}else if(eventObject.equals(rb_approved)){
			
			icon_decision.setIcon(APPROVED_ICON);
			
		}else if(eventObject.equals(rb_rejected)){
			
			icon_decision.setIcon(REJECTED_ICON);
			
		}else if(eventObject.equals(rb_nodecision)){
			
			icon_decision.setIcon(NODECISION_ICON);
			
		}
	}
}
