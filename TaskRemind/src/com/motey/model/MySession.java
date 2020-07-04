package com.motey.model;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.soa.client.model.strong.User;

public class MySession {
	
	public static User user;
	public static AppXSession session;
	public static DataManagementService dmService;
	
	public static String getUserName(){
		try {
			return MyProperty.getStringProperty(user, "user_name");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
