package com.motey.model;

public abstract class TaskReader {
	
	protected String typeName;
	protected TaskTypeModel taskTypeModel;
	
	public TaskTypeModel getTaskTypeModel() {
		return taskTypeModel;
	}
	public void setTaskTypeModel(TaskTypeModel taskTypeModel) {
		this.taskTypeModel = taskTypeModel;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public abstract TaskModel[] read();

}
