package configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.motey.utils.FileUtil;

/**
 * 配置读取器
 * @author Motey
 *
 */
public class ConfigReader {
	
	public static final String CONFIG_FILE_PATH = "/configuration/configuration.xml";
	public static final String CONFIG_FILE_NAME = "configuration.xml";
	public static String CONFIG_FILE_ABS_PATH = null;
	public static File CONFIG_FILE_ABS = null;
	
	public static Document read() throws MalformedURLException, DocumentException {
	       SAXReader reader = new SAXReader();
//	       System.out.println(ConfigReader.class.getResource(CONFIG_FILE_PATH));
	       
	       if(CONFIG_FILE_ABS == null){
	    	   
	    	   try{
	    		   CONFIG_FILE_ABS_PATH = ConfigReader.class.getResource("/").getFile() + CONFIG_FILE_NAME;
	    		   CONFIG_FILE_ABS_PATH = java.net.URLDecoder.decode(CONFIG_FILE_ABS_PATH,"utf-8");  
	    		   System.out.println(CONFIG_FILE_ABS_PATH);
	    		   CONFIG_FILE_ABS = new File(CONFIG_FILE_ABS_PATH);
	    		   
	    		   if(!CONFIG_FILE_ABS.exists()){
	    			   System.out.println(CONFIG_FILE_ABS_PATH + " not found! creation!");
	    			   InputStream is = ConfigReader.class.getResourceAsStream(CONFIG_FILE_PATH);
			    	   FileOutputStream fos = new FileOutputStream(CONFIG_FILE_ABS);
			    	   FileUtil.copyFile(is, fos); 
	    		   }
	    		   
	    	   }catch(Exception e){
	    		   e.printStackTrace();
	    	   }
	    	   
	       }
	       
//	       Document document = reader.read(ConfigReader.class.getResource(CONFIG_FILE_PATH));
	       Document document = reader.read(CONFIG_FILE_ABS);
	       return document;
	}

	
	/**
	 * @return 所有登录过的用户名
	 */
	public static String[] getLoginerNames(){
		
		String[] userArray = null;
		try{
			Document document = read();
			Element root = document.getRootElement();
			List<?> userElements = root.elements("User");
			userArray = new String[userElements.size()];
			
			//从后往前排
			for(int i = 0; i < userElements.size(); i++){
				Element e = (Element) userElements.get(i);
				userArray[userArray.length - i - 1] = e.attributeValue("name");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return userArray;
	}
	
	/**
	 * @param userName 用户名
	 * @return 记录的密码
	 */
	public static String getPassword(String userName){
		
		String password = "";
		try{
			Document document = read();
			Element root = document.getRootElement();
			List<?> userElements = root.elements("User");
			for (Object object : userElements) {
				Element e = (Element) object;
				if(e.attributeValue("name").equals(userName)){
					password = e.attributeValue("password");
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return password;
	}
	
	public static String getIconPath(String userName){
		
		String iconPath = "";
		try{
			Document document = read();
			Element root = document.getRootElement();
			List<?> userElements = root.elements("User");
			for (Object object : userElements) {
				Element e = (Element) object;
				if(e.attributeValue("name").equals(userName)){
					iconPath = e.attributeValue("icon");
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return iconPath;
	}
	
	/**
	 * 获取连接信息
	 * @return{IP,host}
	 */
	public static String[] getLink(){
		
		String[] link = new String[2];
		try{
			Document document = read();
			Element root = document.getRootElement();
			Element linkElement = root.element("Link");
			link[0] = linkElement.attributeValue("ip");
			link[1] = linkElement.attributeValue("host");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return link;
	}
	
	public static int getRefreshInterval(){
		
		int interval = 0;
		try{
			Document document = read();
			Element root = document.getRootElement();
			Element linkElement = root.element("Refresh");
			String intervalStr = linkElement.attributeValue("interval");
			interval = Integer.parseInt(intervalStr);
		}catch(Exception e){
//			e.printStackTrace();
		}
		return interval;
	}
	
	/**
	 * 是否关联胖客户端
	 * @return
	 */
	public static boolean isRelatedRichClient() {
		try{
			Document document = read();
			Element root = document.getRootElement();
			String flag = root.element("RelatedRichClient").getStringValue();
			
			return flag.equals("TRUE");
		}catch(Exception e){
		}
		return false;
	}
	
	/**
	 * 胖客户端路径
	 * @return
	 */
	public static String getRichClientPath(){
		
		try{
			Document document = read();
			Element root = document.getRootElement();
			String s = root.element("RichClientPath").getStringValue();
			return s;
		}catch(Exception e){
		}
		return "";
	}
	
	/**
	 * @return 是否自动登录
	 */
	public static boolean isAutoLogin(){
		try{
			Document document = read();
			Element root = document.getRootElement();
			String flag = root.element("AutoLogin").getStringValue();
			
			return flag.equals("TRUE");
		}catch(Exception e){
//			e.printStackTrace();
		}
		return false;
	}
	
	public static int getAlertDialogAutoCloseTime(){
		
		int autoClosetime = 0;
		try{
			Document document = read();
			Element root = document.getRootElement();
			String autoClosetimeStr = root.element("Alert").attribute("autoCloseTime").getStringValue();
			autoClosetime = Integer.parseInt(autoClosetimeStr);
		}catch(Exception e){
		}
		return autoClosetime;
	}
	
	/**
	 * @return 是否开机自启动
	 */
	public static boolean isStartup(){
		try{
			Document document = read();
			Element root = document.getRootElement();
			String flag = root.element("Startup").getStringValue();
			
			return flag.equals("TRUE");
		}catch(Exception e){
//			e.printStackTrace();
		}
		return false;
	}
	
}
