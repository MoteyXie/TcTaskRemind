package com.motey.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * 窗口操作工具
 * @author Motey
 *
 */
public class MyWindowUtil {

	/**
	 * 为窗口加上监听器，使得窗口可以被点击拖动 
	 * @param frame
	 * @return
	 */
    public static DragableListener setDragable(JFrame frame) {  
    	return new DragableListener(frame);
    }
    
    public static DragableListener setDragable(JDialog dialog) {  
    	return new DragableListener(dialog);
    }
    
    /**
     * 为控件加上监听器，使其所在的窗口可以被点击拖动 
     * @param parent 移动的窗口
     * @param component 监听的控件
     * @return
     */
    public static DragableListener setDragable(JFrame parent, JComponent component){
    	return new DragableListener(parent, component);
    }
    
    /**
     * 为控件加上监听器，使其所在的窗口可以被点击拖动 
     * @param parent 移动的窗口
     * @param component 监听的控件
     * @return
     */
    public static DragableListener setDragable(JDialog parent, JComponent component){
    	return new DragableListener(parent, component);
    }
    
    /**
     * 设置控件在鼠标移动过去后改变背景颜色
     * @param comp 控件
     * @param color 需改变的颜色
     */
	public static void setChangeBackgroundListener(final JComponent comp,final Color color){
		
		comp.addMouseListener(new MouseAdapter() {
			
			Color previousColor = comp.getBackground();
			boolean isOpaque = comp.isOpaque();
			@Override
			public void mouseEntered(MouseEvent e) {
				comp.setBackground(color);
				comp.setOpaque(true);
			}
			
			public void mouseExited(MouseEvent e){
				comp.setBackground(previousColor);
				comp.setOpaque(isOpaque);
			}
			
		});
	}
	
	/**
	 * 鼠标按下控件组中的其中一个控件将会改变其颜色，而之前已改变的将会复原
	 * @param comps 控件组
	 * @param color 按下后改变成的颜色
	 * @return 监听器
	 */
	public static ClickedChangeBackgroundListener setClickedChangeBackgroundListener(JComponent[] comps, Color color){
		return new ClickedChangeBackgroundListener(comps, color);
	}
	
	public static void setWindowsStyle(){
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}

class ClickedChangeBackgroundListener{
	
	private JComponent lastClickedComponent = null;
	private Color previousColor = null;
	
	public ClickedChangeBackgroundListener(final JComponent[] comps,final Color color){
		
		previousColor = comps[0].getBackground();
		
		lastClickedComponent = comps[0];
		lastClickedComponent.setBackground(color);
		lastClickedComponent.setOpaque(true);
		lastClickedComponent.repaint();
		
		for (final JComponent comp : comps) {
			
			comp.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					comp.setOpaque(true);
					if(lastClickedComponent != null){
						lastClickedComponent.setBackground(previousColor);
						
					}
					comp.setBackground(color);
					lastClickedComponent = comp;
					comp.repaint();
				}
			});
		}
	}
	
}

class DragableListener{
	
	private boolean isMoved = false;
	private Point pre_point = null;
	private Point end_point = null;
	
	public DragableListener(final JDialog dialog){
		
		dialog.addMouseListener(new java.awt.event.MouseAdapter() {  

			public void mouseReleased(java.awt.event.MouseEvent e) {  
                isMoved = false;// 鼠标释放了以后，是不能再拖拽的了  
                dialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
            }  
  
            public void mousePressed(java.awt.event.MouseEvent e) {  
                isMoved = true;  
                pre_point = new Point(e.getX(), e.getY());// 得到按下去的位置  
                dialog.setCursor(new Cursor(Cursor.MOVE_CURSOR));  
            }  
        });  
        //拖动时当前的坐标减去鼠标按下去时的坐标，就是界面所要移动的向量。  
    	dialog.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {  
            public void mouseDragged(java.awt.event.MouseEvent e) {  
                if (isMoved) {// 判断是否可以拖拽  
                    end_point = new Point(dialog.getLocation().x + e.getX() - pre_point.x,  
                    		dialog.getLocation().y + e.getY() - pre_point.y);  
                    dialog.setLocation(end_point);  
                }  
            }  
        });
    	
	}
	
	public DragableListener(final JFrame frame){
		
		frame.addMouseListener(new java.awt.event.MouseAdapter() {  

			public void mouseReleased(java.awt.event.MouseEvent e) {  
                isMoved = false;// 鼠标释放了以后，是不能再拖拽的了  
                frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
            }  
  
            public void mousePressed(java.awt.event.MouseEvent e) {  
                isMoved = true;  
                pre_point = new Point(e.getX(), e.getY());// 得到按下去的位置  
                frame.setCursor(new Cursor(Cursor.MOVE_CURSOR));  
            }  
        });  
        //拖动时当前的坐标减去鼠标按下去时的坐标，就是界面所要移动的向量。  
    	frame.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {  
            public void mouseDragged(java.awt.event.MouseEvent e) {  
                if (isMoved) {// 判断是否可以拖拽  
                    end_point = new Point(frame.getLocation().x + e.getX() - pre_point.x,  
                    		frame.getLocation().y + e.getY() - pre_point.y);  
                    frame.setLocation(end_point);  
                }  
            }  
        });
    	
	}
	
	public DragableListener(final JDialog parent,final JComponent component){
		
		component.addMouseListener(new java.awt.event.MouseAdapter() {  

			public void mouseReleased(java.awt.event.MouseEvent e) {  
                isMoved = false;// 鼠标释放了以后，是不能再拖拽的了  
                component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
            }  
  
            public void mousePressed(java.awt.event.MouseEvent e) {  
                isMoved = true;  
                pre_point = new Point(e.getX(), e.getY());// 得到按下去的位置  
                component.setCursor(new Cursor(Cursor.MOVE_CURSOR));  
            }  
        });  
        //拖动时当前的坐标减去鼠标按下去时的坐标，就是界面所要移动的向量。  
		component.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {  
            public void mouseDragged(java.awt.event.MouseEvent e) {  
                if (isMoved) {// 判断是否可以拖拽  
                    end_point = new Point(parent.getLocation().x + e.getX() - pre_point.x,  
                    		parent.getLocation().y + e.getY() - pre_point.y);  
                    parent.setLocation(end_point);  
                }  
            }  
        });
	}
	
	public DragableListener(final JFrame parent, final JComponent component){
		
		component.addMouseListener(new java.awt.event.MouseAdapter() {  

			public void mouseReleased(java.awt.event.MouseEvent e) {  
                isMoved = false;// 鼠标释放了以后，是不能再拖拽的了  
                component.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
            }  
  
            public void mousePressed(java.awt.event.MouseEvent e) {  
                isMoved = true;  
                pre_point = new Point(e.getX(), e.getY());// 得到按下去的位置  
                component.setCursor(new Cursor(Cursor.MOVE_CURSOR));  
            }  
        });  
        //拖动时当前的坐标减去鼠标按下去时的坐标，就是界面所要移动的向量。  
		component.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {  
            public void mouseDragged(java.awt.event.MouseEvent e) {  
                if (isMoved) {// 判断是否可以拖拽  
                    end_point = new Point(parent.getLocation().x + e.getX() - pre_point.x,  
                    		parent.getLocation().y + e.getY() - pre_point.y);  
                    parent.setLocation(end_point);  
                }  
            }  
        });
	}
}
