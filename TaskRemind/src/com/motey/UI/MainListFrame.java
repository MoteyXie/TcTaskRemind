package com.motey.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.motey.factory.ActionFrameFactory;
import com.motey.manager.LoginManager;
import com.motey.manager.ReaderManager;
import com.motey.model.TaskModel;
import com.motey.model.TaskTypeModel;
import com.motey.utils.FileUtil;
import com.motey.utils.ImageUtil;
import com.motey.utils.MyWindowUtil;
import com.sun.awt.AWTUtilities;
import com.teamcenter.soa.client.model.ModelObject;

import configuration.ConfigReader;
import configuration.ConfigWriter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;

public class MainListFrame extends JFrame {

	private static final long serialVersionUID = 2335747209239867655L;
	
	private TaskTypeModel[] taskTypeModels = {new TaskTypeModel("我的流程", TaskMenu.ICON_FLOW, "com.motey.model.FlowTaskReader"),
											new TaskTypeModel("我的邮件", TaskMenu.ICON_MAIL, "com.motey.model.MailReader")};
	
	private Map<String, TaskMenu> taskMenus;
	
	private Map<TaskMenu, TaskListPanel> taskListPanels;
	
	private ArrayList<TaskModel> readedTaskList;
	
	private JPanel contentPane;
	
	private int width = 900;
	private int height = 550;

	private JPanel taskTypeListPanel;

	private JScrollPane scrollPane;
	
	private TaskMenu clickedMenu = null;

	private JLabel selectedTaskTitleLabel;

	private JPanel detailedInformationPanel;

	private TaskContentPanel taskContentPanel;

	private JPanel noneInformationPanel;
	
	private TaskPanel selectedTaskPanel = null;

	private TaskPanelClickedListener selectedListener;

	private TaskMenuClickedListener clickedlistener;

	private JLabel logger;

	private JPanel taskTitlePanel;
	private JLabel lbl_refresh;
	private JLabel lbl_setting;

	private TaskMenu initMenu;
	
	private ReaderManager readerManeger;

	private JScrollPane scrollPane1;
	
	private boolean isRelatedRichClient = false;
	private String richClientPath = "";

	public static void main(String[] args) {
		
		MainListFrame frame = new MainListFrame();
		
		frame.setVisible(true);
	}
	
	public MainListFrame(){
		initialize();
	}

	public void initialize() {
		//windows风格
		MyWindowUtil.setWindowsStyle(); 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(width, height);
		setUndecorated(true);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(247,247,247));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ImageIcon icon = ImageUtil.getImageIcon("/icons/tcdesktop_16.png", 25, 25);
		TopPanel topPanel = new TopPanel("PLM助手", 900, 40, TopPanel.DEFAULT_BACKGROUND, icon);
		topPanel.setBounds(0, 0, 900, 40);
		topPanel.setParent(this,false);
		topPanel.setDragable();
		contentPane.add(topPanel);
		
		final JPanel personPanel = new JPanel();
		personPanel.setBounds(0, 40, 165, 115);
		contentPane.add(personPanel);
		personPanel.setLayout(null);
		
		taskTypeListPanel = new JPanel();
		taskTypeListPanel.setBounds(0, 155, 165, 360);
		contentPane.add(taskTypeListPanel);
		
