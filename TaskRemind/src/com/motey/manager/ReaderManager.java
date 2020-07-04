package com.motey.manager;
import java.util.List;

import com.motey.UI.MainListFrame;
import com.motey.UI.TaskListPanel;
import com.motey.UI.TaskMenu;
import com.motey.factory.AlertDialogFactory;
import com.motey.model.MySession;
import com.motey.model.TaskModel;
import com.motey.model.TaskTypeModel;

import configuration.Cache;
import configuration.ConfigReader;
import configuration.ConfigWriter;

/**
 * 数据读取管理器，用于控制所有的读取器读取数据
 * 读取到数据有与当前窗口中已有的数据作比较，对新数据进行弹窗提醒
 * @author Motey
 *
 */
public class ReaderManager {
	
	public static TaskTypeModel[] TASK_TYPE_MODELS = {
			new TaskTypeModel("我的流程", TaskMenu.ICON_FLOW, "com.motey.model.FlowTaskReader"),
			new TaskTypeModel("我的邮件", TaskMenu.ICON_MAIL, "com.motey.model.MailReader")};
	
	private MainListFrame mainListFrame = null;
	
	private TaskModel[] taskModels = null;
	private TaskMenu menu = null;
	private TaskListPanel taskListPanel = null;
	private List<TaskModel> tempTaskModels = null;
	private List<TaskModel> readedTaskList = null;
	
	private boolean runFlag = true;
	
	private boolean isReading = false;
	
	private int interval = 50000;
	
	public ReaderManager(MainListFrame mainListFrame){
		
		this.mainListFrame = mainListFrame;
		mainListFrame.setReaderManager(this);
		
	}
	
	public void loop(){
		int t = ConfigReader.getRefreshInterval();
		if(t < 2){
			t = 2;
			ConfigWriter.writeRefreshInteval(2);
		}
		interval = t * 60 * 1000;
		
		new ReadThread().start();
	}
	
	public synchronized void reading(){
		
		isReading = true;
		
		readedTaskList = mainListFrame.getReadedTaskList();
		
		if(readedTaskList == null || readedTaskList.size() == 0){
			readedTaskList = Cache.getTaskModels();
		}
		
		mainListFrame.resetReadedTaskList();
		
		for (TaskTypeModel taskTypeModel : TASK_TYPE_MODELS) {
			
			//使用数据读取器读取最新数据
			taskModels = taskTypeModel.getReader().read();
			
			try{
				//根据任务的类型名称获取对应的菜单对象
				menu = mainListFrame.getMenus().get(taskTypeModel.getTaskName());
				
				//根据菜单对象获取列表对象
				taskListPanel = mainListFrame.getTaskListPanels().get(menu);
				
				//获取列表上的所有任务对象
				tempTaskModels = taskListPanel.getTaskModelArray();
				
				if(tempTaskModels == null || tempTaskModels.size() == 0){
					tempTaskModels = readedTaskList;
				}
				
				for (TaskModel taskModel : taskModels) {
					
					if(taskModel.isReaded())continue;
					
					if(!tempTaskModels.contains(taskModel)){
						//对原来没有的任务进行提示
						AlertDialogFactory.create(taskModel, mainListFrame);
						
					}else if(readedTaskList.contains(taskModel)){
						//原来已读的继续标记为已读
						taskModel.setReaded(true);
					}
					
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			mainListFrame.refreshTask(taskTypeModel.getTaskName(), taskModels);
		}
		
		//数据处理完毕后重置标识，下次可以重新读取
		isReading = false;
		mainListFrame.setUserName(MySession.getUserName());
		mainListFrame.setDefaultClicked();
		
		readedTaskList = null;
		taskModels = null;
		menu = null;
		taskListPanel = null;
		tempTaskModels.clear();
		tempTaskModels = null;
	}
	
	public boolean isReading(){
		return isReading;
	}
	
	public void stopRead(){
		runFlag = false;
	}
	
	class ReadThread extends Thread{
		
		public void run(){
			
			int step = 200;
			
			int stepInterval = interval / step;
			
			while(runFlag){
				
				if(!isReading){
					reading();
				}
				
				for(int i = 0; i < step; i ++){
					try {
						if(!runFlag)break;
						Thread.sleep(stepInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
					
			}
			
		}
	}
	

}
