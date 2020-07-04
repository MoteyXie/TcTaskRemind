package com.motey.utils.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ClipPaly {

	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException, InterruptedException {
		//得到音乐文件的File 对象
		File file = new File("8912289ddbc2bc06df6aabf9ba736b44.wav");
		
		paly(file);
	}

	/**
	 * @param file 希望被播放的音乐文件
	 * @param count 先播放一遍后，再播放count遍
	 */
	public static void paly(File file,int count) throws UnsupportedAudioFileException,
			IOException, LineUnavailableException, InterruptedException {
		//获得音频输入流
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

		//获得可用于回放音频文件或音频流的剪辑
		Clip clip = AudioSystem.getClip();  
		
		//打开剪辑，获得所有所需的系统资源并变得可操作
		clip.open( audioInputStream);
		
		//设置回放的次数，即 回放的次数=播放的次数-1，需要注意的是这个方法是异步的，适当延时才能让音频播放
		clip.loop(count);
		
		//延时播放(count+1)遍需要的时间
		Thread.sleep((count+1) * clip.getMicrosecondLength()/1000);
	}
	/**
	 * @param file 希望被播放的音乐文件
	 */
	public static void paly(File file) throws UnsupportedAudioFileException,
	IOException, LineUnavailableException, InterruptedException {
		paly(file,0);
	}

}
