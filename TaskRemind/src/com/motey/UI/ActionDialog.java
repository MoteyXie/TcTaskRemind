package com.motey.UI;

import java.awt.event.ActionListener;

import javax.swing.JDialog;

import com.motey.model.TaskModel;

public abstract class ActionDialog extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = -5081709424666340081L;
	protected MainListFrame parent;
	protected TaskModel taskModel;
	protected boolean isSuccess;
	protected String result;
	public ActionDialog(MainListFrame parent, TaskModel taskModel){
		this.parent = parent;
		this.taskModel = taskModel;
		
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setUndecorated(true);
		setLocationRelativeTo(parent);
		setModal(true);
	}
	
	public boolean isSuccess(){
		return isSuccess;
	}

	public String getResult() {
		return result;
	}

}
