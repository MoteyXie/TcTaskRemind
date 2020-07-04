package com.motey.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.motey.utils.ImageUtil;
import com.motey.utils.MyWindowUtil;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageDialog extends JDialog {

	public static final int INFOMATION = 1;
	public static final int ERROR = 2;
	public static final int RIGHT = 3;
	public static final int WARNING = 4;
	
	public static final ImageIcon INFOMATION_ICON = ImageUtil.getImageIcon("/icons/alert-info.png", 40, 40);
	public static final ImageIcon ERROR_ICON = ImageUtil.getImageIcon("/icons/alert-error.png", 40, 40);
	public static final ImageIcon RIGHT_ICON = ImageUtil.getImageIcon("/icons/alert-right.png", 40, 40);
	public static final ImageIcon WARNING_ICON = ImageUtil.getImageIcon("/icons/alert-warning.png", 40, 40);
	
	private static final long serialVersionUID = 995332901043950732L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MessageDialog dialog = new MessageDialog("提醒信息", ERROR, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MessageDialog(String msg, int type, Component parent) {
		
		MyWindowUtil.setWindowsStyle();
		setModal(true);
		setBounds(100, 100, 400, 130);
		setLocationRelativeTo(parent);
		setAlwaysOnTop(true);
		setUndecorated(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		
		TopPanel4Dialog topPanel = new TopPanel4Dialog("提示", 400, 30, Color.BLACK, ImageUtil.DEFAULT_ICON);
		topPanel.setTitleForeground(Color.WHITE);
		topPanel.setParent(this);
		topPanel.setDragable();
		contentPanel.add(topPanel);
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEnabled(false);
		textArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		textArea.setBackground(contentPanel.getBackground());
		textArea.setBounds(71, 45, 315, 55);
		textArea.setText(msg);
		contentPanel.add(textArea);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(getIcon(type));
		lblNewLabel.setBounds(10, 55, 40, 41);
		contentPanel.add(lblNewLabel);
		{
			JButton okButton = new JButton("确定");
			okButton.setBounds(323, 100, 63, 27);
			contentPanel.add(okButton);
			okButton.setActionCommand("确定");
			getRootPane().setDefaultButton(okButton);
			okButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
		}
	}
	
	private ImageIcon getIcon(int type){
		
		ImageIcon icon = null;
		switch (type) {
		case INFOMATION:icon = INFOMATION_ICON;
			break;
		case ERROR:icon = ERROR_ICON;
			break;
		case RIGHT:icon = RIGHT_ICON;
			break;
		case WARNING:icon = WARNING_ICON;
			break;
		default:icon = INFOMATION_ICON;
			break;
		}
		
		return icon;
	}
}
