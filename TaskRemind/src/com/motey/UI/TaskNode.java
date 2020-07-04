package com.motey.UI;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;

import com.motey.model.MyProperty;
import com.motey.model.TaskModel;
import com.motey.utils.ImageUtil;
import com.motey.utils.MyWindowUtil;

import java.awt.Color;
import java.awt.Dimension;

public class TaskNode extends JPanel {

	private static final long serialVersionUID = -8609753769003924301L;

	public static final int NODE_SIDE = 45;
	
	public static final int HALF_NODE_SIZE = NODE_SIDE >> 1 ;
	
	private JLabel lblIcon;
	private TaskModel taskModel;
	private JLabel lblNewLabel;
	
	private ArrayList<TaskNode> nextNodes;


	private Point topPoint, bottomPoint, leftPoint, rightPoint, centerPoint;

	public TaskNode(TaskModel taskModel) {
		
		this.taskModel = taskModel;
		
		setPreferredSize(new Dimension(45, 45));
		setLayout(new BorderLayout(0, 0));
//		setOpaque(false);
		
		try {
			String state = MyProperty.getStringProperty(taskModel.getTaskObject(), "real_state");
			if("Completed".equals(state)){
				setBackground(new Color(100,240,70));	//Blue
			}else if("Started".equals(state)){
				setBackground(Color.YELLOW);
			}else{
				System.out.println();
				setBackground(new Color(224, 245, 240));
			}
		} catch (Exception e) {
//			e.printStackTrace();
			setBackground(new Color(224, 245, 240));
		}
		
		
		lblIcon = new JLabel("");
		lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
		
		if(taskModel != null){
			ImageIcon icon = ImageUtil.copyImageIcon(taskModel.getIcon(), 30, 30);
			
			lblIcon.setIcon(icon);
			add(lblIcon, BorderLayout.CENTER);
			
			lblNewLabel = new JLabel(taskModel.getName());
			lblNewLabel.setOpaque(true);
			lblNewLabel.setBackground(new Color(224, 255, 255));
			lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 8));
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			add(lblNewLabel, BorderLayout.NORTH);

			setToolTipText(taskModel.getName());
			MyWindowUtil.setChangeBackgroundListener(this, Color.CYAN);
		}
		
		
	}
	
	public TaskModel getTask(){
		return taskModel;
	}
	
	public ArrayList<TaskNode> getNextNodes(){
		return nextNodes;
	}
	
	public void addNextNode(TaskNode node){
		if(nextNodes == null){
			nextNodes = new ArrayList<>();
		}
		nextNodes.add(node);
	}
	
	public void setNextNodes(ArrayList<TaskNode> nodes){
		this.nextNodes = nodes;
	}
	
	public Point getTopPoint(){
		
		if(topPoint != null)return topPoint;
		
		Point location = getLocation();

		int x = (int) (location.getX() +  HALF_NODE_SIZE);
		int y = (int) location.getY();
		
		topPoint = new Point(x,y);
		return topPoint;
		
	}
	
	public Point getBottomPoint(){
		
		if(bottomPoint != null)return bottomPoint;
		
		Point location = getLocation();

		int x = (int) location.getX() +  HALF_NODE_SIZE;
		int y = (int) location.getY() + NODE_SIDE;
		
		bottomPoint = new Point(x,y);
		return bottomPoint;
		
	}
	
	public Point getLeftPoint(){
		
		if(leftPoint != null)return leftPoint;
		
		Point location = getLocation();

		int x = (int) location.getX();
		int y = (int) location.getY() + HALF_NODE_SIZE;
		
		leftPoint = new Point(x,y);
		return leftPoint;
		
	}
	
	public Point getRightPoint(){
		
		if(rightPoint != null)return rightPoint;
		
		Point location = getLocation();

		int x = (int) location.getX() + NODE_SIDE;
		int y = (int) location.getY() + HALF_NODE_SIZE;
		
		rightPoint = new Point(x,y);
		return rightPoint;
		
	}
	
	public Point getCenterPoint(){
		
		if(centerPoint != null)return centerPoint;
		
		Point location = getLocation();

		int x = (int) location.getX() + HALF_NODE_SIZE;
		int y = (int) location.getY() + HALF_NODE_SIZE;
		
		centerPoint = new Point(x,y);
		return centerPoint;
		
	}
	
	@Override
	public boolean equals(Object object){
		
		if(object instanceof TaskNode){
			
			TaskModel temp = ((TaskNode)object).getTask();
			return taskModel.equals(temp);
			
		}else{
			return false;
		}
		
	}

}
