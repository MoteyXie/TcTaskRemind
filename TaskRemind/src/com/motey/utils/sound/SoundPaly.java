package com.motey.utils.sound;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPaly {
	private static AudioInputStream audioInputStream;
	private static AudioFormat audioFormat;
	private static SourceDataLine res;

//	public static void main(String[] args)
//			throws UnsupportedAudioFileException, IOException,
//			LineUnavailableException {
//		
//		File file = new File(SoundPaly.class.getResource("/mic/sound2.mp3").getFile());
//		paly(file);//mp3格式的文件不支持,经过转换为mp3编码之后才支持
//	}

	public static void paly(InputStream input) throws UnsupportedAudioFileException,
			IOException, LineUnavailableException {
		audioInputStream = AudioSystem.getAudioInputStream(input);//得到要播放的文件的音频输入流
		audioFormat = audioInputStream.getFormat();//音频流的

		// 转换mp3文件编码
		if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
			audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					audioFormat.getSampleRate(), 16,
					audioFormat.getChannels(),
					audioFormat.getChannels() * 2,
					audioFormat.getSampleRate(), false);
			audioInputStream = AudioSystem.getAudioInputStream(audioFormat,
					audioInputStream);
		}
		
		res = AudioSystem.getSourceDataLine(audioFormat);//得到输出设备对象
		
		res.open(audioFormat);//打开输出设备
//		res.open();
		res.start();//开始播放
		
		int inBytes = 0;
		byte[] audioData = new byte[1024*200];//缓冲区
		while ((inBytes != -1)) {
			inBytes = audioInputStream.read(audioData, 0, audioData.length);//从音频输入流读取音频数据到缓冲区

			if (inBytes >= 0) {//把音频数据写入到输出设备
				res.write(audioData, 0, inBytes);
			}
		}
		res.drain();//直到输出设备对象缓冲区的数据被输出，返回。加上这句，能完整的输出整个音频流
		res.close();//关闭输出设备
		audioInputStream.close();//关闭音频流
	}

}
