package com.motey.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.services.strong.core._2007_01.DataManagement.VecStruct;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.Property;
import com.teamcenter.soa.client.model.ServiceData;
import com.teamcenter.soa.exceptions.NotLoadedException;

public class MyProperty {
	
	public static DataManagementService dmService;
	public static ServiceData serviceData;
	
	
	public static void setProperty(ModelObject object, String propertyName, String propertyValue){
		
		ModelObject[] objects = new ModelObject[]{object};
		Map<String, VecStruct> properties = new HashMap<>();
		VecStruct vecs = new VecStruct();
		vecs.stringVec = new String[]{propertyValue};
		properties.put(propertyName, vecs);
		dmService.setProperties(objects, properties);
		dmService.refreshObjects(objects);
		
	}
	
	public static void refreshObject(ModelObject object){
		refreshObjects(new ModelObject[]{object});
	}
	
	public static void refreshObjects(ModelObject[] objects){
		dmService.refreshObjects(objects);
	}
	
	public static Property getProperty(ModelObject object, String propertyName) throws NotLoadedException{
	    
//		ModelObject[] ret = new ModelObject[]{object};
//		dmService.refreshObjects(ret);
//    	serviceData = dmService.getProperties(ret, new String[]{propertyName});
//    	ret = null;
		serviceData = dmService.getProperties(new ModelObject[]{object}, new String[]{propertyName});
		
    	return object.getPropertyObject(propertyName);
    }
	
	public static Date getDateProperty(ModelObject object, String propertyName) throws NotLoadedException{
		
		try{
			Property property = getProperty(object, propertyName);
			
			return property.getCalendarValue().getTime();
		}catch(Exception e){
			e.printStackTrace();
			return new Date();
		}
		
	}
	
	public static String getStringProperty(ModelObject object, String propertyName) throws Exception{
		Property property = getProperty(object, propertyName);
		
		return property.getDisplayableValue();
	}
	
	public static List<String> getStringArrayProperty(ModelObject object, String propertyName) throws NotLoadedException{
		Property property = getProperty(object, propertyName);
		return property.getDisplayableValues();
	}
	
	public static boolean getBooleanProperty(ModelObject object, String propertyName) throws NotLoadedException{
		Property property = getProperty(object, propertyName);
		return property.getBoolValue();
	}
	
	public static ModelObject getModelObjectProperty(ModelObject object, String propertyName) throws NotLoadedException{
		Property property = getProperty(object, propertyName);
		property.getPropertyDescription();
		return property.getModelObjectValue();
	}

	public static ModelObject[] getModelObjectArrayProperty(ModelObject object, String propertyName) throws NotLoadedException{
		Property property = getProperty(object, propertyName);
		return property.getModelObjectArrayValue();
	}
	
	public static ModelObject[] getContents(ModelObject object) throws NotLoadedException{
		return getModelObjectArrayProperty(object, "contents");
	}
}
