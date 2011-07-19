package org.lucterios.gui.test;

import org.lucterios.gui.GUIButton;
import org.lucterios.gui.GUIContainer;
import org.lucterios.gui.GUIDialog;
import org.lucterios.gui.GUIEdit;
import org.lucterios.gui.GUILabel;
import org.lucterios.gui.GUIParam;
import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIDialog.DialogVisitor;
import org.lucterios.gui.GUIParam.FillMode;
import org.lucterios.gui.GUIParam.ReSizeMode;

public class DialogExample implements DialogVisitor {

	private GUIDialog mDialog;
	
	public void execute(GUIDialog dialog) {
		mDialog=dialog;
		mDialog.setTitle("Exemple dialogue");
		initScroll();	
		initBtn();				
	}

	private void initScroll() {
		GUIContainer sv=mDialog.getContainer().createContainer(ContainerType.CT_SCROLL, new GUIParam(0, 0, 3, 1));
		for(int index=0;index<10;index++) {
			GUILabel tv=sv.createLabel(new GUIParam(0, index,1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
			tv.setTextString("Text"+(index+1));
			GUIEdit et=sv.createEdit(new GUIParam(1, index, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL));
			et.setTextString("---");
		}
	}

	private void initBtn() {
		GUIButton bt1=mDialog.getContainer().createButton(new GUIParam(0, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt1.setTextString("Bt1");
		GUIButton bt2=mDialog.getContainer().createButton(new GUIParam(1, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt2.setTextString("Bt2");
		GUIButton bt3=mDialog.getContainer().createButton(new GUIParam(2, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE));
        bt3.setTextString("Bt3");
	}

}
