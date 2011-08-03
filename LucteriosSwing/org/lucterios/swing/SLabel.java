package org.lucterios.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUILabel;
import org.lucterios.ui.GUIActionListener;

public class SLabel extends JLabel implements GUILabel,MouseListener {

	private static final long serialVersionUID = 1L;

	public void addFocusListener(GUIFocusListener l) {}

	public void removeFocusListener(GUIFocusListener l) {}

	public SLabel(){
		super();
		setFocusable(false);
		addMouseListener(this);
	}
	
	public AbstractImage getImage() {		
		return new SwingImage(getIcon());
	}

	public void setImage(AbstractImage image) {
		if (image instanceof SwingImage)
			setIcon((ImageIcon)image.getData());		
	}

	public void setImage(URL image) {
		setIcon(new ImageIcon(image));
	}
	
	public String getTextString() {
		return getText();
	}

	public void setTextString(String text) {
		setText(text);		
	}

	public void setStyle(int style) {
		Font font=getFont();
		setFont(new Font(font.getName(), style, font.getSize()));		
	}	

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));		
	}
	
	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>();
	public void addActionListener(GUIActionListener l){
		mActionListener.add(l);
	}

	public void removeActionListener(GUIActionListener l){
		mActionListener.remove(l);
	}

	private boolean mIsActiveMouse=false;
	public void setActiveMouseAction(boolean isActive) {
		mIsActiveMouse=isActive;		
	}

	public void mouseEntered(MouseEvent e) {
		if (mIsActiveMouse) {
			if (!Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR).equals(getCursor())) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
	}

	public void mouseExited(MouseEvent e) {
		if (mIsActiveMouse) {
			if (Cursor.getPredefinedCursor(Cursor.HAND_CURSOR).equals(getCursor())) {
				setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public void mouseClicked(MouseEvent e) { 
		if (e.getClickCount()==2) {
			for(GUIActionListener l:mActionListener)
				l.actionPerformed();
		}
	}
	
	public void mousePressed(MouseEvent e) { }

	public void mouseReleased(MouseEvent e) { }	
}
