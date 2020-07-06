package configuration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.motey.UI.MessageDialog;
import com.motey.utils.FileUtil;

public class ConfigWriter {
	
	public static final String CONFIG_FILE_PATH = "/configuration/configuration.xml";
	public static final String CONFIG_FILE_NAME = "configuration.xml";
	public static String CONFIG_FILE_ABS_PATH = null;
	public static File CONFIG_FILE_ABS = null;
	
	private static XMLWriter writer = null;

	private static OutputFormat format = null;
	
	public static void writeLogginer(String userName, String password, String iconPath){
		if(userName == null)userName = "";
		if(password == null)password = "";
		if(iconPath == null)iconPath = "";
		
		try {
			Document document = read();
			Element root = document.getRootElement();
			
			//找到旧的记录删掉，把新的写进去，确保最后一次登录的数据保存到最末端
			for (Object object : root.elements("User")) {
				Element e = (Element) object;
				if(e.attributeValue("name").equals(userName)){
					root.remove(e);
					break;
				}
			}
			
			root.addElement("User")
			.addAttribute("name", userName)
			.addAttribute("password", password)
			.addAttribute("icon", iconPath);
			
			save(document);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void writeLink(String serverIP, String serverHost){
		try {
			Document document = read();
			Element root = document.getRootElement();
			
			Element linkElement = root.element("Link");
			if(linkElement == null){
				linkElement = root.addElement("Link");
			}
			
			Attribute ipa = linkElement.attribute("ip");
			if(ipa == null){
				linkElement.addAttribute("ip", serverIP);
			}else{
				ipa.setValue(serverIP);
			}
			Attribute hosta = linkElement.attribute("host");
			if(hosta == null){
				linkElement.addAttribute("host", serverHost);
			}else{
				hosta.setValue(serverHost);
			}
			save(document);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void writeRefreshInteval(int time){
		try {
			Document document = read();
			Element root = document.getRootElement();
			
			Element linkElement = root.element("Refresh");
			if(linkElement == null){
				linkElement = root.addElement("Refresh");
			}
			
			Attribute ipa = linkElement.attribute("interval");
			if(ipa == null){
				linkElement.addAttribute("interval", String.valueOf(time));
			}else{
				ipa.setValue(String.valueOf(time));
			}
			save(document);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void setAlertDialogAutoCloseTime(int time){
		
		try {
			Document document = read();
			Element root = document.getRootElement();
			
			Element linkElement = root.element("Alert");
			if(linkElement == null){
				linkElement = root.addElement("Alert");
			}
			
			Attribute ipa = linkElement.attribute("autoCloseTime");
			if(ipa == null){
				linkElement.addAttribute("autoCloseTime", String.valueOf(time));
			}else{
				ipa.setValue(String.valueOf(time));
			}
			save(document);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void writeIsAutoLogin(boolean flag){
		
		String str = flag ? "TRUE":"FALSE";
		try {
			Document document = read();
			Element root = document.getRootElement();
			Element autoElement = root.element("AutoLogin");
			if(autoElement == null){
				autoElement = root.addElement("AutoLogin");
			}
			autoElement.setText(str);
			
			save(document);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void writeIsRelatedRichClient(boolean flag) {
		String str = flag ? "TRUE":"FALSE";
		try {
			Document document = read();
			Element root = document.getRootElement();
			Element autoElement = root.element("RelatedRichClient");
			if(autoElement == null){
				autoElement = root.addElement("RelatedRichClient");
			}
			autoElement.setText(str);
			
			save(document);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void writeRichClientPath(String path) {
		try {
			Document document = read();
			Element root = document.getRootElement();
			Element autoElement = root.element("RichClientPath");
			if(autoElement == null){
				autoElement = root.addElement("RichClientPath");
			}
			autoElement.setText(path);
			
			save(document);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void setStartup(boolean flag) throws Exception{
		
		String str = flag ? "TRUE":"FALSE";
		
		Document document = read();
		Element root = document.getRootElement();
		Element autoElement = root.element("Startup");
		if(autoElement == null){
			autoElement = root.addElement("Startup");
		}
		autoElement.setText(str);
		
		save(document);
		
		setStartupBat(flag);
		
	}
	
	private static void setStartupBat(boolean isStartup) throws Exception {
		String osName = System.getProperty("os.name").toLowerCase();
		System.out.println("当前操作系统为 "+osName);
		String startupPath = "C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Roaming\\Microsoft\\windows\\Start Menu\\Programs\\Startup";
		File startupFile = new File(startupPath, "PLM_REMIND.bat");
		
		if(isStartup){
			String runPath = java.net.URLDecoder.decode(ConfigWriter.class.getResource("/").getFile(),"utf-8"); 
			if(runPath.startsWith("\\") || runPath.startsWith("/"))runPath = runPath.substring(1);
			FileUtil.copyFile(new File(runPath+"run.bat"), startupFile);
			
			if(!startupFile.exists()) {
				throw new Exception("设置开机自启动失败，可尝试关闭安全软件后重试！");
			}
			
			String runText = FileUtil.readData(startupFile.getAbsolutePath());
			runText = runText.replace("%~dp0", runPath).replace("%cd%", runPath);
			
			FileUtil.writeTxtFile(startupFile.getAbsolutePath(), runText, true);
		}else{
			if(startupFile.exists()){
				startupFile.delete();
			}
		}
	}
	
	public static void initStartWithTargetBat() throws Exception {
		
		String richClientPath = ConfigReader.getRichClientPath();
		richClientPath = richClientPath.replace("\\", "/");
		if(richClientPath.endsWith("/")) {
			richClientPath = richClientPath.substring(0, richClientPath.length() - 1);
		}
		File checkFile = new File(richClientPath + "/Teamcenter.exe");
		
		if(!checkFile.exists())
			throw new Exception("客户端路径 [" + richClientPath + "] 无效！");
		
		String[] resNames = {"start_tc_with_target.bat","target.ugs"};
		String rootPath = checkFile.getParentFile().getParent();
		
		for (String resName : resNames) {
			String path = richClientPath + "/" + resName;
			File file = new File(path);
			if(file.exists()) {
				continue;
			}
			InputStream is = ConfigWriter.class.getResourceAsStream("/resources/"+resName);
			FileOutputStream fos = new FileOutputStream(path);
			FileUtil.copyFile(is, fos);
			
			String data = FileUtil.readData(path);
			data = data.replace("#{ROOT_PATH}", rootPath);
			FileUtil.writeTxtFile(path, data, true);
		}
	}

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
	
	public static void save(Document document) throws Exception{
		 
		if(format == null){
			format = OutputFormat.createPrettyPrint(); 
			format.setEncoding("utf-8");// 设置XML文件的编码格式  
		}
		File configFile = ConfigReader.CONFIG_FILE_ABS;
//		OutputStream out = new FileOutputStream(ConfigReader.class.getResource(CONFIG_FILE_PATH).toURI().getPath()); 
		OutputStream out = new FileOutputStream(configFile);
		Writer wr = new OutputStreamWriter(out, "UTF-8");  
		writer = new XMLWriter(wr, format);
        writer.write(document);  
        writer.close();
	}

}
