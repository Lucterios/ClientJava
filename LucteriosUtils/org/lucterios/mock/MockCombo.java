package org.lucterios.mock;

import java.util.ArrayList;

import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIComponent;
import org.lucterios.ui.GUIActionListener;

public class MockCombo extends MockComponent implements GUICombo  {

	private static final long serialVersionUID = 1L;

	public void addFocusListener(GUIFocusListener l){
	}

	public void addActionListener(GUIActionListener l){
	}

	public void removeFocusListener(GUIFocusListener l){
	}

	public void removeActionListener(GUIActionListener l){
	}

	public MockCombo(GUIComponent aOwner){
        super(aOwner);
	}
	
	private ArrayList<Object> m_comboModel=new ArrayList<Object>();
	public void removeAllElements(){
		m_comboModel.clear();
	}
	
	public void addElement(Object obj){
		m_comboModel.add(obj);
	}

	public void addList(Object[] Items) {
		removeAllElements();
		for(Object obj:Items)
			addElement(obj);
	}

	private int mSelectedIndex=-1;
	public void setSelectedIndex(int index) {
		mSelectedIndex=index;
	}
	public int getSelectedIndex() {
		return mSelectedIndex;
	}

	public Object getSelectedItem() {
		return m_comboModel.get(mSelectedIndex);
	}

	
}
