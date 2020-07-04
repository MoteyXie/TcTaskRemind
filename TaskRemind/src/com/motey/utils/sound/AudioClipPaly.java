package com.motey.utils.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class AudioClipPaly {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		
		//得到指向音乐文件的URL 对象
		URL url = new File("8912289ddbc2bc06df6aabf9ba736b44.wav").toURI().toURL();
//		URL url = new File("最炫民族风.wav").toURI().toURL();//如果音乐文件的的格式不被支持，则不作任何操作或提示
		
		//得到这个音乐文件的音频剪切板AudioClip
		AudioClip audioclip = Applet.newAudioClip(url);

		//循环播放音乐文件
//		audioclip.loop();
		audioclip.play();
		
	}

	
	
	
}
