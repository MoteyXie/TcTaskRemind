package com.motey.UI;

import javax.swing.JPanel;
import javax.swing.UIManager;

import com.motey.model.TaskModel;
import com.motey.utils.MyWindowUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class FlowChartPanel extends JPanel {

	private static final long serialVersionUID = 4532132523581309211L;

	//初始位置距离原点的距离
	private static int ORIGIN = 30;
	//控件间的间隔
	private static int INTERVAL = 75;
	//控件的大小
	private static int NODE_SIZE = 45;
	//控件中间
	private static int HALF_NODE_SIZE = NODE_SIZE >> 1 ;
	//偏移量
	private static int OFFSET = 5;
	
	//每行可容纳的最大控件数量
	private static int COLUMN_NODE_COUNT = 5;	
	
	private Graphics2D g2d = null;
	
	private List<TaskModel> taskModels = null;
	private List<Integer[]> taskRelateds = new ArrayList<>();
	private List<TaskNode> nodes = new ArrayList<>();

	public FlowChartPanel() {
		
		setLayout(null);
		
		MyWindowUtil.setWindowsStyle();
		setBorder(UIManager.getBorder("TitledBorder.border"));

	}
	
	public void setTaskModels(ArrayList<TaskNode> taskNodes){
		
		nodes.clear();
		taskRelateds.clear();
		
		removeAll();
		if(taskNodes == null)return;
		
		for(int i = 0; i < taskNodes.size(); i++){
			addNode(taskNodes.get(i));
			
			ArrayList<TaskNode> nexts = taskNodes.get(i).getNextNodes();
			if(nexts == null)continue;
			for(int j = 0; j < taskNodes.size(); j++){
				
				if(nexts.contains(taskNodes.get(j))){
					taskRelateds.add(new Integer[]{i,j});
				}
				
			}
		}
		
		repaint();
	}
	
	public void addNode(TaskNode taskNode){
		
		int nodeCount = nodes.size();	//得到当前任务的个数
		int rowNum = nodeCount / COLUMN_NODE_COUNT;		//计算待添加任务所在列
		
		int columnNum = 0;
		
		//计算待添加任务所在行,奇数列反向排序 
		if(rowNum % 2 == 0){
			columnNum = nodeCount % COLUMN_NODE_COUNT;	
		}else{
			columnNum = COLUMN_NODE_COUNT - nodeCount % COLUMN_NODE_COUNT - 1;
		}
		
		
		int y = rowNum * INTERVAL + ORIGIN;		//计算任务控件的Y坐标
		int x = columnNum * INTERVAL + ORIGIN;	//计算任务控件的X坐标
		
		taskNode.setBounds(x, y, NODE_SIZE, NODE_SIZE);
		
		nodes.add(taskNode);
		add(taskNode);
		
	}
	
	public List<TaskModel> getTaskModels(){
		
		return taskModels;
	}
	
	@Override
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(1.5f));
		
		if(taskRelateds != null){
			for (Integer[] index : taskRelateds) {
				
//				lineBetweenTask(g2d, index[0]+1, index[1]+1);
				lineBetweenTask(g2d, index[0], index[1]);
			}
		}
		
