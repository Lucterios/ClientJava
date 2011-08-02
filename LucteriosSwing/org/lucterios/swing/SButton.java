package org.lucterios.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.ui.GUIActionListener;

public class SButton extends JComponent implements GUIButton, FocusListener,ActionListener,MouseListener {
	private static final long serialVersionUID = 1L;

	private AbstractButton mButton;

	// FOCUS 
	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void focusLost(FocusEvent e) {
		for(GUIFocusListener l:mFocusListener)
			l.focusLost();
	}
	public void focusGained(FocusEvent e) {	}

	// ACTION
	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>();
	public void addActionListener(GUIActionListener l){
		if (l!=null)
			mActionListener.add(l);
	}

	public void removeActionListener(GUIActionListener l){
		if (l!=null)
			mActionListener.remove(l);
	}

	public void actionPerformed(ActionEvent e) {
		for(GUIActionListener l:mActionListener)
			l.actionPerformed();
	}
	
	public SButton(){
        super();
        setLayout(new GridLayout());
        mButton=new JButton();
        mButton.addActionListener(this);
        mButton.addFocusListener(this);
        add(mButton);
        addFocusListener(this);
        addMouseListener(this);
	}

	public void doClick() {		
		actionPerformed(null);
	}
	
	public String getTextString() {
		return mButton.getText();
	}

	public void setTextString(String text) {
		mButton.setText(text);		
	}

	public void setToggle(boolean isToogle) {
        removeAll();		
        if (isToogle)
        	mButton=new JToggleButton();
        else
        	mButton=new JButton();
        mButton.addActionListener(this);
        mButton.addFocusListener(this);
        add(mButton);
	}

	public boolean isSelected() {
		return mButton.isSelected();
	}

	public void setSelected(boolean select) {
		mButton.setSelected(select);
	}
	
	public void setMnemonic(char c) {
		mButton.setMnemonic(c);
	}

	public void setBackgroundColor(int color) {
		mButton.setBackground(new Color(color));
		
	}

	public int getBackgroundColor() {
		return mButton.getBackground().getRGB();		
	}
	
	public void setImage(URL image) {
		mButton.setIcon(new ImageIcon(image));
	}

	public void setImage(AbstractImage image) {
		if (image instanceof SwingImage)
			mButton.setIcon((ImageIcon)image.getData());
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

	public void mouseClicked(MouseEvent e) { }
	
	public void mousePressed(MouseEvent e) { }

	public void mouseReleased(MouseEvent e) { }	
	
}
