package com.motey.manager;

import com.motey.UI.LoginFrame;
import com.motey.model.MyProperty;
import com.motey.model.MySession;
import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.strong.core.DataManagementService;
import com.teamcenter.soa.client.model.strong.User;

import configuration.ConfigReader;

/**
 * 登录控制器
 * @author Motey
 *
 */
public class LoginManager {
	
	private DataManagementService dmService;
	private User user;
	private LoginFrame dialog;
	private AppXSession session;
	
	public static String userName;
	public static String password;
	
	private boolean loginFlag = false;

	public LoginManager(){
		
	}
	
	public void stopLogin(){
		loginFlag = false;
	}
	
	public void loginout(){
		loginFlag = false;
		if(session != null)session.logout();
	}
	
	public void Login(final String userName, final String password){
		
		LoginManager.userName = userName;
		LoginManager.password = password;
		
		loginFlag = true;
		
		new Thread(){
			public void run(){
				String[] link = ConfigReader.getLink();
				String url = "http://"+link[0]+":"+link[1]+"/tc";
				System.out.println("URL : "+url);
				AppXSession session = new AppXSession(url);
				dmService = DataManagementService.getService(AppXSession.getConnection());
				try {
					user = session.login(userName,password);
				} catch (Exception e) {
					dialog.loginFailed(e.toString()+"\n原因可能如下：\n"+session.getErrorMsg());
				}
				
				MySession.dmService = dmService;
				MySession.session = session;
				MySession.user = user;
				MyProperty.dmService = dmService;
				
				if(dialog != null && loginFlag && user != null)dialog.loginSuccess();
				
			}
		}.start();
		
	}
	
	public void setRelatedDialog(LoginFrame dialog){
		this.dialog = dialog;
	}
	
	public User getUser(){
		return user;
	}
	
	public DataManagementService getDataManagerService(){
		return dmService;
	}

}
