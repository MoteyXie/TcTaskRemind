package com.motey.main;

import com.motey.UI.LoginFrame;
import com.motey.UI.MainListFrame;
import com.motey.UI.Tray;
import com.motey.manager.LoginManager;

/**
 * 程序主入口
 * @author Motey
 *
 */
public class RemindMain {
	
	public static void main(String[] args) {
		
		//实例化登录管理器
		LoginManager loginController = new LoginManager();
		
		//实例化系统托盘
		Tray tray = new Tray();
		
		//实例化登录窗口,装载登录管理器
		LoginFrame loginFrame = new LoginFrame(loginController, tray);
		
		//实例化任务列表查看窗口
		MainListFrame mainFrame = new MainListFrame();
		
		//实例化信息提醒窗口
		
		
		//装载各个窗口
		tray.setLoginDialog(loginFrame);
		tray.setMainDialog(mainFrame);
		
		//装载登录控制器
		tray.setLoginController(loginController);
		
		//显示窗口
		loginFrame.setVisible(true);
		
	}

}
