package org.lucterios.swing;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.lucterios.graphic.CursorMouseListener;
import org.lucterios.graphic.FocusListenerList;
import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUIComponent;
import org.lucterios.gui.GUIGrid.GUISelectListener;
import org.lucterios.ui.GUIActionListener;

public class SCheckList extends JScrollPane implements GUICheckList, ListSelectionListener {
	private static final long serialVersionUID = 1L;

	private JList cmp_list;
	
	private CursorMouseListener mCursorMouseListener;
	
	private FocusListenerList mFocusListener=new FocusListenerList(); 
	private ArrayList<GUISelectListener> mSelectListener=new ArrayList<GUISelectListener>(); 
	
	public void clearFocusListener() {
		mFocusListener.clear();
	}
		
	public void addFocusListener(GUIFocusListener l){
		mFocusListener.add(l);
	}

	public void removeFocusListener(GUIFocusListener l){
		mFocusListener.remove(l);
	}

	public void addSelectListener(GUISelectListener l){
		mSelectListener.add(l);
	}

	public void removeSelectListener(GUISelectListener l) {
		mSelectListener.remove(l);
	}	
	
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		for(GUISelectListener l:mSelectListener)
			l.selectionChanged();
	}
	
	private GUIComponent mOwner=null;
	public GUIComponent getOwner(){
		return mOwner;
	}	
	
	public SCheckList(GUIComponent aOwner){
        super();
        mOwner=aOwner;
		cmp_list = new JList();
        mCursorMouseListener=new CursorMouseListener(cmp_list,this);
		cmp_list.setFocusable(true);
		cmp_list.setAutoscrolls(true);
		cmp_list.addListSelectionListener(this);
		cmp_list.addMouseListener(mCursorMouseListener);
		cmp_list.addFocusListener(mFocusListener);
		setViewportView(cmp_list);
		setFocusable(false);
	}
	
	public void setListData(Object[] listData){
		cmp_list.setListData(listData);
	}

	public void clearList() {
		cmp_list.removeAll();
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

	public void setActiveMouseAction(boolean isActive) {
		mCursorMouseListener.setActiveMouseAction(isActive);		
	}
	
	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		cmp_list.setFocusable(aFlag);
	}

	public void setSelectedIndices(int[] indices) {
		cmp_list.setSelectedIndices(indices);
	}

	public boolean isActive() {
		return getOwner().isActive();
	}
	
	public void requestFocusGUI() {
		requestFocus();
	}

	public void setNbClick(int mNbClick) {
		mCursorMouseListener.setNbClick(mNbClick);
	}
}
