package com.motey.factory;

import com.motey.UI.ActionDoTaskDialog;
import com.motey.UI.ActionConditionDialog;
import com.motey.UI.ActionDialog;
import com.motey.UI.ActionReviewDialog;
import com.motey.UI.MainListFrame;
import com.motey.model.TaskModel;
import com.motey.model.TaskTypeModel;

public class ActionFrameFactory {
	
	public static ActionDialog getActionDialog(MainListFrame parent, TaskModel taskModel){
		
		ActionDialog dialog = null;
		
		String taskType = taskModel.getType();
		
		switch (taskType) {
		case TaskTypeModel.DO : dialog = new ActionDoTaskDialog(parent, taskModel);
			break;
		case TaskTypeModel.REVIEW : dialog = new ActionReviewDialog(parent, taskModel);
			break;
		case TaskTypeModel.PERFORM_SIGNOFF : dialog = new ActionReviewDialog(parent, taskModel);
			break;
		case TaskTypeModel.CONDITION : dialog = new ActionConditionDialog(parent, taskModel);
			break;
		default:
			break;
		}
		
		return dialog;
	}

}
