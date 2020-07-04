package com.motey.factory;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.LinkedList;

import com.motey.UI.AlertDialog;
import com.motey.UI.MainListFrame;
import com.motey.model.TaskModel;
import com.motey.utils.SoundUtil;

public class AlertDialogFactory {
	
	public static int FIRST_Y = 0;
	public static int FIRST_X = 0;
	public static int DIALOG_HEIGHT = AlertDialog.HEIGHT + 5;
	
	private static LinkedList<AlertDialog> DIALOG_POOL = new LinkedList<>();
	
	public static void create(TaskModel taskModel, MainListFrame parent){
		
		AlertDialog a = new AlertDialog(taskModel, parent);
		
		if(FIRST_Y == 0){
			FIRST_Y=Toolkit.getDefaultToolkit().getScreenSize().height-Toolkit.getDefaultToolkit().getScreenInsets(a.getGraphicsConfiguration()).bottom-a.getHeight()-5;
		}
		
		if(FIRST_X == 0){
			FIRST_X = Toolkit.getDefaultToolkit().getScreenSize().width-Toolkit.getDefaultToolkit().getScreenInsets(a.getGraphicsConfiguration()).right-a.getWidth()-5;
		}
		
		a.setLocation(FIRST_X, FIRST_Y);
		
		a.setVisible(true);
		
		DIALOG_POOL.add(a);
		
		SoundUtil.playMessageSound();
		
		refreshPosition();
		
	}
	
	public static void refreshPosition(){
		
		ArrayList<AlertDialog> temp = new ArrayList<>();
		for(int i = DIALOG_POOL.size() - 1; i >= 0; i--){
			
			if(!DIALOG_POOL.get(i).isDisplayable()){
				temp.add(DIALOG_POOL.get(i));
				continue;
			}
		}
		
		for (AlertDialog alertDialog : temp) {
			DIALOG_POOL.remove(alertDialog);
		}
		
		for(int i = DIALOG_POOL.size() - 1; i >= 0; i--){
			int c = DIALOG_POOL.size() - 1 - i;
			int y = FIRST_Y - c * DIALOG_HEIGHT;
			DIALOG_POOL.get(i).setLocation(FIRST_X, y);
		}
		
		temp = null;
		
	}
	
//	public static void refreshPosition(){
//		
//		int flag = -1;
//		
//		for(int i = DIALOG_POOL.size() - 1; i >= 0; i--){
//			
//			if(!DIALOG_POOL.get(i).isDisplayable()){
//				flag = i;
//				break;
//			}
//			int c = DIALOG_POOL.size() - 1 - i;
//			int y = FIRST_Y - c * DIALOG_HEIGHT;
//			DIALOG_POOL.get(i).setLocation(FIRST_X, y);
//		}
//		
//		if(flag > -1){
//			
//			for(int i = flag; i < DIALOG_POOL.size(); i++){
//				DIALOG_POOL.remove(i);
//			}
//			
//		}
//		
//	}
	
//	public static void addCount(){
//		FIRST_Y -= DIALOG_HEIGHT;
//	}
//	
//	public static void minusCount(){
//		FIRST_Y += DIALOG_HEIGHT;
//	}
	

}
