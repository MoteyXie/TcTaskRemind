package com.motey.UI;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.motey.manager.LoginManager;
import com.motey.manager.ReaderManager;

import configuration.Cache;

/**
 * 系统托盘
 * @author Motey
 *
 */
public class Tray implements ActionListener{
	
	public String[] commands = {"打开主界面", "设置", "注销", "关闭"};
	private LoginFrame loginFrame;
	private MainListFrame mainFrame;
	private LoginManager loginController;

	private SystemTray systemTray;
	private TrayIcon trayIcon;
	private ReaderManager rm;
	
	public Tray(){
		
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/icons/defaultapplication_32.png"));
		icon.setImage(icon.getImage().getScaledInstance(16,16,Image.SCALE_FAST)); 
		trayIcon = new TrayIcon(icon.getImage());
		
		// 添加工具提示文本
        trayIcon.setToolTip("PLM信息助手");
        
        // 创建弹出菜单
        PopupMenu popupMenu = new PopupMenu();
        
        for(int i = 0; i < commands.length; i++){
        	MenuItem menuItem = new MenuItem(commands[i]);
       	 	menuItem.setActionCommand(i+"");
       	 	menuItem.addActionListener(this);
       	 	popupMenu.add(menuItem);
        }
      
        // 为托盘图标加弹出菜弹
        trayIcon.setPopupMenu(popupMenu);
        
		trayIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 2){
					openMainFrame();
				}
			}
			
		});
		
		systemTray = SystemTray.getSystemTray();
		
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
	}
	
	public void openMainFrame(){
		if(loginFrame != null && !loginFrame.isSuccess() && loginFrame.isDisplayable()){
			loginFrame.display();
		}else if(mainFrame != null && mainFrame.isDisplayable()){
			mainFrame.display();
		}
	}
	
	private void openOptionFrame() {
		OptionFrame.getInstance();
	}
	
	public void loginOut(){
		loginController.loginout();
		if(mainFrame != null)mainFrame.setVisible(false);
		loginFrame = new LoginFrame(loginController, this);
		loginFrame.setVisible(true);
	}
	
	public void close(){
		try {
			loginController.loginout();
			if(loginFrame != null)loginFrame.dispose();
			if(mainFrame != null)mainFrame.destory();
			if(rm != null)rm.stopRead();
			Cache.writeTasks(mainFrame.getReadedTaskList());
			systemTray.remove(trayIcon);
			
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loginSuccess(){
		loginFrame.dispose();
		try {
//			mainFrame.refreshTask();
			rm = new ReaderManager(mainFrame);
			rm.loop();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainFrame.setVisible(true);
	}

	public LoginFrame getLoginDialog() {
		return loginFrame;
	}

	public void setLoginDialog(LoginFrame loginDialog) {
		this.loginFrame = loginDialog;
	}

	public JFrame getMainDialog() {
		return mainFrame;
	}

	public void setMainDialog(MainListFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	public LoginManager getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginManager loginController) {
		this.loginController = loginController;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case "0":
			openMainFrame();
			break;
		case "1":
			openOptionFrame();
			break;
		case "2":
			loginOut();
			break;
		case "3":
			close();
			break;

		default:
			break;
		}
	}


}
