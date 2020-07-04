package configuration;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.motey.model.TaskModel;
import com.motey.utils.FileUtil;

public class Cache {
	
	public static final String CONFIG_FILE_PATH = "/configuration/cache.xml";
	public static final String CONFIG_FILE_NAME = "cache.xml";
	public static String CONFIG_FILE_ABS_PATH = null;
	public static File CONFIG_FILE_ABS = null;
	
	private static XMLWriter writer = null;

	private static OutputFormat format = null;
	
	public static void writeTasks(List<TaskModel> taskModelList){
		
		System.out.println("Writing cache...");
		try {
			Document document = read();
			Element root = document.getRootElement();
			Element readedElement = root.element("Readed");
			
			if(readedElement != null){
				root.remove(readedElement);
			}
			
			readedElement = root.addElement("Readed");
			
			for (TaskModel taskModel : taskModelList) {
				readedElement.addElement("Task")
				.addAttribute("uid", taskModel.getUid())
				.setText(taskModel.getName());
			}
			save(document);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.out.println("success!");
	}
	
	public static List<TaskModel> getTaskModels(){
		
		List<TaskModel> taskModelList = new ArrayList<>();
		try {
			Document document = read();
			Element root = document.getRootElement();
			Element readedElement = root.element("Readed");
			
			TaskModel tm = null;
			for (Object o : readedElement.elements("Task")) {
				tm = new TaskModel();
				tm.setUid(((Element)o).attribute("uid").getValue());
				tm.setReaded(true);
				taskModelList.add(tm);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return taskModelList;
	}


//	public static Document read() throws MalformedURLException, DocumentException {
//	       SAXReader reader = new SAXReader();
//	       Document document = reader.read(ConfigReader.class.getResource(CONFIG_FILE_PATH));
//	       return document;
//	}
	
	public static Document read() throws MalformedURLException, DocumentException {
		
	       SAXReader reader = new SAXReader();
	       
	       if(CONFIG_FILE_ABS == null){
	    	   
	    	   try{
	    		   CONFIG_FILE_ABS_PATH = ConfigReader.class.getResource("/").getFile() + CONFIG_FILE_NAME;
	    		   CONFIG_FILE_ABS_PATH = java.net.URLDecoder.decode(CONFIG_FILE_ABS_PATH,"utf-8");  
	    		   System.out.println(CONFIG_FILE_ABS_PATH);
	    		   CONFIG_FILE_ABS = new File(CONFIG_FILE_ABS_PATH);
	    		   if(!CONFIG_FILE_ABS.exists()){
	    			   InputStream is = ConfigReader.class.getResourceAsStream(CONFIG_FILE_PATH);
			    	   FileOutputStream fos = new FileOutputStream(CONFIG_FILE_ABS);
			    	   FileUtil.copyFile(is, fos); 
	    		   }
	    	   }catch(Exception e){
	    		   e.printStackTrace();
	    	   }
	    	   
	       }
	       
	       Document document = reader.read(CONFIG_FILE_ABS);
	       return document;
	}
	
	public static void save(Document document) throws Exception{
		 
		if(format == null){
			format = OutputFormat.createPrettyPrint(); 
			format.setEncoding("utf-8");// 设置XML文件的编码格式  
		}
		
//		OutputStream out = new FileOutputStream(ConfigReader.class.getResource(CONFIG_FILE_PATH).toURI().getPath());
		OutputStream out = new FileOutputStream(CONFIG_FILE_ABS); 
		Writer wr = new OutputStreamWriter(out, "UTF-8");  
		
		writer = new XMLWriter(wr, format);
        writer.write(document);  
        writer.close();
	}

}
