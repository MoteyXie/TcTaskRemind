package com.motey.utils;

import java.io.File;
import java.io.InputStream;
import com.motey.utils.sound.SoundPaly;

public class SoundUtil {
	
	
	public static File MESSAGE_SOUND = null;
	
	public static final int PLAY_INTERVAL = 5000;
	
	public static boolean play_flag = true;
	
	private static PlayThread playThread = null;

	private static InputStream input;
	
	public static void playMessageSound(){
		
		playThread = new PlayThread();
		playThread.start();
		playThread = null;
		
	}

	private static void messageSound(){
		
		try {
			
			input = SoundUtil.class.getResourceAsStream("/mic/sound2.mp3");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			play_flag = false;
			SoundPaly.paly(input);
			Thread.sleep(PLAY_INTERVAL);
			play_flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	static class PlayThread extends Thread{
		
		public void run(){
			if(play_flag)messageSound();
		}
		
	}

}


class Countdown extends Thread{

	public void run(){
		try {
			Thread.sleep(SoundUtil.PLAY_INTERVAL);
			SoundUtil.play_flag = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
