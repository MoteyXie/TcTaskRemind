package com.motey.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.motey.utils.ImageUtil;
import com.motey.utils.MyWindowUtil;

import java.awt.Font;

/**
 * 顶部窗口
 * @author Motey
 *
 */
public class TopPanel4Dialog extends JPanel {

	private static final long serialVersionUID = -69041544241418785L;
	public static final Color DEFAULT_BACKGROUND = new Color(135,205,250);
	private JLabel close;
//	private JLabel minimize;
	private JDialog dialog;
	private ImageIcon titleIcon;
	public int width;
	public int height;
	private JLabel titleComponent;
	
	public TopPanel4Dialog(String title, int width, int height, Color background, ImageIcon titleIcon) {

		this.titleIcon = titleIcon;
		this.width = width;
		this.height = height;
		setLayout(null);
		
		if(background != null){
			setBackground(background);
		}else{
			setOpaque(false);
		}
		
		titleComponent = new JLabel(title);
		titleComponent.setFont(new Font("微软雅黑", Font.PLAIN, 17));
		titleComponent.setBounds(37, (height>>1) - 10, width>>1, height>>1);
		add(titleComponent);
		
		ImageIcon closeIcon = ImageUtil.getImageIcon("/icons/close_116.png", 30,30);
		
//		ImageIcon minimizeIcon = ImageUtil.getImageIcon("/icons/minimize_116.png", 30,30);
        
		close = new JLabel();
		close.setIcon(closeIcon);
		close.setHorizontalAlignment(SwingConstants.CENTER);
		close.setForeground(new Color(255, 255, 240));
		close.setFont(new Font("微软雅黑", Font.BOLD, 17));
		close.setBounds(width - height - 1, 1, height, height-1);
		MyWindowUtil.setChangeBackgroundListener(close, Color.RED);
	
//		minimize = new JLabel();
//		minimize.setIcon(minimizeIcon);
//		minimize.setHorizontalAlignment(SwingConstants.CENTER);
//		minimize.setForeground(new Color(255, 255, 240));
//		minimize.setFont(new Font("微软雅黑", Font.BOLD, 13));
//		minimize.setBounds(width - (height<<1) - 1, 1, height, height-1);
//		MyWindowUtil.setChangeBackgroundListener(minimize, Color.RED);
		
		add(close);
//		add(minimize);
		
		setBounds(0, 0, width, height);
		
	}
	
	public void setTitleForeground(Color color){
		titleComponent.setForeground(color);
	}
	
	public void setFontSize(int size){
		titleComponent.setFont(new Font("微软雅黑", Font.PLAIN, size));
	}
	
	public void setParent(final JDialog dialog, final boolean disposeWhenClickedClose){
		this.dialog = dialog;
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(disposeWhenClickedClose){
					dialog.dispose();
				}else{
					dialog.setVisible(false);
				}
				
			}
		});

	}
	
	public void setParent(JDialog dialog){
		setParent(dialog, true);
	}
	
	/**
	 * 设置窗口可拖动
	 * 调用此方法前需保证parent是存在的
	 */
	public void setDragable(){
		MyWindowUtil.setDragable(dialog, this);
	}
	
	/**
	 * 设置允许通过点击此控件拖动窗口
	 * @param frame 窗口
	 */
	public void setDragable(JDialog dialog){
		setParent(dialog);
		setDragable();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
        
		if(titleIcon != null){
			
			int iconWidth = titleIcon.getIconWidth();
			int iconHeight = titleIcon.getIconHeight();
			
			int x = 5;
			int y = (height - iconHeight)>>1;
		
			g.drawImage(titleIcon.getImage(), x, y, iconHeight+x, iconHeight+y, 0, 0, iconWidth, iconHeight, null);
		}

	}
}