		ImageIcon personIcon = ImageUtil.getImageIcon("/icons/PERSON_32.png", 20, 20);
		logger = new JLabel("N/A");
		logger.setIcon(personIcon);
		logger.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		logger.setBounds(25, 84, 151, 18);
		personPanel.add(logger);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(ImageUtil.getImageIcon("/images/teamcenter_app_256.gif", 64, 64));
		label_2.setBounds(20, 13, 64, 64);
		personPanel.add(label_2);
		
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int flag = JOptionPane.showConfirmDialog(personPanel, "是否要打开胖客户端？");
				if(flag == 0) {
					try {
						openWithRichClient(null);
					} catch (Exception e1) {
						e1.printStackTrace();
						MessageDialog dialog = new MessageDialog(e1.getMessage(), ERROR, null);
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
					}
				}
			}
		});
		
		JPanel toolPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) toolPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		toolPanel.setBackground(new Color(220, 220, 220));
		toolPanel.setBounds(79, 515, 86, 34);
		contentPane.add(toolPanel);
		
		lbl_refresh = new JLabel("");
		MyWindowUtil.setChangeBackgroundListener(lbl_refresh, Color.BLUE);
		lbl_refresh.setIcon(ImageUtil.getImageIcon("/icons/revise_32.png", 23, 23));
		toolPanel.add(lbl_refresh);
		lbl_refresh.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e){
				try {
//					refreshTask();
					readerManeger.reading();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		lbl_setting = new JLabel("");
		MyWindowUtil.setChangeBackgroundListener(lbl_setting, Color.BLUE);
		lbl_setting.setIcon(ImageUtil.getImageIcon("/icons/setting2.png", 21, 21));
		toolPanel.add(lbl_setting);
		lbl_setting.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e){
				try {
					OptionFrame.getInstance();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(3);
		splitPane.setResizeWeight(0.29);
		splitPane.setBounds(166, 40, 734, 509);
		contentPane.add(splitPane);

		detailedInformationPanel = new JPanel();
		detailedInformationPanel.setPreferredSize(new Dimension(0, 630));
		scrollPane1 = new JScrollPane(detailedInformationPanel);
		scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane1.getVerticalScrollBar().setUnitIncrement(60);
		
//		splitPane.setRightComponent(detailedInformationPanel);
		splitPane.setRightComponent(scrollPane1);
		detailedInformationPanel.setLayout(null);
		
		//保持窗口的相对位置不变
		detailedInformationPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				taskTitlePanel.setBounds(14, 0, detailedInformationPanel.getWidth()-30, 30);
				taskContentPanel.setBounds(14, 36, detailedInformationPanel.getWidth()-30, detailedInformationPanel.getHeight() - 50);
				noneInformationPanel.setBounds(0, 0, detailedInformationPanel.getWidth(), detailedInformationPanel.getHeight());
			}
		});
		
		noneInformationPanel = new JPanel();
		noneInformationPanel.setBounds(0, 0, 495, 507);
		detailedInformationPanel.add(noneInformationPanel);
		noneInformationPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel noneInfoBackground = new JLabel("");
		noneInfoBackground.setIcon(ImageUtil.getImageIcon("/images/teamcenter_app_black2.png", 150, 150));
		noneInfoBackground.setHorizontalAlignment(SwingConstants.CENTER);
		noneInformationPanel.add(noneInfoBackground, BorderLayout.CENTER);
		noneInformationPanel.setVisible(true);
		
		taskContentPanel = new TaskContentPanel();
		taskContentPanel.setSize(460, 558);
		detailedInformationPanel.add(taskContentPanel);
		taskContentPanel.setVisible(false);
		
		taskTitlePanel = new JPanel();
		FlowLayout fl_taskTitlePanel = (FlowLayout) taskTitlePanel.getLayout();
		fl_taskTitlePanel.setAlignment(FlowLayout.LEFT);
		taskTitlePanel.setBounds(14, 0, 500, 30);
		detailedInformationPanel.add(taskTitlePanel);
		
		selectedTaskTitleLabel = new JLabel("");
		selectedTaskTitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		selectedTaskTitleLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
