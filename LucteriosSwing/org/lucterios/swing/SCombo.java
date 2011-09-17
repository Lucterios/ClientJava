package org.lucterios.swing;

import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIComponent;
import org.lucterios.ui.GUIActionListener;

public class SCombo extends JComboBox implements GUICombo  {

	private static final long serialVersionUID = 1L;

	private CursorMouseListener mCursorMouseListener;
	private FocusListenerList mFocusListener=new FocusListenerList(); 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void addActionListener(GUIActionListener l){
		mCursorMouseListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void removeActionListener(GUIActionListener l){
		mCursorMouseListener.remove(l);
	}

	private DefaultComboBoxModel m_comboModel;
	
	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SCombo(GUIComponent aOwner){
        super();
        mOwner=aOwner;
        mCursorMouseListener=new CursorMouseListener(this,this);
        addFocusListener(mFocusListener);	
        addActionListener(mCursorMouseListener);
        addMouseListener(mCursorMouseListener);
		m_comboModel = new DefaultComboBoxModel();
		setModel(m_comboModel);
	}
	
	public void removeAllElements(){
		m_comboModel.removeAllElements();
	}
	
	public void addElement(Object obj){
		m_comboModel.addElement(obj);
	}

	public void addList(Object[] Items) {
		removeAllElements();
		for(Object obj:Items)
			addElement(obj);
	}
		
	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
	}

	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		setFocusable(aFlag);
	}

	public boolean isActive() {
		return getOwner().isActive();
	}
	
	public void requestFocusGUI() {
		requestFocus();
	}
}
