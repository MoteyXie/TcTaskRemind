package com.motey.model;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import com.motey.utils.ImageUtil;

public class PartIcon {
	
	public static Map<String, ImageIcon> icons = null;
	
	static{
		icons = new HashMap<>();
		icons.put("Part", 				createIcon("/icons/part_32.png"));
		icons.put("Item", 				createIcon("/icons/item_32.png"));
		icons.put("Item Revision", 		createIcon("/icons/item_revision_32.png"));
		icons.put("Document", 			createIcon("/icons/DOCUMENT_32.png"));
		icons.put("Document Revision", 	createIcon("/icons/documentrevision_32.png"));
		icons.put("Dataset", 			createIcon("/icons/DATASET_32.png"));
		icons.put("PDF", 				createIcon("/icons/pdf_32.png"));
		icons.put("Excel", 				createIcon("/icons/exceldataset_32.png"));
		icons.put("Word", 				createIcon("/icons/worddataset_32.png"));
		icons.put("Design", 			createIcon("/icons/design_obj_32.png"));
		icons.put("Design Revision", 	createIcon("/icons/design_obj_rev_32.png"));
	}
	
	public static ImageIcon getIcon(String typeName){
		
		ImageIcon icon = icons.get(typeName);
		if(icon == null)icon = icons.get("Item");
		return icon;
	}
	
	public static ImageIcon createIcon(String resourcePath){
		return ImageUtil.getImageIcon(resourcePath, 20, 20);
	}

}
