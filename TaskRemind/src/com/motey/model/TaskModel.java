package com.motey.model;

import java.util.Date;

import javax.swing.ImageIcon;

import com.teamcenter.soa.client.model.ModelObject;

public class TaskModel {
	
	private ModelObject taskObject;
	private TaskTypeModel taskTypeModel;
	private String owner;
	private String name;
	private String parentName;
	private String displayName;
	private Date startDate;
	private String type;
	private ImageIcon icon;
	private ImageIcon doneIcon;
	private boolean isReaded = false;
	private String uid;
	private String readFlagPropName;

	private ModelObject[] targets;
	
	public TaskModel(){
		
	}
	
	public TaskModel(ModelObject task){
		this.taskObject = task;
		uid = task.getUid();
	}
	
	public ModelObject getTaskObject(){
		return taskObject;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getDisplayName() {
		
		displayName = name + "-" + parentName;
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
		icon = TaskIcon.getIcon(type);
		doneIcon = TaskIcon.getDoneIcon(type);
	}
	
	public void setIcon(ImageIcon icon){
		this.icon = icon;
	}
	
	public ImageIcon getIcon(){
		return icon;
	}
	
	public void setDoneIcon(ImageIcon doneIcon){
		this.doneIcon = doneIcon;
	}
	
	public ImageIcon getDoneIcon(){
		return doneIcon;
	}
	
	public ModelObject[] getTargets(){
		return targets;
	}
	
	public void setTargets(ModelObject[] targets){
		this.targets = targets;
	}
	
	public boolean isReaded() {
		return isReaded;
	}

	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}
	
	public TaskTypeModel getTaskTypeModel() {
		return taskTypeModel;
	}

	public void setTaskTypeModel(TaskTypeModel taskTypeModel) {
		this.taskTypeModel = taskTypeModel;
	}
	
	public String getUid(){
		if(uid == null && taskObject != null)uid = taskObject.getUid();
		return uid;
	}
	
	public void setUid(String uid){
		this.uid = uid;
	}
	
	public String getReadFlagPropName() {
		return readFlagPropName;
	}

	public void setReadFlagPropName(String readFlagPropName) {
		this.readFlagPropName = readFlagPropName;
	}
	
	public boolean equals(Object obj){
		
		boolean flag = false;
		
		if(obj == null)return false;
		
		try{
			if(obj instanceof TaskModel){
				
				TaskModel temp = (TaskModel) obj;
				
				if(temp.getUid() != null && getUid() != null){
					
					flag = temp.getUid().equals(getUid());
					
				}else if(getName().equals(temp.getName()) && 
						getParentName().equals(temp.getParentName()) &&
						getOwner().equals(temp.getOwner()) &&
						getStartDate().equals(temp.getStartDate())  ){
					
					flag = true;
				}
				
			}else{
				flag = false;
			}
		}catch(Exception e){
			flag = false;
		}
		
		
		return flag;
		
	}
	
}
