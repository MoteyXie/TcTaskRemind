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

public class ActionConditionDialog extends ActionDialog{

	private static final long serialVersionUID = -5828469130490920872L;
	
	public static final ImageIcon APPROVED_ICON = ImageUtil.getImageIcon("/icons/approved_32.png", 20, 20);
	public static final ImageIcon REJECTED_ICON = ImageUtil.getImageIcon("/icons/rejected_16.png", 20, 20);
	public static final ImageIcon NODECISION_ICON = ImageUtil.getImageIcon("/icons/nodecision_32.png", 20, 20);
	private JPanel contentPane;

	private JRadioButton[] rb_conditions;

	private JTextArea textArea_comment;

	private JButton btn_enter;

	private JButton btn_cancel;

	private String taskStatus;

	public ActionConditionDialog(MainListFrame parent, TaskModel taskModel) {
		super(parent, taskModel);
		
		setSize(450, 226);
		setLocationRelativeTo(parent);
		TopPanel4Dialog topPanel = new TopPanel4Dialog("条件任务", 450, 35, Color.darkGray, taskModel.getIcon());
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
		panel_decision.setBounds(14, 48, 204, 120);
		contentPane.add(panel_decision);
		
		String[] conditions = SoaTaskUtil.getConditions(taskModel.getTaskObject());
		rb_conditions = new JRadioButton[conditions.length];
		
		ButtonGroup bg = new ButtonGroup();
		for(int i = 0; i < conditions.length; i++){
			rb_conditions[i] = new JRadioButton(conditions[i]);
			rb_conditions[i].setFont(new Font("微软雅黑", Font.PLAIN, 15));
//			rb_approved.addActionListener(this);
			panel_decision.add(rb_conditions[i]);
			bg.add(rb_conditions[i]);
		}
		
		JPanel panel_comment = new JPanel();
		panel_comment.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel_comment.setBounds(232, 48, 204, 120);
		contentPane.add(panel_comment);
		panel_comment.setLayout(null);
		
		JLabel lbl_comment = new JLabel("注释：");
		lbl_comment.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lbl_comment.setBounds(14, 13, 45, 18);
		panel_comment.add(lbl_comment);
		
		textArea_comment = new JTextArea();
		textArea_comment.setLineWrap(true);
		textArea_comment.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		textArea_comment.setBounds(60, 12, 131, 97);
		panel_comment.add(textArea_comment);
		
		JPanel panel_button = new JPanel();
		panel_button.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel_button.setBounds(14, 175, 422, 41);
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
	
	public String getSelectedCondition(){
		for (JRadioButton rb : rb_conditions) {
			if(rb.isSelected())return rb.getText();
		}
		return null;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object eventObject = e.getSource();
		
		if(eventObject.equals(btn_enter)){
			
			String condition = getSelectedCondition();
			
			if(condition == null){
				new MessageDialog("请选择一个条件！", MessageDialog.INFOMATION, ActionConditionDialog.this);
				return ;
			}
			
			SoaTaskUtil.conditionTaskAction(taskModel.getTaskObject(), condition, textArea_comment.getText());
			
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
		}
	}
}
