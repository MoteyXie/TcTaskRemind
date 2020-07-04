package com.motey.utils;

import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageUtil {
	
	public static final ImageIcon DEFAULT_ICON = getImageIcon("/icons/tcdesktop_16.png",20,20);
	
	public static ImageIcon getImageIcon(String resourcePath, int width, int height){
		
		ImageIcon imageIcon = new ImageIcon(ImageUtil.class.getResource(resourcePath));
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(width,height,Image.SCALE_FAST)); 
		
		return imageIcon;
		
	}
	
	public static ImageIcon setImageIconSize(ImageIcon icon, int width, int height){
		icon.setImage(icon.getImage().getScaledInstance(width,height,Image.SCALE_FAST)); 
		return icon;
	}
	
	public static ImageIcon copyImageIcon(ImageIcon icon, int width, int height){
		
		String iconUrl = icon.toString();
		String iconName = iconUrl.substring(iconUrl.lastIndexOf("/")+1, iconUrl.length());
//		try {
//			iconUrl = URLDecoder.decode(iconUrl,"utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		ImageIcon icon2 = new ImageIcon(iconUrl);
		ImageIcon icon2 = new ImageIcon(ImageUtil.class.getResource("/icons/"+iconName));
		setImageIconSize(icon2, width, height);
		
		return icon2;
	}

}
