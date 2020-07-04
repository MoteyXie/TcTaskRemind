package com.motey.UI;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.UIManager;

import com.motey.model.TaskModel;
import com.motey.utils.MyWindowUtil;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class FlowChartPanel2 extends JPanel {

	private static final long serialVersionUID = 4532132523581309211L;

	private JPanel[] pn_rows = new JPanel[5];
	private TaskModel[] taskModels = null;
	private List<JPanel> nodes = new ArrayList<>();

	public FlowChartPanel2() {
		
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setVgap(5);
		flowLayout.setHgap(0);
		
		int p_width = 410;
		int p_height = 200/3 - 10;
		
		MyWindowUtil.setWindowsStyle();
		setBorder(UIManager.getBorder("TitledBorder.border"));
		
		Dimension rowDimension = new Dimension(p_width, p_height);
		FlowLayout layout = null;
		for(int i = 0; i < pn_rows.length; i++){
			
			pn_rows[i] = new JPanel();
			layout = (FlowLayout) pn_rows[i].getLayout();
			layout.setHgap(20);
			layout.setVgap(0);
			layout.setAlignment(i % 2 == 0 ? FlowLayout.LEFT : FlowLayout.RIGHT);
			
			pn_rows[i].setOpaque(false);
			pn_rows[i].setLayout(layout);
			pn_rows[i].setPreferredSize(rowDimension);
//			pn_rows[i].setBorder(new EtchedBorder());
			add(pn_rows[i]);
		}

	}
	
	public void setTaskModels(TaskModel[] taskModels){
		
		this.taskModels = taskModels;
		nodes.clear();
		for (JPanel pnr : pn_rows) {
			pnr.removeAll();
		}
		
		if(taskModels == null)return;
		for(int i = 0; i < taskModels.length; i++){
			addNode(new TaskNode(taskModels[i]));
		}
		repaint();
		
	}
	
	public void addNode(JPanel taskNode){
		
		int nodeCount = nodes.size();
		int rowNum = nodeCount / 6;
		
		//奇数列反向排列
		if(rowNum % 2 == 1){
			
			Component[] cs = pn_rows[rowNum].getComponents();
			pn_rows[rowNum].removeAll();
			pn_rows[rowNum].add(taskNode);
			for (Component component : cs) {
				pn_rows[rowNum].add(component);
			}
			
		}else{
			pn_rows[rowNum].add(taskNode);
		}
		
		nodes.add(taskNode);
		pn_rows[rowNum].updateUI();
	}
	
	public TaskModel[] getTaskModels(){
		return taskModels;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
//		g.drawLine(60, 30, 80, 30);
		lineBetweenTask(g, 2, 4);
	}
	
	private void lineBetweenTask(Graphics g, int index1, int index2){
		if(pn_rows.length <= index1 || pn_rows.length <= index2)return;
		Point location1 = pn_rows[index1].getLocation();
		Point location2 = pn_rows[index2].getLocation();
		g.drawLine(location1.x, location1.y, location2.x, location2.y);
		g.drawLine(60, 30, 80, 30);
	}

}
