package org.lucterios.swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.graphic.SwingImage;
import org.lucterios.gui.AbstractImage;
import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIComponent;
import org.lucterios.ui.GUIActionListener;

public class SButton extends JComponent implements GUIButton {
	private static final long serialVersionUID = 1L;

	private CursorMouseListener mCursorMouseListener;
	
	private AbstractButton mButton;
	public AbstractButton getButton(){
		return mButton;
	}

	// FOCUS 
	private FocusListenerList mFocusListener=new FocusListenerList(); 
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	// ACTION
	public void addActionListener(GUIActionListener l){
		mCursorMouseListener.add(l);
	}

	public void removeActionListener(GUIActionListener l){
		mCursorMouseListener.remove(l);
	}

	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SButton(GUIComponent aOwner){
        super();
        mOwner=aOwner;
        mCursorMouseListener=new CursorMouseListener(this,this);
        setLayout(new GridLayout());
        setFocusable(false);
        mButton=new JButton();
        mButton.addActionListener(mCursorMouseListener);
        mButton.addFocusListener(mFocusListener);
        add(mButton);
        addFocusListener(mFocusListener);
        addMouseListener(mCursorMouseListener);
	}

	public void doClick() {		
		mCursorMouseListener.actionPerformed(null);
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
        mButton.addActionListener(mCursorMouseListener);
        mButton.addFocusListener(mFocusListener);
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

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		mButton.setVisible(aFlag);
		mButton.setFocusable(aFlag);
	}
	
	@Override
	public void setEnabled(boolean aEnabled) {
		super.setEnabled(aEnabled);
		mButton.setEnabled(aEnabled);
	}	

	private Object mObject=null;
	public Object getObject() {
		return mObject;
	}

	public void setObject(Object object) {
		mObject=object;
	}

	public boolean isActive() {
		return getOwner().isActive();
	}
	
}
