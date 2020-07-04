package com.motey.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import com.motey.model.TaskModel;
import com.motey.model.TaskTypeModel;

public class TaskListPanel extends JPanel {

	private static final long serialVersionUID = 1224591615043582368L;

	private TaskTypeModel taskTypeModel;
	private HashMap<TaskModel, TaskPanel> taskPanels = new HashMap<>();
	private int column = 9;
	
	public TaskListPanel() {
		
		setBackground(new Color(255, 255, 255));
		setLayout(new GridLayout(column, 1, 0, 0));
		
	}
	
	public void addTask(TaskPanel taskPanel){
		
		if(getTaskCount() >= 9){
			setLayout(new GridLayout(getTaskCount()+1, 1, 0, 0));
			int height = 58 * getTaskCount();
			setPreferredSize(new Dimension(getWidth(), height));
		}
		add(taskPanel);
		taskPanels.put(taskPanel.getTask(), taskPanel);
	}
	
	public int getTaskCount(){
		return getComponentCount();
	}
	
	public TaskTypeModel getTaskTypeModel() {
		return taskTypeModel;
	}
	
	public Set<TaskModel> getTaskModels(){
		return taskPanels.keySet();
	}
	
	public TaskPanel getTaskPanel(TaskModel taskModel){
		return taskPanels.get(taskModel);
	}
	
	public List<TaskModel> getTaskModelArray(){
		
		List<TaskModel> list = new ArrayList<>();
		
		Component[] temp = getComponents();
		for (Component component : temp) {
			if(component instanceof TaskPanel){
				list.add(((TaskPanel)component).getTask());
			}
		}
		
		return list;
	}

	public void setTaskTypeModel(TaskTypeModel taskTypeModel) {
		this.taskTypeModel = taskTypeModel;
	}
	
	@Override
	public void removeAll(){
		super.removeAll();
		taskPanels.clear();
	}

}