//		lineBetweenTask(g2d, 0, 1);
	}
	
	private void lineBetweenTask(Graphics2D g, int index1, int index2){
		
		if(nodes.size() <= index1 || nodes.size() <= index2)return;
		
		TaskNode node1 = nodes.get(index1);
		TaskNode node2 = nodes.get(index2);
		
		int x1 = node1.getLocation().x + HALF_NODE_SIZE;
		int y1 = node1.getLocation().y + HALF_NODE_SIZE + OFFSET;	//这里加偏移量是为了让线在控件的正中心
		int x2 = node2.getLocation().x + HALF_NODE_SIZE;
		int y2 = node2.getLocation().y + HALF_NODE_SIZE + OFFSET;	//同上
		
		double ctrlx1 = x1;
		double ctrlx2 = x2;
		double ctrly1 = y1;
		double ctrly2 = y2;
		
		int alx1 = x1;
		int aly1 = y1;
		
		g.setColor(Color.BLUE);
		
		//同一行
		if(y1 == y2){
			
			ctrlx1 = (x1 + x2)>>1;
			ctrlx2 = ctrlx1;
			
			//同一行且相邻
			if(Math.abs(x2 - x1) <= INTERVAL){
				
				if(x2 > x1){
					x1 = node1.getRightPoint().x;
					y1 = node1.getRightPoint().y;
					x2 = node2.getLeftPoint().x;
					y2 = node2.getLeftPoint().y;
				}else{
					x1 = node1.getLeftPoint().x;
					y1 = node1.getLeftPoint().y;
					x2 = node2.getRightPoint().x;
					y2 = node2.getRightPoint().y;
				}
				
				ctrlx1 = (x1 + x2)>>1;
				ctrlx2 = ctrlx1;
				ctrly1 = y1;
				ctrly2 = y1;
						
				alx1 = x1;
				aly1 = y1;
				
//				g.setColor(Color.BLUE);
				
			//同一行但不相邻
			}else{
				
				x1 = node1.getTopPoint().x;
				y1 = node1.getTopPoint().y;
				x2 = node2.getTopPoint().x;
				y2 = node2.getTopPoint().y;
				
//				g.setColor(Color.cyan);
				
				if(x2 > x1){
					ctrlx1 = x1+2;
					ctrlx2 = x2-2;
				}else{
					ctrlx1 = x1-2;
					ctrlx2 = x2+2;
				}
				
				ctrly1 = y1 - HALF_NODE_SIZE;
				ctrly2 = ctrly1;
				
				aly1 = y1 - 4*NODE_SIZE;
				alx1 = x1;
			}
		
		//同一列
		}else if(x1 == x2){
			
			ctrly1 = (y1 + y2)>>1;
			ctrly2 = ctrly1;
			
			//同一列且相邻
			if(Math.abs(y2 - y1) <= INTERVAL){
				
				if(y2 > y1){
					x1 = node1.getBottomPoint().x;
					y1 = node1.getBottomPoint().y;
					x2 = node2.getTopPoint().x;
					y2 = node2.getTopPoint().y;
				}else{
					x1 = node1.getTopPoint().x;
					y1 = node1.getTopPoint().y;
					x2 = node2.getBottomPoint().x;
					y2 = node2.getBottomPoint().y;
				}
				
				ctrlx1 = x1;
				ctrlx2 = x1;
				ctrly1 = (y1 + y2)>>1;
				ctrly2 = ctrly1;
				
				alx1 = x1;
				aly1 = y1;
				
//				g.setColor(Color.green);
			
			//同一列但不相邻
			}else{
				
				x1 = node1.getLeftPoint().x;
				y1 = node1.getLeftPoint().y;
				x2 = node2.getLeftPoint().x;
				y2 = node2.getLeftPoint().y;
				
				ctrlx1 = x1 - HALF_NODE_SIZE;
				ctrlx2 = ctrlx1;
				
				if(y2 > y1){
					ctrly1 = y1+2;
					ctrly2 = y2-2;
				}else{
					ctrly1 = y1-2;
					ctrly2 = y2+2;
				}
				
				aly1 = y1;
				alx1 = x1 - 4*NODE_SIZE;
				
//				g.setColor(Color.ORANGE);
			}
			
		//不同行不同列
		}else{
			
			if(y2 > y1){
				x1 = node1.getBottomPoint().x;
				y1 = node1.getBottomPoint().y;
				x2 = node2.getTopPoint().x;
				y2 = node2.getTopPoint().y;
				
				ctrly1 = y1+2;
				ctrly2 = y2-2;
				
			}else{
				x1 = node1.getTopPoint().x;
				y1 = node1.getTopPoint().y;
				x2 = node2.getBottomPoint().x;
				y2 = node2.getBottomPoint().y;
				
				ctrly1 = y1-HALF_NODE_SIZE;
				ctrly2 = y2+HALF_NODE_SIZE;
			}
			
			if(x2 > x1){
				ctrlx1 = x1+NODE_SIZE;
				ctrlx2 = x2-NODE_SIZE;
			}else{
				ctrlx1 = x1-NODE_SIZE;
				ctrlx2 = x2+HALF_NODE_SIZE;
			}
			
//			g.setColor(Color.RED);
		}
		
		CubicCurve2D cubic = new CubicCurve2D.Double(
		(double)x1, 
		(double)y1,
		ctrlx1,
		ctrly1,
		ctrlx2,
		ctrly2,
		(double)x2, 
		(double)y2);
		
		g.draw(cubic);
		
//		g.fillOval(x1-5, y1-5, 10, 10);
//		g.fillRect(x2-5, y2-5, 10, 10);
//		g.fillRect(x1-5, y1-5, 10, 10);
		drawAL(alx1, aly1, x2, y2, g);
	}
	
    public void drawAL(int sx, int sy, int ex, int ey, Graphics2D g2)  
    {  
  
        double H = 12; // 箭头高度  
        double L = 5; // 底边的一半  
        int x3 = 0;  
        int y3 = 0;  
        int x4 = 0;  
        int y4 = 0;  
        double awrad = Math.atan(L / H); // 箭头角度  
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度  
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);  
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);  
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点  
        double y_3 = ey - arrXY_1[1];  
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点  
        double y_4 = ey - arrXY_2[1];  
  
        Double X3 = new Double(x_3);  
        x3 = X3.intValue();  
        Double Y3 = new Double(y_3);  
        y3 = Y3.intValue();  
        Double X4 = new Double(x_4);  
        x4 = X4.intValue();  
        Double Y4 = new Double(y_4);  
        y4 = Y4.intValue();  
        // 画线  
//        g2.drawLine(sx, sy, ex, ey);  
        //  
        GeneralPath triangle = new GeneralPath();  
        triangle.moveTo(ex, ey);  
        triangle.lineTo(x3, y3);  
        triangle.lineTo(x4, y4);  
        triangle.closePath();  
        //实心箭头  
        g2.fill(triangle);  
        //非实心箭头  
        //g2.draw(triangle);  
  
    }  
  
    // 计算  
    public static double[] rotateVec(int px, int py, double ang,  
            boolean isChLen, double newLen) {  
  
        double mathstr[] = new double[2];  
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度  
        double vx = px * Math.cos(ang) - py * Math.sin(ang);  
        double vy = px * Math.sin(ang) + py * Math.cos(ang);  
        if (isChLen) {  
            double d = Math.sqrt(vx * vx + vy * vy);  
            vx = vx / d * newLen;  
            vy = vy / d * newLen;  
            mathstr[0] = vx;  
            mathstr[1] = vy;  
        }  
        return mathstr;  
    }  

}
