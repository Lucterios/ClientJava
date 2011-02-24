package org.lucterios.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;

public class SButton extends JButton implements GUIButton, FocusListener,ActionListener {
	private static final long serialVersionUID = 1L;

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>(); 
	
	public void setImage(AbstractImage image) {
		if (image instanceof SwingImage)
			setIcon((ImageIcon)image.getData());
	}

	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void addActionListener(GUIActionListener l){
		mActionListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void removeActionListener(GUIActionListener l){
		mActionListener.remove(l);
	}

	public void focusLost(FocusEvent e) {
		for(GUIFocusListener l:mFocusListener)
			l.focusLost();
	}
	public void focusGained(FocusEvent e) {	}
	public void actionPerformed(ActionEvent e) {
		for(GUIActionListener l:mActionListener)
			l.actionPerformed();
	}
	
	public SButton(){
        super();
        addFocusListener(this);	
        addActionListener(this);
	}

	public String getTextString() {
		return getText();
	}

	public void setTextString(String text) {
		setText(text);		
	}
	
}