//				taskAction(selectedTaskPanel.getTask());
				try {
					int flag = JOptionPane.showConfirmDialog(personPanel, "是否要打开胖客户端？");
					
					if(flag == 0)openWithRichClient(selectedTaskPanel.getTask().getTaskObject());
				} catch (Exception e1) {
					e1.printStackTrace();
					MessageDialog dialog = new MessageDialog(e1.getMessage(), ERROR, null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
			}
		});
		
		taskTitlePanel.add(selectedTaskTitleLabel);
		JLabel openRichClient_lb = new JLabel("<html><u>从胖客户端打开</u></html>");
		openRichClient_lb.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		openRichClient_lb.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				try {
					openWithRichClient(selectedTaskPanel.getTask().getTaskObject());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		taskTitlePanel.add(openRichClient_lb);
		
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(60);
		splitPane.setLeftComponent(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(220, 220, 220));
		panel.setBounds(0, 515, 86, 34);
		contentPane.add(panel);
		panel.setLayout(null);
		
		initTaskMenu();
		
		//设置窗口圆角
		AWTUtilities.setWindowShape(this, new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5.0D, 5.0D));
		
		validate();
		
		isRelatedRichClient = ConfigReader.isRelatedRichClient();
		richClientPath = ConfigReader.getRichClientPath();
	}
	
	public void setUserName(String name){
		logger.setText(name);
	}
	
	public Map<String, TaskMenu> getMenus(){
		return taskMenus;
	}
	
	public Map<TaskMenu, TaskListPanel> getTaskListPanels(){
		return taskListPanels;
	}
	
	public List<TaskModel> getReadedTaskList(){
		if(readedTaskList == null){
			readedTaskList = new ArrayList<>();
		}
		return readedTaskList;
	}
	
	public void resetReadedTaskList(){
		readedTaskList = new ArrayList<>();
	}
	
	public void addReadedTask(TaskModel taskModel){
		if(readedTaskList == null){
			readedTaskList = new ArrayList<>();
		}
		readedTaskList.add(taskModel);
	}
	
	public void removeReadedTask(TaskModel taskModel){
		
		try{
			readedTaskList.remove(taskModel);
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 将任务放到窗口对应的列表上
	 * @param taskModels
	 * @param typeName
	 */
	public void putTasks(TaskModel[] taskModels, String typeName){
		
		TaskMenu menu = taskMenus.get(typeName);
		TaskListPanel listPanel = taskListPanels.get(menu);
		
		if(menu == null){
			System.out.println("任务加入失败，未找到对应的菜单！");
			return;
		}
		
		for(int i = 0; i < taskModels.length; i++){
			if(taskModels[i] == null)continue;
			TaskPanel taskPanel = new TaskPanel(taskModels[i], this);
			taskPanel.addMouseListener(selectedListener);
			listPanel.addTask(taskPanel);
		}
		
		listPanel.updateUI();
		
	}
	
	public void putTask(TaskModel taskModel, String typeName, boolean isUpdateList){
		
		TaskMenu menu = taskMenus.get(typeName);
		TaskListPanel listPanel = taskListPanels.get(menu);
		
		if(menu == null){
			System.out.println("任务加入失败，未找到对应的菜单！");
			return;
		}
		
		if(taskModel == null)return;
		TaskPanel taskPanel = new TaskPanel(taskModel, this);
		taskPanel.addMouseListener(selectedListener);
		listPanel.addTask(taskPanel);
		
		if(isUpdateList)listPanel.updateUI();
		
	}
	
	public void refreshTask(String taskTypeName,  TaskModel[] taskModels){
		
		TaskMenu menu = taskMenus.get(taskTypeName);
		TaskListPanel taskListPanel = taskListPanels.get(menu);
		
		if(taskModels == null || taskModels.length == 0)return;
		
		taskListPanel.removeAll();
		
		for(int i = 0; i < taskModels.length; i++){
			if(taskModels[i] == null)continue;
			TaskPanel taskPanel = new TaskPanel(taskModels[i], this);
			taskPanel.addMouseListener(selectedListener);
			taskListPanel.addTask(taskPanel);
		}
		taskListPanel.updateUI();
	}
	
	public void refreshTask(Map<String, TaskModel[]> taskModelMap){
		
		if(taskModelMap == null)return;
		
		String typeName = null;
		TaskModel[] taskModels = null;
		TaskMenu menu = null;
		TaskListPanel taskListPanel = null;
		for (Entry<String, TaskModel[]> entry : taskModelMap.entrySet()) {
			typeName = entry.getKey();
			taskModels = entry.getValue();
			
			menu = taskMenus.get(typeName);
			taskListPanel = taskListPanels.get(menu);
			
			if(taskModels == null || taskModels.length == 0)continue;
			
			taskListPanel.removeAll();
			
			for(int i = 0; i < taskModels.length; i++){
				if(taskModels[i] == null)continue;
				TaskPanel taskPanel = new TaskPanel(taskModels[i], this);
				taskPanel.addMouseListener(selectedListener);
				taskListPanel.addTask(taskPanel);
			}
			taskListPanel.updateUI();
		}
		
	}
	
	
//	public void refreshTask() throws Exception{
//		
//		logger.setText(MySession.getUserName());
//		
//		if(taskListPanels == null)return;
//		
//		//遍历所有菜单上的列表控件
//		for (TaskListPanel taskListPanel : taskListPanels.values()) {
//			
//			taskListPanel.removeAll();
//			TaskModel[] tasks = taskListPanel.getTaskTypeModel().getReader().read();
//			if(tasks == null)continue;
//			
//			for(int i = 0; i < tasks.length; i++){
//				if(tasks[i] == null)continue;
//				TaskPanel taskPanel = new TaskPanel(tasks[i], this);
//				taskPanel.addMouseListener(selectedListener);
//				taskListPanel.addTask(taskPanel);
//			}
//			taskListPanel.updateUI();
//		}
//		
//		if(initMenu != null){
//			initMenu.clicked();
//			JPanel panel = taskListPanels.get(initMenu);
//			if(panel != null){
//				scrollPane.setViewportView(panel);
//				scrollPane.updateUI();
//			}
//			clickedMenu = initMenu;
//			initMenu = null;
//		}
//	}
	
	public void setDefaultClicked() {
		
		if(initMenu != null){
			initMenu.clicked();
			JPanel panel = taskListPanels.get(initMenu);
			if(panel != null){
				scrollPane.setViewportView(panel);
				scrollPane.updateUI();
			}
			clickedMenu = initMenu;
			initMenu = null;
		}
	}
	
	private void initTaskMenu() {
		
		taskListPanels = new HashMap<>();
		taskMenus = new HashMap<>();
		
		selectedListener = new TaskPanelClickedListener();
		clickedlistener = new TaskMenuClickedListener();
		
		for(int i = 0; i < taskTypeModels.length; i++){
			
			TaskMenu menu = new TaskMenu(taskTypeModels[i]);
			
			menu.addMouseListener(clickedlistener);
			
			if(i == 0)initMenu = menu;
			
			taskTypeListPanel.add(menu);
			
			TaskListPanel tlp = new TaskListPanel();
			tlp.setTaskTypeModel(taskTypeModels[i]);
			
			taskMenus.put(taskTypeModels[i].getTaskName(),  menu);
			
			taskListPanels.put(menu, tlp);
			
			TaskModel[] tasks = null;
			
			try{
				tasks = taskTypeModels[i].getReader().read();
			}catch (Exception e) {
			
			}
			
			if(tasks == null)continue;
			
			for(int j = 0; j < tasks.length; j++){
				TaskPanel taskPanel = new TaskPanel(tasks[j], this);
				taskPanel.addMouseListener(selectedListener);
				tlp.addTask(taskPanel);
			}
			
		}
	}
	
	public void taskAction(TaskModel task){
		
		ActionDialog actionFrame = ActionFrameFactory.getActionDialog(this, task);
		
		if(actionFrame == null){
			new MessageDialog("暂不支持此任务类型的操作！", MessageDialog.ERROR, this);
			return;
		}
		actionFrame.setVisible(true);
		String result = actionFrame.getResult();
		if(result == null)return;
		
		
		if(actionFrame.isSuccess){
			new MessageDialog("操作成功！\n"+result, MessageDialog.RIGHT, this);
			try {
//				refreshTask();
				readerManeger.reading();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			new MessageDialog("操作失败！\n"+result, MessageDialog.ERROR, this);
		}
	}
	
	/**
	 * 设置详细信息控件
	 * @param task
	 */
	public void setDetialedInfomation(TaskModel task){
		
		if(task == null){
			
			taskTitlePanel.setVisible(false);
			taskContentPanel.setVisible(false);
			noneInformationPanel.setVisible(true);
			
		}else{
			
			taskTitlePanel.setVisible(true);
			taskContentPanel.setVisible(true);
			noneInformationPanel.setVisible(false);
			
			selectedTaskTitleLabel.setIcon(task.getIcon());
			selectedTaskTitleLabel.setText(task.getDisplayName());
			
			taskContentPanel.setTask(task);
			taskContentPanel.setTaskOwner(task.getOwner());
			taskContentPanel.setDate(task.getStartDate());
		}
		
		detailedInformationPanel.updateUI();
	}
	
	public void setReaderManager(ReaderManager readerManeger){
		this.readerManeger = readerManeger;
	}
	
	public ReaderManager getReaderManager(){
		return readerManeger;
	}
	
	public void openWithRichClient(ModelObject mo) throws Exception {
		
		ConfigWriter.initStartWithTargetBat();
		
		String ugsPath = richClientPath+"/target.ugs";
		
		String uid = mo == null ? "" : mo.getUid();
		System.out.println("打开uid为 " + uid +" 的对象");
		
		FileUtil.writeTxtFile(ugsPath, "-attach\r\n" + 
				"-o="+uid+"\r\n" + 
				"-s=226TCSession\r\n" + 
				"servername=TcWeb1", true);
		
		String cmd = richClientPath + "/start_tc_with_target.bat "+ ugsPath + " " + LoginManager.userName + " " + LoginManager.password;
		Runtime.getRuntime().exec(cmd);
	}
	
	public void displayDetail(TaskModel taskModel){
		
		try{
			if(isRelatedRichClient) {
				openWithRichClient(taskModel.getTaskObject());
			}else {
				TaskMenu menu = taskMenus.get(taskModel.getTaskTypeModel().getTaskName());
				TaskListPanel listPanel = taskListPanels.get(menu);
				TaskPanel taskPanel = listPanel.getTaskPanel(taskModel);
				clickedTaskMenu(menu);
				clickedTaskPanel(taskPanel);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		display();
	}

	public void display(){
		setVisible(true);
		setExtendedState(JFrame.NORMAL);
		toFront();
	}
	
	public void destory(){
		dispose();
	}
	
	private void clickedTaskMenu(TaskMenu menu){
		if(menu.equals(clickedMenu))return;
		
		if(clickedMenu != null) clickedMenu.unClicked();
		
		clickedMenu = menu;
		
		clickedMenu.clicked();
		
		TaskListPanel panel = taskListPanels.get(clickedMenu);
		if(panel != null){
			scrollPane.setViewportView(panel);
			scrollPane.updateUI();
		}
		setDetialedInfomation(null);
	}
	
	private void clickedTaskPanel(TaskPanel taskPanel){
		if(selectedTaskPanel != null) selectedTaskPanel.setSelected(false);
		selectedTaskPanel = taskPanel;
		selectedTaskPanel.setSelected(true);
		selectedTaskPanel.setReaded(true);
		setDetialedInfomation(selectedTaskPanel.getTask());
	}
	
	class TaskMenuClickedListener extends MouseAdapter{
		
		public void mouseClicked(MouseEvent e){
			
			Object source = e.getSource();
			if(source instanceof TaskMenu ){
				
				clickedTaskMenu((TaskMenu) source);
			}
		}
	}
	
	class TaskPanelClickedListener extends MouseAdapter{
		
		public void mouseClicked(MouseEvent e){
			
			Object source = e.getSource();
			if(source instanceof TaskPanel ){
				
				clickedTaskPanel((TaskPanel) source);
			}
		}
	}
}
