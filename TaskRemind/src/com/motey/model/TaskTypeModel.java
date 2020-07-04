package com.motey.model;

import javax.swing.ImageIcon;

public class TaskTypeModel {
	
	public static final String TASK 			 = "EPMTask";
	public static final String DO 				 = "EPMDoTask";
	public static final String REVIEW 			 = "EPMReviewTask";
	public static final String PERFORM_SIGNOFF 	 = "EPMPerformSignoffTask";
	public static final String SELECT_SIGNOFF	 = "EPMSelectSignoffTask";
	public static final String ROUTE 			 = "EPMRouteTask";
	public static final String CONDITION 		 = "EPMConditionTask";
	public static final String ACKNOWLEDGE		 = "EPMAcknowledgeTask";
	public static final String MAIL 			 = "Envelope";
	
	private String taskName;
	private ImageIcon taskIcon;
	private TaskReader reader;
	private String readerClassName;
	
	public TaskTypeModel(){
		
	}
	
	public TaskTypeModel(String name){
		this.taskName = name;
	}
	
	public TaskTypeModel(String name, ImageIcon icon){
		this.taskName = name;
		this.taskIcon = icon;
	}
	
	public TaskTypeModel(String name, ImageIcon icon, String readerClassName){
		this.taskName = name;
		this.taskIcon = icon;
		this.readerClassName = readerClassName;
	}
	
	
	public String getTaskName() {
		return taskName;
	}
	public TaskTypeModel setTaskName(String taskName) {
		this.taskName = taskName;
		return this;
	}
	public ImageIcon getTaskIcon() {
		return taskIcon;
	}
	public TaskTypeModel setTaskIcon(ImageIcon taskIcon) {
		this.taskIcon = taskIcon;
		return this;
	}
	public TaskReader getReader() {
		
		if(reader == null){
			try {
				reader = (TaskReader) Class.forName(readerClassName).newInstance();
				reader.setTaskTypeModel(this);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return reader;
	}
	public TaskTypeModel setReader(TaskReader reader) {
		this.reader = reader;
		return this;
	}

}
