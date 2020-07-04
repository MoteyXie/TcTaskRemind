package com.motey.UI;

import javax.swing.JPanel;

/**
 * 设置面板基类
 * @author Motey
 *
 */
public abstract class OptionPanel extends JPanel {

	private static final long serialVersionUID = 7892312057006262001L;
	
	private String name;
	
	public abstract void save();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



}
