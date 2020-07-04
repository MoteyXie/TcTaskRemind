package com.motey.model;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.motey.utils.ImageUtil;

public class TaskIcon {
	
	public static Map<String, ImageIcon> icons = null;
	public static Map<String, ImageIcon> doneIcons = null;
	
	static{
		icons = new HashMap<>();
		icons.put("EPMTask", 				createIcon("/icons/processdesignerapplication_32.png"));
		icons.put("EPMDoTask", 				createIcon("/icons/dotask_32.png"));
		icons.put("EPMReviewTask", 			createIcon("/icons/reviewtask_32.png"));
		icons.put("EPMPerformSignoffTask", 	createIcon("/icons/reviewtask_32.png"));
		icons.put("EPMSelectSignoffTask", 	createIcon("/icons/selectsignofftask_32.png"));
		icons.put("EPMRouteTask", 			createIcon("/icons/routetask_32.png"));
		icons.put("EPMConditionTask", 		createIcon("/icons/conditiontask_32.png"));
		icons.put("EPMAcknowledgeTask", 	createIcon("/icons/acknowledgetask_32.png"));
		icons.put("EPMOrTask", 				createIcon("/icons/ortask_16.png"));
		icons.put("EPMAddStatusTask", 		createIcon("/icons/addstatustask_32.png"));
		icons.put("Envelope", 				createIcon("/icons/email_32.png"));
		
		doneIcons = new HashMap<>();
		doneIcons.put("EPMDoTask", 				createIcon("/icons/dotask_done_32.png"));
		doneIcons.put("EPMReviewTask", 			createIcon("/icons/reviewtask_done_32.png"));
		doneIcons.put("EPMSelectSignoffTask", 	createIcon("/icons/selectsignofftask_done_32.png"));
		doneIcons.put("EPMRouteTask", 			createIcon("/icons/routetask_done_32.png"));
		doneIcons.put("EPMConditionTask", 		createIcon("/icons/conditiontask_done_32.png"));
		
		doneIcons.put("Envelope", 				createIcon("/icons/emial_expanded_32.png"));
		
	}
	
	public static ImageIcon getIcon(String typeName){
		
		ImageIcon icon = icons.get(typeName);
		if(icon == null)icon = icons.get("EPMReviewTask");
		return icon;
	}
	
	public static ImageIcon getDoneIcon(String typeName){
		
		ImageIcon icon = doneIcons.get(typeName);
		if(icon == null)icon = doneIcons.get("EPMReviewTask");
		return icon;
	}
	
	public static ImageIcon createIcon(String resourcePath){
		return ImageUtil.getImageIcon(resourcePath, 20, 20);
	}

}
