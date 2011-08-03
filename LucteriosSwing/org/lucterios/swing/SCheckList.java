package org.lucterios.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.ui.GUIActionListener;

public class SCheckList extends JScrollPane implements GUICheckList, ListSelectionListener,MouseListener {
	private static final long serialVersionUID = 1L;

	JList cmp_list;
	
	private ArrayList<GUIFocusListener> mFocusListener=new ArrayList<GUIFocusListener>(); 
	private ArrayList<GUISelectListener> mSelectListener=new ArrayList<GUISelectListener>(); 
	
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void addSelectListener(GUISelectListener l){
		mSelectListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void removeSelectListener(GUISelectListener l) {
		mSelectListener.remove(l);
	}	
	
    public void focusLost(java.awt.event.FocusEvent evt) 
    {
		for(GUIFocusListener l:mFocusListener)
			l.focusLost();
    }
    
	public void focusGained(FocusEvent e) { }
	
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		for(GUISelectListener l:mSelectListener)
			l.selectionChanged();
	}
	
	public SCheckList(){
		super();
		cmp_list = new javax.swing.JList();
		cmp_list.setFocusable(true);
		cmp_list.setAutoscrolls(true);
		cmp_list.addListSelectionListener(this);
		cmp_list.addMouseListener(this);
		setViewportView(cmp_list);
		setFocusable(false);
	}
	
	public void setListData(Object[] listData){
		cmp_list.setListData(listData);
	}

	public void setMultiSelection(boolean isMultiSelection) {
		cmp_list.setSelectionMode(isMultiSelection?ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:ListSelectionModel.SINGLE_SELECTION);
	}
	
	public Object[] getSelectedValues(){
		return cmp_list.getSelectedValues();
	}

	public void setBackgroundColor(int color) {
		setBackground(new Color(color));	
	}
	public int getBackgroundColor(){
		return getBackground().getRGB();
	}

	public void addActionListener(GUIActionListener l) { }

	public void removeActionListener(GUIActionListener l) { }

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
	
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		setFocusable(aFlag);
	}
}
