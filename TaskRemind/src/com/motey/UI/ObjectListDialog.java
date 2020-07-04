package com.motey.UI;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

import com.motey.model.MyProperty;
import com.motey.model.PartIcon;
import com.teamcenter.soa.client.model.ModelObject;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.GridLayout;

public class ObjectListDialog extends JDialog {

	private static final long serialVersionUID = -6355013858705204182L;

	private static ObjectListDialog instance = new ObjectListDialog();
	private final JPanel contentPanel = new JPanel();
	private ModelObject[] objects;

	private JPanel objectPanel;

	public static ObjectListDialog getInstance(){
		instance.setVisible(true);
		return instance;
	}
	
	private ObjectListDialog() {
		
		setUndecorated(true);
		setBounds(100, 100, 215, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(173, 216, 230));
		contentPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				setVisible(false);
			}
		});
		
		lblNewLabel.setIcon(new ImageIcon(ObjectListDialog.class.getResource("/icons/cancel_16.png")));
		lblNewLabel.setBounds(195, 2, 18, 18);
		contentPanel.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(2, 23, 211, 175);
		contentPanel.add(scrollPane);
		
		objectPanel = new JPanel();
		scrollPane.setViewportView(objectPanel);
		objectPanel.setLayout(new GridLayout(6, 1, 0, 0));

		addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent arg0) {
				setVisible(false);
			}
			
			@Override
			public void windowGainedFocus(WindowEvent arg0) {
				
			}
		});
	}
	
	public void resetObjectPanel(){
		if(objects == null || objects.length == 0)return;
		objectPanel.removeAll();
		for (ModelObject object : objects) {
			try {
				
				String displayName = MyProperty.getStringProperty(object, "object_string");
				String className = object.getTypeObject().getClassName();
				ImageIcon icon = PartIcon.getIcon(className);
				JLabel lb = new JLabel(displayName);
				lb.setIcon(icon);
				objectPanel.add(lb);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		objectPanel.updateUI();
	}

	public void setObject(ModelObject[] objects) {
		this.objects = objects;
		resetObjectPanel();
	}
}
