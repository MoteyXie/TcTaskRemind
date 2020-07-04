package com.motey.model;

import java.util.Date;

import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.Folder;
import com.teamcenter.soa.client.model.strong.TaskInbox;
import com.teamcenter.soa.exceptions.NotLoadedException;

public class FlowTaskReader extends TaskReader {

	@Override
	public TaskModel[] read() {
		
		TaskInbox inbox = getTaskInbox();
		
		Folder performTaskFolder = getPerformTaskFolder(inbox);
		
		ModelObject[] performTasks = getPerformTasks(performTaskFolder);
		
		MyProperty.refreshObjects(performTasks);
		
		TaskModel[] taskModels = new TaskModel[performTasks.length];
		
		for (int i = 0; i < taskModels.length; i++) {
			
			try {
				String name				 = MyProperty.getStringProperty(performTasks[i], "object_name");
				String owner 			 = MyProperty.getStringProperty(performTasks[i], "fnd0Performer");
				Date startDate 			 = MyProperty.getDateProperty(performTasks[i], "fnd0StartDate");
				String parentName 		 = MyProperty.getStringProperty(performTasks[i], "root_task");
				ModelObject[] targets	 = MyProperty.getModelObjectArrayProperty(performTasks[i], "root_target_attachments");
				String taskType			 = MyProperty.getStringProperty(performTasks[i], "task_type");
				boolean isReaded 		 = MyProperty.getBooleanProperty(performTasks[i], "viewed_by_me");
				taskModels[i] = new TaskModel(performTasks[i]);
				taskModels[i].setReadFlagPropName("viewed_by_me");
				taskModels[i].setName(name);
				taskModels[i].setOwner(owner);
				taskModels[i].setStartDate(startDate);
				taskModels[i].setParentName(parentName);
				taskModels[i].setTargets(targets);
				taskModels[i].setType(taskType);
				taskModels[i].setTaskTypeModel(getTaskTypeModel());
				taskModels[i].setReaded(isReaded);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	
		return taskModels;
	}
	
	/**
	 * 获取要执行的任务
	 * @return 要执行的任务
	 */
	public ModelObject[] getPerformTasks(Folder performTaskFolder){
		
		ModelObject[] tasks = null;
		try{
	    	tasks = MyProperty.getContents(performTaskFolder);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return tasks;
	}
	
	public Folder getPerformTaskFolder(TaskInbox inbox){
		
		Folder folder = null;
		try {
			folder = (Folder) MyProperty.getModelObjectArrayProperty(inbox, "contents")[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folder;
	}
	
	public TaskInbox getTaskInbox(){
		
		TaskInbox inbox = null;
		try {
			inbox = (TaskInbox) MyProperty.getModelObjectProperty(MySession.user, "taskinbox");
			return inbox;
		} catch (NotLoadedException e) {
			e.printStackTrace();
		}
		return inbox;
	}

}
