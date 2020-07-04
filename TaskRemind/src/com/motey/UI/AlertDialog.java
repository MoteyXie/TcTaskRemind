package com.motey.UI;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.motey.factory.AlertDialogFactory;
import com.motey.model.TaskModel;
import com.motey.utils.ImageUtil;

import configuration.ConfigReader;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AlertDialog extends JDialog implements MouseListener {

	private static final long serialVersionUID = -5121352083391803224L;
	
//	public static int DISPLAY_TIME = 10000;
	public static int HEIGHT = 40;
	public static int WIDTH = 360;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblMessage;
	private TaskModel taskModel;
	private MainListFrame parent;

	private JLabel lblClose;
	
	private boolean mouseEnterFlag = false;

	private int autoCloseTime;
	
	public AlertDialog(TaskModel taskModel, MainListFrame parent){
		this.taskModel = taskModel;
		this.parent = parent;
		initialize();
	}

	private void initialize() {
		
		String message = taskModel.getDisplayName();
		ImageIcon icon = taskModel.getIcon();
		
		autoCloseTime = ConfigReader.getAlertDialogAutoCloseTime();
		
		setAlwaysOnTop(true);
		setBounds(100, 100, WIDTH, HEIGHT);
		setUndecorated(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(135, 206, 235));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblMessage = new JLabel(message);
		lblMessage.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		lblMessage.setIcon(icon);
		lblMessage.setBounds(10, 5, 320, 27);
		contentPanel.add(lblMessage);
		
		lblClose = new JLabel("");
		lblClose.setIcon(ImageUtil.getImageIcon("/icons/close_116.png", 30, 30));
		lblClose.setBounds(325, 5, 30, 30);
		
		lblClose.addMouseListener(this);
		addMouseListener(this);
		
//		MyWindowUtil.setDragable(this);
		contentPanel.add(lblClose);
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				AlertDialogFactory.refreshPosition();
			}
			
		});
		
		if(autoCloseTime > 0)new LiftThread().start();
	}
	
	public void setMessage(String msg, ImageIcon icon){
		
		lblMessage = new JLabel(msg);
		
		icon = ImageUtil.setImageIconSize(icon, 40, 40);
		lblMessage.setIcon(icon);
		contentPanel.updateUI();
	}
	
	
	public void paint(Graphics g){
		super.paint(g);
		if(mouseEnterFlag){
			g.setColor(new Color(255,255,255));
			int w = getWidth() -1;
			int h = getHeight() -1;
			g.drawLine(0, 0, w, 0);
			g.drawLine(0, h, w, h);
			g.drawLine(0, 0, 0, h);
			g.drawLine(w, 0, w, h);
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(e.getSource().equals(lblClose)){
			dispose();
		}else if(e.getSource().equals(this)){
			if(parent != null){
				parent.displayDetail(taskModel);
			}
			dispose();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
			mouseEnterFlag = true;
			repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
			mouseEnterFlag = false;
			repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	
	class LiftThread extends Thread{
		
		public void run(){
			try {
				Thread.sleep(autoCloseTime * 1000);
				while(mouseEnterFlag){ }
				dispose();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

}
