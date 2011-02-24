package org.lucterios.gui;

import org.lucterios.gui.GUIContainer.ContainerType;
import org.lucterios.gui.GUIContainer.FillMode;
import org.lucterios.gui.GUIContainer.ReSizeMode;
import org.lucterios.gui.IDialog.DialogVisitor;

public class DialogExample implements DialogVisitor {

	private IDialog mDialog;
	
	public void execute(IDialog dialog) {
		mDialog=dialog;
		mDialog.setTitle("Exemple dialogue");
		initScroll();	
		initBtn();				
	}

	private void initScroll() {
		GUIContainer sv=mDialog.getContainer().createContainer(ContainerType.CT_SCROLL, 0, 0, 3, 1, ReSizeMode.RSM_BOTH, FillMode.FM_BOTH);
		for(int index=0;index<10;index++) {
			GUILabel tv=sv.createLabel(0, index, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE);
			tv.setTextString("Text"+(index+1));
			GUIEdit et=sv.createEdit(1, index, 1, 1, ReSizeMode.RSM_HORIZONTAL, FillMode.FM_HORIZONTAL);
			et.setTextString("---");
		}
	}

	private void initBtn() {
		GUIButton bt1=mDialog.getContainer().createButton(0, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE);
        bt1.setTextString("Bt1");
		GUIButton bt2=mDialog.getContainer().createButton(1, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE);
        bt2.setTextString("Bt2");
		GUIButton bt3=mDialog.getContainer().createButton(2, 3, 1, 1, ReSizeMode.RSM_NONE, FillMode.FM_NONE);
        bt3.setTextString("Bt3");
	}

}
