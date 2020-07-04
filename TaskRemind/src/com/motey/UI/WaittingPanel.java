package com.motey.UI;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Color;

public class WaittingPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		frame.getContentPane().add(new WaittingPanel(), BorderLayout.CENTER);
		frame.setVisible(true);
	}

	public WaittingPanel() {
		setBackground(new Color(254, 254, 254));
		setLayout(null);
		
		JLabel label = new JLabel();
		label.setBounds(77, 40, 220, 220);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setIcon(new ImageIcon(WaittingPanel.class.getResource("/images/loading2.gif")));
		add(label);
	}
	

}
