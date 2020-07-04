package com.motey.UI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.motey.model.TaskTypeModel;
import com.motey.utils.ImageUtil;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.Font;

public class TaskMenu extends JPanel {

	public static ImageIcon ICON_MAIL = ImageUtil.getImageIcon("/icons/notifytask_32.png", 20, 20);
	public static ImageIcon ICON_FLOW = ImageUtil.getImageIcon("/icons/processdesignerapplication_32.png", 20, 20);
	private static final long serialVersionUID = -396921343988810450L;
	private JLabel countLabel;
	private TaskTypeModel model;
	
	public TaskMenu(TaskTypeModel model){
		this(model.getTaskName(), model.getTaskIcon());
		this.model = model;
	}
	/**
	 * @wbp.parser.constructor
	 */
	public TaskMenu(String name, ImageIcon icon, int count){
		this(name, icon);
		setCount(count);
	}

	public TaskMenu(String name, ImageIcon icon) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		setPreferredSize(new Dimension(170, 30));
		
		JLabel space = new JLabel("    ");
		space.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		add(space);
		
		JLabel nameLabel = new JLabel(name);
		nameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		setName(name);
		nameLabel.setIcon(icon);
		add(nameLabel);
		
		countLabel = new JLabel("");
		countLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		add(countLabel);

	}
	
	public void setCount(int count){
		if(count <= 0)countLabel.setText("");
		else countLabel.setText("("+count+")");
	}
	
	public TaskTypeModel getTaskModel(){
		return model;
	}
	
	public String getTaskName(){
		return model.getTaskName();
	}
	
	public void clicked(){
		setOpaque(true);
		setBackground(new Color(150,200, 255));
	}
	
	public void unClicked(){
		setOpaque(false);
		setBackground(Color.WHITE);
	}

}
