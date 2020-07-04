package com.motey.UI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyProgressbar extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3569841908781317826L;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		frame.getContentPane().add(new MyProgressbar());
		frame.setVisible(true);
	}
	/**
	 * Create the panel.
	 */
	public MyProgressbar() {
		super();
	}
	
	@Override
	public void paintComponent(Graphics g) {
//        super.paint(g);
		super.paintComponent(g);
		g.setColor(new Color(10,10,10));
		g.fillRect(0, 10, 500, 40);
		
//		g.drawLine(0, 50, 500, 40);
    }

}
