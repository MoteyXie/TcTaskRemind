package com.motey.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.strong.Folder;

public class MailReader extends TaskReader {

	Date date = new Date();
	
	@Override
	public TaskModel[] read() {
		
		List<TaskModel> taskModels = new ArrayList<>();
		try {
			if(MySession.user == null)return null;
			
			Folder mailbox = (Folder) MyProperty.getModelObjectProperty(MySession.user, "mailbox");
			MyProperty.refreshObjects(new ModelObject[]{mailbox});
			ModelObject[] contents = MyProperty.getContents(mailbox);
			MyProperty.refreshObjects(contents);
			TaskModel taskModel = null;
			
			for(int i = 0; i < contents.length; i++){
				String objectType = MyProperty.getStringProperty(contents[i], "object_type");
				if(!"Envelope".equals(objectType))continue;
				
				String name				 = MyProperty.getStringProperty(contents[i], "object_name");
				String owner 			 = MyProperty.getStringProperty(contents[i], "owning_user");
				Date startDate 			 = MyProperty.getDateProperty(contents[i], "sent_date");
				String parentName 		 = MyProperty.getStringProperty(contents[i], "object_desc");
				ModelObject[] targets	 = MyProperty.getContents(contents[i]);
				boolean isReaded 		 = MyProperty.getBooleanProperty(contents[i], "envelopeReadFlag");
				
				taskModel = new TaskModel(contents[i]);
				taskModel.setReadFlagPropName("envelopeReadFlag");
				taskModel.setName(name);
				taskModel.setOwner(owner);
				taskModel.setStartDate(startDate);
				taskModel.setParentName(parentName);
				taskModel.setTargets(targets);
				taskModel.setType(objectType);
				taskModel.setTaskTypeModel(getTaskTypeModel());
				taskModel.setReaded(isReaded);
				
				taskModels.add(taskModel);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		TaskModel[] t = new TaskModel[10];
//		
//		
//		for(int i = 0; i < t.length; i++){
//			t[i] = new TaskModel(null);
//			t[i].setName("邮件名");
//			t[i].setOwner("周杰伦"+i);
//			t[i].setParentName("邮件概述");
//			t[i].setStartDate(date);
//			t[i].setIcon(ImageUtil.getImageIcon("/icons/notifytask_32.png", 20, 20));
//			t[i].setTaskTypeModel(getTaskTypeModel());
//		}
		
		return taskModels.toArray(new TaskModel[taskModels.size()]);
	}

}
