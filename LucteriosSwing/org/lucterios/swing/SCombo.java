package org.lucterios.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIButton.GUIActionListener;

public class SCombo extends JComboBox implements GUICombo, FocusListener,ActionListener  {

	private static final long serialVersionUID = 1L;

	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	private ArrayList<GUIActionListener> mActionListener=new ArrayList<GUIActionListener>(); 
	
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

	private DefaultComboBoxModel m_comboModel;
	
	public SCombo(){
        super();
        addFocusListener(this);	
        addActionListener(this);
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
		
}
