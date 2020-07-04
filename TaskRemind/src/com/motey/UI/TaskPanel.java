package com.motey.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JPanel;

import com.motey.model.TaskModel;
import com.motey.utils.ImageUtil;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class TaskPanel extends JPanel implements MouseListener{

	private static final long serialVersionUID = -1600989448690525223L;

	private static int icon_size = 20;
	private static int icon_x = 7;
	
	public static ImageIcon ICON_READ 		= ImageUtil.getImageIcon("/icons/read.png",icon_size, icon_size);
	public static ImageIcon ICON_UNREAD		= ImageUtil.getImageIcon("/icons/unread.png",icon_size, icon_size);
	public static ImageIcon ICON_MIDREAD 	= ImageUtil.getImageIcon("/icons/read_mid.png",icon_size, icon_size);
	public static ImageIcon ICON_TOP 		= ImageUtil.getImageIcon("/icons/top.png",icon_size, icon_size);
	public static ImageIcon ICON_UNTOP 		= ImageUtil.getImageIcon("/icons/untop.png",icon_size, icon_size);
	public static ImageIcon ICON_MIDTOP 	= ImageUtil.getImageIcon("/icons/top_mid.png",icon_size, icon_size);
	
	public static SimpleDateFormat dataFormat1 = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat dataFormat2 = new SimpleDateFormat("MM-dd");
	
	private MainListFrame parent;
	
	private TaskModel task;

	private JLabel readIcon;

	private JLabel taskActionIcon;

	private JLabel ownerLabel;

	private JLabel flowNameLabel;

	private JLabel dateLabel;
	
	private boolean isReaded = false;
	private boolean isSelected = false;

	
	public TaskPanel(TaskModel task, MainListFrame mainListFrame){
		
		this.parent = mainListFrame;
		this.task = task;
		String owner = task.getOwner();
		String taskName = task.getDisplayName();
		Date taskStartDate = task.getStartDate();
		
		setLayout(null);
		setOpaque(false);
		setBackground(new Color(224, 255, 255));
		
		readIcon = new JLabel("");
		readIcon.setIcon(ICON_UNREAD);
		readIcon.setBounds(icon_x, 7, icon_size, icon_size);
		readIcon.addMouseListener(this);
		setReaded(task.isReaded());
		add(readIcon);
		
//		topIcon = new JLabel("");
//		topIcon.setIcon(ICON_UNTOP);
//		topIcon.setBounds(icon_x, 26, icon_size, icon_size);
//		topIcon.addMouseListener(this);
//		add(topIcon);
		
		taskActionIcon = new JLabel("");
		taskActionIcon.setIcon(task.getIcon());
		taskActionIcon.addMouseListener(this);
		taskActionIcon.setBounds(icon_x, 26, icon_size, icon_size);
		add(taskActionIcon);
		
		ownerLabel = new JLabel(owner);
		
		ownerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		ownerLabel.setBounds(34, 7, 95, 18);
		add(ownerLabel);
		
		flowNameLabel = new JLabel(taskName);
		flowNameLabel.setForeground(Color.GRAY);
		flowNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		flowNameLabel.setBounds(34, 28, getWidth()-34, 18);
		add(flowNameLabel);
		
		dateLabel = new JLabel(getDateString(taskStartDate));
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		dateLabel.setForeground(Color.GRAY);
		dateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		dateLabel.setBounds(getWidth() - 83, 7, 83, 18);
		add(dateLabel);
		
		addMouseListener(this);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				flowNameLabel.setBounds(34, 28, getWidth()-34, 18);
				dateLabel.setBounds(getWidth() - 83, 7, 83, 18);
			}
		});

	}
	
	public String getDateString(Date date){
		
		long d = 1000 * 24 * 60 * 60;
		long l = new Date().getTime() - date.getTime();
		long day = l / d ;
		if(day < 1){
			return dataFormat1.format(date);
		}else if(day < 2){
			return "昨天";
		}else{
			return dataFormat2.format(date);
		}
	}

	public void setReaded(boolean isReaded){
		this.isReaded = isReaded;
		task.setReaded(isReaded);
		readIcon.setIcon(isReaded ? ICON_READ : ICON_UNREAD);
		
		if(isReaded){
			parent.addReadedTask(task);
		}else{
			parent.removeReadedTask(task);
		}
	}
	
	public boolean isReaded(){
		return isReaded;
	}
	
	public void setSelected(boolean isSelected){
		this.isSelected = isSelected;
		setOpaque(isSelected);
		updateUI();
	}
	
	public boolean isSelected(){
		return isSelected;
	}
	
	public TaskModel getTask(){
		return task;
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		g.setColor(new Color(10,10,10));
		g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(e.getSource() instanceof JLabel){
			
			JLabel source = (JLabel) e.getSource();
			
			if(source.equals(readIcon)){
				
				setReaded(!isReaded);
				
			}else if(source.equals(taskActionIcon)){
				
				parent.taskAction(task);
				
			}else{
				return;
			}
			
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		if(e.getSource() instanceof JLabel){
			
			JLabel source = (JLabel) e.getSource();
			
			if(source.equals(readIcon) && isReaded){
				
				readIcon.setIcon(ICON_MIDREAD);
				
			}else{
				return;
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		if(e.getSource() instanceof JLabel){
			
			JLabel source = (JLabel) e.getSource();
			
			if(source.equals(readIcon) && isReaded){
				
				readIcon.setIcon(ICON_READ);
				
			}else{
				return;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

}
