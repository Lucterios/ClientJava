package org.lucterios.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUICheckBox;
import org.lucterios.gui.GUICheckList;
import org.lucterios.gui.GUICombo;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUIGrid;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIMemo;
import org.lucterios.gui.GUISpinEdit;

public class SContainer extends JPanel implements GUIContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ContainerType mType;
	private JPanel mPanel=null;
	private JTabbedPane mTab=null;
	
	public SContainer(ContainerType type) {
		super();
		mType=type;
		switch(mType) {
			case CT_NORMAL:
				mPanel=this;
				mPanel.setLayout(new GridBagLayout());
				break;
			case CT_SCROLL:
				mPanel=new JPanel();
				mPanel.setLayout(new GridBagLayout());
				JScrollPane scoll=new JScrollPane(mPanel);
				scoll.setFocusable(true);
				scoll.setAutoscrolls(true);
				this.setLayout(new GridBagLayout());
				add(scoll,getCnt(0,0,1,1,ReSizeMode.RSM_BOTH, FillMode.FM_BOTH));
				break;
			case CT_TAB:
				this.setLayout(new GridBagLayout());
				mTab=new JTabbedPane();
				add(mTab,getCnt(0,0,1,1,ReSizeMode.RSM_BOTH, FillMode.FM_BOTH));
				break;
		}
	}
	
	public ContainerType getType(){
		return mType;
	}

	protected GridBagConstraints getCnt(int x, int y, int w, int h, ReSizeMode reSize, FillMode fill) {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;
		gridBagConstraints.gridheight = h;
		gridBagConstraints.gridwidth = w;
		switch (reSize) {
			case RSM_NONE:
				gridBagConstraints.weightx = 0;
				gridBagConstraints.weighty = 0;
				break;
			case RSM_HORIZONTAL:
				gridBagConstraints.weightx = 1;
				gridBagConstraints.weighty = 0;
				break;
			case RSM_VERTICAL:
				gridBagConstraints.weightx = 0;
				gridBagConstraints.weighty = 1;
				break;
			case RSM_BOTH:
				gridBagConstraints.weightx = 1;
				gridBagConstraints.weighty = 1;
				break;
		}		
		switch (fill) {
			case FM_NONE:
				gridBagConstraints.fill = GridBagConstraints.NONE;
				break;
			case FM_HORIZONTAL:
				gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
				break;
			case FM_VERTICAL:
				gridBagConstraints.fill = GridBagConstraints.VERTICAL;
				break;
			case FM_BOTH:
				gridBagConstraints.fill = GridBagConstraints.BOTH;
				break;
		}
		gridBagConstraints.insets = new Insets(1, 1, 1, 1);
		return gridBagConstraints;
	}

	public GUIContainer addTab(ContainerType type,String name) {
		if (mTab==null) return null;
		SContainer result=new SContainer(type);
		mTab.addTab(name,result);
		return result;
	}
	
	public GUIButton createButton(int x, int y, int w, int h, ReSizeMode reSize,
			FillMode fill) {
		if (mPanel==null) return null;
		SButton result=new SButton();
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public GUICheckBox createCheckBox(int x, int y, int w, int h,
			ReSizeMode reSize, FillMode fill) {
		if (mPanel==null) return null;
		SCheckBox result=new SCheckBox();
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public GUICheckList createCheckList(int x, int y, int w, int h,
			ReSizeMode reSize, FillMode fill) {
		if (mPanel==null) return null;
		SCheckList result=new SCheckList();
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public GUICombo createCombo(int x, int y, int w, int h, ReSizeMode reSize,
			FillMode fill) {
		if (mPanel==null) return null;
		SCombo result=new SCombo();
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public GUIContainer createContainer(ContainerType type,int x, int y, int w, int h,
			ReSizeMode reSize, FillMode fill) {
		if (mPanel==null) return null;
		SContainer result=new SContainer(type);
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public GUIEdit createEdit(int x, int y, int w, int h, ReSizeMode reSize,
			FillMode fill) {
		if (mPanel==null) return null;
		SEdit result=new SEdit();
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public GUIGrid createGrid(int x, int y, int w, int h, ReSizeMode reSize,
			FillMode fill) {
		if (mPanel==null) return null;
		SGrid result=new SGrid();
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public GUILabel createLabel(int x, int y, int w, int h, ReSizeMode reSize,
			FillMode fill) {
		if (mPanel==null) return null;
		SLabel result=new SLabel();
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public GUIMemo createMemo(int x, int y, int w, int h, ReSizeMode reSize,
			FillMode fill) {
		if (mPanel==null) return null;
		SMemo result=new SMemo();
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public GUISpinEdit createSpinEdit(int x, int y, int w, int h,
			ReSizeMode reSize, FillMode fill) {
		if (mPanel==null) return null;
		SSpinEdit result=new SSpinEdit();
		mPanel.add(result, getCnt(x,y,w,h,reSize,fill));
		return result;
	}

	public void calculBtnSize(GUIButton[] btns) {
        int wbtn=0;
        int hbtn=0;
        for(GUIButton btn:btns) {
            wbtn=Math.max(wbtn,((SButton)btn).getPreferredSize().width);
            hbtn=Math.max(hbtn,((SButton)btn).getPreferredSize().height);
        }
        for(GUIButton btn:btns) {
        	((SButton)btn).setPreferredSize(new java.awt.Dimension(wbtn,hbtn));
        	((SButton)btn).setMaximumSize(new java.awt.Dimension(wbtn,hbtn));
        	((SButton)btn).setMinimumSize(new java.awt.Dimension(wbtn,hbtn));
        }
	}

}
