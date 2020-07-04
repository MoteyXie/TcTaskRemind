package com.motey.UI;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.motey.model.TaskModel;
import com.motey.utils.ImageUtil;
import com.motey.utils.SoaTaskUtil;

import javax.swing.JTextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.UIManager;

public class TaskContentPanel extends JPanel {

	private static final long serialVersionUID = -3033720561148258426L;
	private JLabel selectedTaskDateLabel;
	private JLabel selectedTaskOwnerLabel;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private JTextArea description;
	private JLabel lblReferenceObjects;
	private TaskModel taskModel;
	private FlowChartPanel flowChartPanel;
	private WaittingPanel waittingPanel;
	private JPanel panel;
	
	public TaskContentPanel() {
		
		setBackground(new Color(255, 255, 255));
		setBounds(24, 36, 460, 558);
		setLayout(null);
		
		JLabel label = new JLabel("\u53D1\u8D77\u8005\uFF1A");
		label.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label.setBounds(14, 10, 66, 18);
		add(label);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.GRAY);
		separator.setBackground(Color.GRAY);
		separator.setBounds(12, 35, 440, 1);
		add(separator);
		
		selectedTaskDateLabel = new JLabel("2017-08-31 18:00");
		selectedTaskDateLabel.setForeground(Color.GRAY);
		selectedTaskDateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		selectedTaskDateLabel.setBounds(324, 11, 128, 18);
		add(selectedTaskDateLabel);
		
		selectedTaskOwnerLabel = new JLabel("N/A");
		selectedTaskOwnerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		selectedTaskOwnerLabel.setBounds(83, 10, 177, 18);
		add(selectedTaskOwnerLabel);
		
		JPanel targetFilePanel = new JPanel();
		targetFilePanel.setOpaque(false);
		targetFilePanel.setBounds(12, 41, 440, 28);
		add(targetFilePanel);
		targetFilePanel.setLayout(null);
		
		lblReferenceObjects = new JLabel("\u5173\u8054\u5BF9\u8C61");
		lblReferenceObjects.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lblReferenceObjects.setBounds(3, 5, 101, 18);
		lblReferenceObjects.setIcon(ImageUtil.getImageIcon("/icons/attach.png", 20, 20));
		targetFilePanel.add(lblReferenceObjects);
		
		lblReferenceObjects.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e){
				
				ObjectListDialog sod = ObjectListDialog.getInstance();
				sod.setObject(taskModel.getTargets());
				sod.setLocation(e.getXOnScreen(), e.getYOnScreen());
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblReferenceObjects.setForeground(Color.RED);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				lblReferenceObjects.setForeground(Color.BLACK);
			}
		});
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.GRAY);
		separator_1.setBackground(Color.GRAY);
		separator_1.setBounds(12, 75, 440, 1);
		add(separator_1);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBorder(UIManager.getBorder("TitledBorder.border"));
//		panel.setBounds(14, 85, 438, 363);
		panel.setBounds(14, 85, 438, 483);
		add(panel);
		panel.setLayout(null);
		
		description = new JTextArea();
		description.setEnabled(false);
		description.setBorder(UIManager.getBorder("TitledBorder.border"));
		description.setBackground(new Color(255, 255, 240));
		description.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		description.setLineWrap(true);
		description.setBounds(14, 41, 410, 99);
		panel.add(description);
		
		JLabel label_1 = new JLabel("\u63CF\u8FF0\uFF1A");
		label_1.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		label_1.setBounds(14, 13, 72, 18);
		panel.add(label_1);
		
		flowChartPanel = new FlowChartPanel();
		flowChartPanel.setBackground(new Color(255, 255, 255));
		flowChartPanel.setBounds(14, 150, 410, 315);
		panel.add(flowChartPanel);
		
		waittingPanel = new WaittingPanel();
		waittingPanel.setBackground(new Color(255, 255, 255));
		waittingPanel.setBounds(14, 150, 410, 315);
		waittingPanel.setVisible(false);
		panel.add(waittingPanel);
	}
	
	public void setTaskOwner(String ownerName){
		selectedTaskOwnerLabel.setText(ownerName);
	}

	public void setDate(Date date) {
		selectedTaskDateLabel.setText(dateFormat.format(date));
	}
	
	public void setTask(final TaskModel taskModel){
		
		this.taskModel = taskModel;
		if(!taskModel.getType().startsWith("EPM")){
			flowChartPanel.setTaskModels(null);
			return;
		}
		
		new Thread(){
			
			public void run(){
				flowChartPanel.setVisible(false);
				waittingPanel.setVisible(true);
				panel.updateUI();
				
				try {
					
					ArrayList<TaskNode> taskNodes = SoaTaskUtil.getFlowTaskNodes(taskModel.getTaskObject());
					
					flowChartPanel.setTaskModels(taskNodes);
					
					flowChartPanel.setVisible(true);
					waittingPanel.setVisible(false);
					panel.updateUI();
					
				}catch(Exception e){
					e.printStackTrace();
					
					flowChartPanel.setVisible(true);
					waittingPanel.setVisible(false);
					panel.updateUI();
				}
			}
			
		}.start();
		
	}
}
